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
public class LoginController implements Serializable {
    private String username;
    private String password;
    private String url;
    @EJB
    private CMISService service;
    private LoginInfo loginInfo = new LoginInfo();

    public String doLogin() {
        loginInfo.setUsername(username);
        loginInfo.setPassword(password);
        loginInfo.setUrl(url);
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
        if (!loginInfo.isEmpty()) {
            return service.isValidUser(loginInfo);
        }
        return false;
    }
}
