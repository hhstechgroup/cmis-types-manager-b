package com.engagepoint.view;

import com.engagepoint.utils.MessageUtils;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;
    private UserInfo userInfo;
    private String sessionID;
    private boolean loggedIn;

    @PostConstruct
    public void init() {
        userInfo = new UserInfo();
    }

    public String doLogin() {
        try {
            userInfo.setRepository(service.getDefaultRepository(userInfo));
            if (isValid()) {
                sessionID = String.valueOf(Math.random() * 1000);
                HttpSession httpSession = getHttpSession();
                httpSession.setAttribute(Constants.Strings.SESSION_ID_DISPLAY_NAME, sessionID);
                loadRepositoryInfo();
                loggedIn = true;
                return Constants.Navigation.TO_MAIN_PAGE;
            } else {
                return Constants.Navigation.TO_LOGIN;
            }
        } catch (CmisException e) {
            String message = e.getMessage();
            if (Constants.Messages.UNEXPECTED_DOCUMENT.equals(message) || Constants.Messages.NOT_FOUND.equals(message)) {
                message = Constants.Messages.REPO_NOT_EXISTS;
            }
            MessageUtils.printError(message);
            LOGGER.error(message, e);
            return Constants.Navigation.TO_LOGIN;
        }
    }

    public String logout() {
        destroyRepositoryInfo();
        loggedIn = false;
        userInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        sessionID = Constants.Strings.EMPTY_STRING;
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute(Constants.Strings.SESSION_ID_DISPLAY_NAME, sessionID);
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

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
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

    private void loadRepositoryInfo(){
        try {
            sessionStateBean.setRepositoryInfo(service.getRepositoryInfo(userInfo));
        } catch (CmisException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void destroyRepositoryInfo(){
        sessionStateBean.destroyReposytoryInfo();
    }
}