package com.engagepoint.managedBeans;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.LoginInfo;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by Qnex.
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    @EJB
    private CmisService service;
    private LoginInfo loginInfo = new LoginInfo();


    public String doLogin() {
        return "index";
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
        try {
            return service.isValidUser(loginInfo);
        } catch (CmisConnectException e) {
            FacesContext.getCurrentInstance().addMessage("exceptions", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
        }
        return false;
    }

    public String logout() {
        loginInfo.reset();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
    }
}
