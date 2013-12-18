package com.engagepoint.view;

import com.engagepoint.services.TypeProxy;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

/**
 * User: AlexDenisenko
 * Date: 03.12.13
 * Time: 9:32
 */
@ManagedBean
@ViewScoped
public class ModifyTypeBean implements Serializable {

    private static final String SECONDARY_ID = "cmis:secondary";

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;
    private TypeProxy selectedType;

    @PostConstruct
    public void init() {
        selectedType = navigationBean.getTypeProxy();
    }

    public Boolean isSecondary() {
        return selectedType.getBaseType().equals(SECONDARY_ID);
    }


    public String getBaseType() {
        return selectedType.getBaseType();
    }

    public String getParentType() {
        if (selectedType.getBaseType().equals(SECONDARY_ID)) {
            return SECONDARY_ID;
        } else {
            return selectedType.getId();
        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }


}
