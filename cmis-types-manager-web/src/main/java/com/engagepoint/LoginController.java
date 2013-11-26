package com.engagepoint;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Qnex.
 */
@Named
@SessionScoped
public class LoginController implements Serializable {
    @EJB
    private CmisService service;
    private LoginInfo loginInfo = new LoginInfo();



    public String doLogin() {
        if (isValid()) {
            return "index";
        }
        return "error";
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public String getUsername() {
        return loginInfo.getUsername();
    }

    public void setUsername(String username) {
        loginInfo.setUsername(username);
    }

    public String getPassword() {
        return loginInfo.getPassword();
    }

    public void setPassword(String password) {
        loginInfo.setPassword(password);
    }

    public String getUrl() {
        return loginInfo.getUrl();
    }

    public void setUrl(String url) {
        loginInfo.setUrl(url);
    }

    public boolean isValid() {
        if (!loginInfo.isEmpty()) {
            boolean test = service.isValidUser(loginInfo);
            return test;
        }
        return false;
    }

    public String logout() {
        loginInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }
}
