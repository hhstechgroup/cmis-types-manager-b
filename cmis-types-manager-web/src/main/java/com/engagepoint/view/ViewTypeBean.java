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
    private Prototype prototype;
    private final TypeProxy type;

    public LoginBean getLogin() {
        return login;
    }
    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public ViewTypeBean() {
        type = (TypeProxy) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedType");
    }

    @PostConstruct
    public void TypeBean() {
        UserInfo userInfo = login.getUserInfo();
        try {
            prototype = service.getPrototypeById(userInfo, type);
        } catch (CmisConnectException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage("exceptions", message);
        }
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public Collection<PropertyDefinition<?>> getPropertyDefinitions() {
        return prototype.getPropertyDefinitions().values();
    }

}

