package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.Prototype;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 03.12.13
 * Time: 9:32
 */
@ManagedBean
@ViewScoped
public class ModifyTypeBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private Prototype prototype;
    private TypeProxy type;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;



    private MessagesBean messagesBean = new MessagesBean();

    public ModifyTypeBean() {


    }

    @PostConstruct
    public void init() {
        type = navigationBean.getTypeProxy();

        prototype = new Prototype();
    }
    public String createType() {
        UserInfo userInfo = login.getUserInfo();
        try {
            prototype.setParentTypeId(type.getId());
            prototype.setBaseTypeId(type.getBaseType());
            service.createType(userInfo, prototype);
            messagesBean.addMessage(FacesMessage.SEVERITY_INFO, prototype.getDisplayName() + " type created!", "");
        } catch (CmisConnectException e) {
            messagesBean.addMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        } catch (CmisCreateException e) {
            messagesBean.addMessage(FacesMessage.SEVERITY_INFO, e.getMessage(), "");
        }
        return navigationBean.toMainPage();
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

}
