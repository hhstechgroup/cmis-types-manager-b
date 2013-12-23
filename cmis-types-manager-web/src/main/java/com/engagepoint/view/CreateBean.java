package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
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
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

@ManagedBean
@ViewScoped
public class CreateBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;
    private Type newType;
    private List<TypeProperty> typeProperties;

    private List<String> cardinalityValues;
    private List<String> propertyTypeValues;
    private List<String> updatabilityValues;
    private String secondary = "cmis:secondary";
    private TypeProperty newTypeProperty;
    private TypeProperty selectedTypeProperty;

    @PostConstruct
    public void init() {
        newTypeProperty = new TypeProperty();
        typeProperties = new ArrayList<TypeProperty>();
        newType = new Type();
        setValuesToLists();
        selectedTypeProperty = new TypeProperty();
        UserInfo userInfo = login.getUserInfo();
        setAttributes(userInfo);

        if (isHide()) {
            navigationBean.getTypeProxy().setId(secondary);
            newType.setCreatable(false);
            newType.setFileable(false);
            newType.setControllableAcl(false);
            newType.setControllablePolicy(false);
        }
    }

    public void addNewMetaData() {
        getTypeProperties().add(newTypeProperty);
        newTypeProperty = new TypeProperty();
    }

    public void updateSelectedMetaData(){
        System.out.println(getTypeProperties());
    }

    public void deleteMetaData() {
        typeProperties.remove(selectedTypeProperty);
        Message.printInfo("Deleted " + selectedTypeProperty.getId());
        selectedTypeProperty = new TypeProperty();
    }

    public void deleteMetaData(TypeProperty property) {
        getTypeProperties().remove(property);
    }

    private void setAttributes(UserInfo usrInf) {
        try {
            TypeDefinition typeDefinition = service.getTypeDefinition(usrInf, navigationBean.getTypeProxy());
            newType.setCreatable(typeDefinition.isCreatable());
            newType.setFileable(typeDefinition.isFileable());
            newType.setQueryable(typeDefinition.isQueryable());
            newType.setIncludedInSupertypeQuery(typeDefinition.isIncludedInSupertypeQuery());
            newType.setFulltextIndexed(typeDefinition.isFulltextIndexed());
            newType.setControllableAcl(typeDefinition.isControllableAcl());
            newType.setControllablePolicy(typeDefinition.isControllablePolicy());
        } catch (CmisException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to initialise type view", e);
        }
    }

    public Type getType() {
        return newType;
    }

    public void setType(Type type) {
        newType = type;
    }

    public String getBaseType() {
        return navigationBean.getTypeProxy().getBaseType();
    }

    public String getParentType() {
        return navigationBean.getTypeProxy().getId();
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
            newType.setBaseTypeId(navigationBean.getTypeProxy().getBaseType());
            newType.setParentTypeId(navigationBean.getTypeProxy().getId());
            newType.setProperties(typeProperties);
            service.createType(userInfo, newType);
            Message.printInfo(newType.getDisplayName() + " type created!");
            return Constants.Navigation.TO_MAIN_PAGE;
        } catch (CmisException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to create type", e);
        }
        return Constants.Navigation.TO_CURRENT_PAGE;
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
        return propertyTypeValues;
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

    public boolean isHide() {
        return navigationBean.getTypeProxy().getBaseType().equals(secondary);
    }

    public TypeProperty getNewTypeProperty() {
        return newTypeProperty;
    }

    public void setNewTypeProperty(TypeProperty newTypeProperty) {
        this.newTypeProperty = newTypeProperty;
    }

    public TypeProperty getSelectedTypeProperty() {
        return selectedTypeProperty;
    }

    public void setSelectedTypeProperty(TypeProperty selectedTypeProperty) {
        this.selectedTypeProperty = selectedTypeProperty;
    }
}
