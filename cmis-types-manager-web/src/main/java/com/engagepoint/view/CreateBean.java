package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.*;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private Type type;
    private List<TypeProperty> typeProperties;


    public CreateBean() {
        typeProperties = new ArrayList<TypeProperty>();
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
            String name = "MyMy";
            Type type = new Type();
            type.setDescription(name);
            type.setId(name);
            type.setDisplayName(name);
            type.setLocalName(name);
            type.setBaseTypeId("cmis:folder");
            type.setParentTypeId("cmis:folder");
            type.setProperties(typeProperties);
            service.createType(userInfo, type);
        } catch (CmisConnectException e) {
            Message.printInfo(e.getMessage());
        } catch (CmisCreateException e) {
            Message.printInfo(e.getMessage());
        }
        return "";
    }


    public void deleteAction(TypeProperty property) {
        typeProperties.remove(property);
    }

    public List<TypeProperty> getTypeProperties() {
        return typeProperties;
    }
}
