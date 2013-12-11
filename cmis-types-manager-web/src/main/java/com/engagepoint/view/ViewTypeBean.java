package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.Prototype;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collection;

/**
 * User: AlexDenisenko
 * Date: 01.12.13
 * Time: 11:46
 */
@ManagedBean
@ViewScoped
public class ViewTypeBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;

    private MessagesBean messagesBean = new MessagesBean();

    private Prototype prototype;
    private TypeProxy type;

    public LoginBean getLogin() {
        return login;
    }
    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public ViewTypeBean() {

    }

    @PostConstruct
    public void TypeBean() {
        type = navigationBean.getTypeProxy();
        UserInfo userInfo = login.getUserInfo();
        try {
            prototype = service.getPrototypeById(userInfo, type);
        } catch (CmisConnectException e) {
            messagesBean.addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(),"");
        }
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public Collection<PropertyDefinition<?>> getPropertyDefinitions() {
        return prototype.getPropertyDefinitions().values();
    }

    public CmisService getService() {
        return service;
    }

    public void setService(CmisService service) {
        this.service = service;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public MessagesBean getMessagesBean() {
        return messagesBean;
    }

    public void setMessagesBean(MessagesBean messagesBean) {
        this.messagesBean = messagesBean;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    public TypeProxy getType() {
        return type;
    }

    public void setType(TypeProxy type) {
        this.type = type;
    }
}

