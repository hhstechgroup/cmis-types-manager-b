package com.engagepoint;

/**
 * Created by Qnex.
 */
public class LoginInfo {
    private String userName;
    private String password;
    private String url;


    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
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

    public boolean isEmpty() {
        return ((userName == null) ||
                (password == null) ||
                (url == null));
    }
}
