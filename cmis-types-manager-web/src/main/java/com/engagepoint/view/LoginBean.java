package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import com.engagepoint.view.NavigationBean;

import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by Qnex.
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 7765876811740798583L;

    @EJB
    private CmisService service;
    private UserInfo userInfo = new UserInfo();
    private String sessionID;

    private boolean loggedIn;

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean = new NavigationBean();

    public String doLogin() {
        try {
            if (isValid()) {
                sessionID = String.valueOf(Math.random() * 1000);
                HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                httpSession.setAttribute("sessionID", sessionID);
                loggedIn = true;
                return navigationBean.toMainPage();
            } else {
                return navigationBean.toLogin();
            }
        } catch (CmisConnectException e) {
            FacesContext.getCurrentInstance().addMessage("exceptions", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return "";
    }

    public String logout() {
        loggedIn = false;
        userInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return navigationBean.toLogin();
    }

    private boolean isValid() throws CmisConnectException {
        userInfo.setRepositoryId(service.getDefaultRepositoryIdName(userInfo));
        return service.isUserExist(userInfo);
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

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}