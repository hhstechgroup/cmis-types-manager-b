package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.Prototype;
import com.engagepoint.services.UserInfo;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private Prototype prototype;
    private final String typeId;

    public LoginBean getLogin() {
        return login;
    }
    public void setLogin(LoginBean login) {
        this.login = login;
    }
    public ModifyTypeBean() {
        typeId = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedType");
        prototype = new Prototype();
    }
    public String createType() {
        UserInfo userInfo = login.getUserInfo();
        try {
            prototype.setParentTypeId(typeId);
            prototype.setBaseTypeId(typeId);
            service.createType(userInfo, prototype);
            return "index.xhtml?faces-redirect=true";
        } catch (CmisConnectException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage("exceptions", message);
            return "";
        }
    }

    public Prototype getPrototype() {
        return prototype;
    }

    public void setPrototype(Prototype prototype) {
        this.prototype = prototype;
    }

    public String getTypeId() {
        return typeId;
    }
}
