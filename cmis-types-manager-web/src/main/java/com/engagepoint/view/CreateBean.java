package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateBean implements Serializable {
    private Logger log = LoggerFactory.getLogger(ModifyTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private Type type;
    private List<TypeProperty> typeProperties;
    private TypeProxy typeProxy;

    public CreateBean() {
        getParametersFromFlash();
        typeProperties = new ArrayList<TypeProperty>();
        type = new Type();
    }


    public String addAction() {
        TypeProperty property = new TypeProperty();
        property.setDisplayName("");
        property.setLocalName("");
        property.setQueryName("");
        property.setId("");
        property.setCardinality("");
        property.setUpdatability("");
        property.setPropertyType("");
        typeProperties.add(property);
        return null;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getBaseType() {
        return typeProxy.getBaseType();
    }

    public String getParentType() {
        return typeProxy.getId();
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


    public String createType() {
        try {
            UserInfo userInfo = login.getUserInfo();
            type.setBaseTypeId(typeProxy.getBaseType());
            type.setParentTypeId(typeProxy.getId());
            type.setProperties(typeProperties);
            service.createType(userInfo, type);
            Message.printInfo(type.getDisplayName() + " type created!");
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


    public void deleteAction(TypeProperty property) {
        typeProperties.remove(property);
    }

    public List<TypeProperty> getTypeProperties() {
        return typeProperties;
    }

    private void getParametersFromFlash() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        typeProxy = (TypeProxy) externalContext.getFlash().get("selectedType");
    }
}
