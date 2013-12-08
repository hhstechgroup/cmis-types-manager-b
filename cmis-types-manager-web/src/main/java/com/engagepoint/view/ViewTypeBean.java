package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private TypeDefinition definition;
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
            definition = service.getTypeDefinition(userInfo, type);
        } catch (CmisConnectException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }


    public List<PropertyDefinition> getPropertyDefinitions() {
        List<PropertyDefinition> test = new ArrayList<PropertyDefinition>(definition.getPropertyDefinitions().values());
        return new ArrayList<PropertyDefinition>(definition.getPropertyDefinitions().values());
    }

    public TypeDefinition getDefinition() {
        return definition;
    }

}

