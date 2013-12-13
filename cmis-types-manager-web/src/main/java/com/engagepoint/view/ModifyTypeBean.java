package com.engagepoint.view;

import com.engagepoint.services.CmisService;
import com.engagepoint.services.TypeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private TypeProxy selectedType;
    private String secondaryId = "cmis:secondary";

    @PostConstruct
    public void init() {
       selectedType = navigationBean.getTypeProxy();
    }

    public Boolean isSecondary() {
        return selectedType.getBaseType().equals(secondaryId);
    }


    public String getBaseType() {
        return selectedType.getBaseType();
    }

    public String getParentType() {
        if (selectedType.getBaseType().equals(secondaryId))
            return secondaryId;
        else
            return selectedType.getId();
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
