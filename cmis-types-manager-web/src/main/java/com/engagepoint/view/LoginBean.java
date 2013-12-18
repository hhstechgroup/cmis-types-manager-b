package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
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
    public static final String SESSION_ID = "sessionID";
    private static final String NOT_FOUND = "Not Found";
    private static final String REPO_NOT_EXISTS = "The repository on this URL doesn't exist!";
    private static final String UNEXPECTED_DOCUMENT = "Unexpected document! Received: something unknown";
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
                httpSession.setAttribute(SESSION_ID, sessionID);
                loggedIn = true;
                return Constants.Navigation.TO_MAIN_PAGE;
            } else {
                return Constants.Navigation.TO_LOGIN;
            }
        } catch (CmisException e) {
            String message = e.getMessage();
            if (UNEXPECTED_DOCUMENT.equals(message) || NOT_FOUND.equals(message)){
                message = REPO_NOT_EXISTS;
            }
            Message.printError(message);
            LOGGER.error(message, e);
            return Constants.Navigation.TO_LOGIN;
        }
    }

    public String logout() {
        loggedIn = false;
        userInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        sessionID = Constants.Strings.EMPTY_STRING;
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute(SESSION_ID, sessionID);
        return Constants.Navigation.TO_LOGIN;
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

    private boolean isValid() throws CmisException {
        return service.isUserExists(userInfo);
    }
}