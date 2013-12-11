package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.*;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
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
    private TypeDefinition typeDefinition;
    private TypeProxy typeProxy;
    private List<String> cardinalityValues;
    private List<String> propertyTypeValues;
    private List<String> updatabilityValues;

    @PostConstruct
    public void init(){
        typeProxy = navigationBean.getTypeProxy();
        UserInfo userInfo = login.getUserInfo();
        try {
            typeDefinition = service.getTypeDefinition(userInfo, typeProxy);
            type.setCreatable(typeDefinition.isCreatable());
            type.setFileable(typeDefinition.isFileable());
            type.setQueryable(typeDefinition.isQueryable());
            type.setIncludedInSupertypeQuery(typeDefinition.isIncludedInSupertypeQuery());
            type.setFulltextIndexed(typeDefinition.isFulltextIndexed());
            type.setControllableAcl(typeDefinition.isControllableAcl());
            type.setControllablePolicy(typeDefinition.isControllablePolicy());
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            log.error("Unable to initialise type view", e);
        }
    }

    public CreateBean() {
        typeProperties = new ArrayList<TypeProperty>();
        type = new Type();
        setValuesToLists();

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
            Message.printError(e.getMessage());
            log.error("Unable to create type", e);
        } catch (CmisCreateException e) {
            Message.printError(e.getMessage());
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

    public List<String> getUpdatabilityValues() {
        return updatabilityValues;
    }

    public List<String> getCardinalitylityValues() {
        return cardinalityValues;
    }

    public List<String> getPropertyTypeValuesValues() {
        return  propertyTypeValues;
    }

    private void setValuesToLists() {
        cardinalityValues = getValuesForSelectOneMenu(Cardinality.values());
        updatabilityValues = getValuesForSelectOneMenu(Updatability.values());
        propertyTypeValues = getValuesForSelectOneMenu(PropertyType.values());
    }

    private List<String> getValuesForSelectOneMenu(Enum[] values) {
        List<String> list = new ArrayList<String>();
        for (Enum value : values) {
            list.add(value.name());
        }
        return list;
    }
}
