package com.engagepoint.pojo;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * User: AlexDenisenko
 * Date: 28.12.13
 * Time: 10:02
 */
public class UserInfo {
    private String username;
    private String password;
    private String url;
    private String repositoryId;


    public UserInfo() {
        username = StringUtils.EMPTY;
        password = StringUtils.EMPTY;
        url = StringUtils.EMPTY;
        repositoryId = StringUtils.EMPTY;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
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


    public void setRepository(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void reset() {
        username = StringUtils.EMPTY;
        password = StringUtils.EMPTY;
        url = StringUtils.EMPTY;
    }
//  TODO change the connection method
    public Map<String, String> getAtomPubParameters() {
        return new HashMap<String, String>() {
            {
                put(SessionParameter.USER, username);
                put(SessionParameter.PASSWORD, password);
                put(SessionParameter.ATOMPUB_URL, url);
                put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                put(SessionParameter.REPOSITORY_ID, repositoryId);
            }
        };
    }

}
