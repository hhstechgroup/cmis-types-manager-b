package com.engagepoint;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by Qnex.
 */
@Named
@SessionScoped
public class LoginBean implements Serializable {
    private LoginInfo loginInfo = new LoginInfo();
    private String username;
    private String password;
    private String url;
    @EJB
    private CMISService service;

    public String doLogin() {
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setUrl(url);
        return "index";
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private boolean isValid() {
        return service.isValidUser(loginInfo);
    }
}
