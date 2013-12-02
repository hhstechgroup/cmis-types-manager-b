package com.engagepoint.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: michael.vakulik
 * Date: 12/2/13
 * Time: 1:44 PM
 */
@ManagedBean(name="navigation")
@SessionScoped
public class NavigationBean implements Serializable {
    private static final String TO_LOGIN = "login?faces-redirect=true";
    private static final String TO_MAIN_PAGE = "dashboard/index?faces-redirect=true";
    private static final String TO_CREATE_TYPE = "type?faces-redirect=true";

    public String toLogin(){
        return TO_LOGIN;
    }

    public String toMainPage(){
        return TO_MAIN_PAGE;
    }

    public String toCreateType(){
        return TO_CREATE_TYPE;
    }

    public String toUpdateType(){
        return TO_CREATE_TYPE;
    }

    public String toViewType(){
        return TO_CREATE_TYPE;
    }
}
