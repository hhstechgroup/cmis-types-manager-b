package com.engagepoint.bean;

import com.engagepoint.ejb.CmisConnection;
import com.engagepoint.exception.AppException;
import com.engagepoint.pojo.ClientSession;
import com.engagepoint.pojo.UserInfo;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.engagepoint.constant.MessageConstants.*;
import static com.engagepoint.constant.NameConstants.SESSION_DISPLAY_NAME;
import static com.engagepoint.constant.NavigationConstants.TO_LOGIN;
import static com.engagepoint.constant.NavigationConstants.TO_MAIN_PAGE;


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
    private CmisConnection connection;
    private UserInfo userInfo;
    private String sessionID;
    private boolean loggedIn;
    private ClientSession clientSession;



    @PostConstruct
    public void init() {
        userInfo = new UserInfo();
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

    public String doLogin() {
        try {
            List<Repository> repositories = connection.getRepositories(getSessionParameters());
            clientSession = new ClientSession(repositories);
            clientSession.createSession(0);
            sessionID = String.valueOf(Math.random() * 1000);
            HttpSession httpSession = getHttpSession();
            httpSession.setAttribute(SESSION_DISPLAY_NAME, sessionID);
            setLoggedIn(true);
            return TO_MAIN_PAGE;
        } catch (AppException e) {
            String message = e.getMessage();
            if (UNEXPECTED_DOCUMENT.equals(message) || NOT_FOUND.equals(message)) {
                message = REPO_NOT_EXISTS;
            }
            MessageUtils.printError(message);
            LOGGER.error(message, e);
            return TO_LOGIN;
        }
    }

    public String logout() {
        setLoggedIn(false);
        userInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        sessionID = StringUtils.EMPTY;
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute(SESSION_DISPLAY_NAME, sessionID);
        return TO_LOGIN;
    }

    private Map<String, String> getSessionParameters() {
        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put(SessionParameter.USER, userInfo.getUsername());
        parameters.put(SessionParameter.PASSWORD, userInfo.getPassword());
        parameters.put(SessionParameter.ATOMPUB_URL, userInfo.getUrl());
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        return parameters;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public ClientSession getClientSession() {
        return clientSession;
    }

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
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



}