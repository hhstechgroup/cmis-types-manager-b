package com.engageoint.managedBeans;

import com.engageoint.instances.LoginInfo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: michael.vakulik
 * Date: 11/21/13
 * Time: 4:57 PM
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    private LoginInfo loginInfo = new LoginInfo();

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public boolean isLogged() {
      return loginInfo.isLogged();
    }

    public void doLogout() {
        loginInfo.reset();
    }

    public String doLogin() {
        //check success login
        //message
        //if not success loginInfo.reset()
        return "index";
    }

}



