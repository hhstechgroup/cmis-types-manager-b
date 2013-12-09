package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.Prototype;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * User: AlexDenisenko
 * Date: 03.12.13
 * Time: 9:32
 */
@ManagedBean
@ViewScoped
public class ModifyTypeBean implements Serializable {
    private Logger log = LoggerFactory.getLogger(ModifyTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private Prototype prototype;
    private TypeProxy type;
    private String secondaryId = "cmis:secondary";

    @PostConstruct
    public void init() {
        getParametersFromFlash();
        prototype = new Prototype();
    }

    public Boolean isSecondary() {
        if (type.getBaseType().equals(secondaryId))
            return true;
        else
            return false;
    }

    public String createType() {
        try {
            UserInfo userInfo = login.getUserInfo();
            if (type.getBaseType().equals(secondaryId)) {
                prototype.setParentTypeId(secondaryId);
            } else {
                prototype.setParentTypeId(type.getId());
            }
            prototype.setBaseTypeId(type.getBaseType());
            service.createType(userInfo, prototype);
            Message.printInfo(prototype.getDisplayName() + " type created!");
            return navigationBean.toMainPage();
        } catch (CmisConnectException e) {
            Message.printInfo(e.getMessage());
            log.error("Unable to create type", e);
        } catch (CmisCreateException e) {
            Message.printInfo(e.getMessage());
            log.error("Error while create type", e);
        }
        return "";
    }


    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    public String getBaseType() {
        return type.getBaseType();
    }

    public String getParentType() {
        if (type.getBaseType().equals(secondaryId))
            return secondaryId;
        else
            return type.getId();
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

    private void getParametersFromFlash() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        type = (TypeProxy) externalContext.getFlash().get("selectedType");
    }
}
