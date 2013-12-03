package com.engagepoint.services;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Qnex.
 */
public class UserInfo {

    private String userName;
    private String password;
    private String url;
    private String repositoryId;


    public UserInfo() {
        userName = "";
        password = "";
        url = "";
        //TODO My. change hardecode repository
        this.repositoryId = "";//"A1";
    }

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

    public String getRepositoryId() {
        return repositoryId;
    }


    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void reset() {
        userName = "";
        password = "";
        url = "";
    }

    public boolean isEmpty(){
        return StringUtils.isEmpty(userName) &&
                StringUtils.isEmpty(password) &&
                StringUtils.isEmpty(url);
    }
}
