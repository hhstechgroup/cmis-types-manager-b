package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;


/**
 * User: AlexDenisenko
 * Date: 15/11/13
 * Time: 2:29 AM
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private UserInfo userInfo = new UserInfo();
    private String sessionID;
    private boolean loggedIn;

    public String doLogin() {
        try {
            userInfo.setRepositoryId(service.getDefaultRepositoryIdName(userInfo));
            if (isValid()) {
                sessionID = String.valueOf(Math.random() * 1000);
                HttpSession httpSession = getHttpSession();
                httpSession.setAttribute("sessionID", sessionID);
                loggedIn = true;
                return navigationBean.toMainPage();
            } else {
                return navigationBean.toLogin();
            }
        } catch (CmisConnectException e) {
            String message = e.getMessage().toString();
            if (e.getMessage().equals("Unexpected document! Received: something unknown") ||
                    e.getMessage().equals("Not Found")){
                message = "The repository on this URL doesn't exist!";
            }
            Message.printError(message);
            LOGGER.error(message, e);
            return navigationBean.toLogin();
        }
    }

    public String logout() {
        loggedIn = false;
        userInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        sessionID = "";
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute("sessionID", sessionID);
        return navigationBean.toLogin();
    }

    public String getUsername() {
        return userInfo.getUsername();
    }

    public void setUsername(String username) {
        userInfo.setUsername(username);
    }

    public String getPassword() {
        return userInfo.getPassword();
    }

    public void setPassword(String password) {
        userInfo.setPassword(password);
    }

    public String getUrl() {
        return userInfo.getUrl();
    }

    public void setUrl(String url) {
        userInfo.setUrl(url);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private HttpSession getHttpSession() {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    private boolean isValid() throws CmisConnectException {
        return service.isUserExist(userInfo);
    }
}