package com.engagepoint.view;

import com.engagepoint.utils.MessageUtils;
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
    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;
    private Type newType;
    private List<TypeProperty> typeProperties;
    private TypeProxy selectedType;
    private List<String> cardinalityValues;
    private List<String> propertyTypeValues;
    private List<String> updatabilityValues;
    private TypeProperty newTypeProperty;
    private TypeProperty selectedTypeProperty;
    private List<TypeProperty> selectedTypeProperties;
    private boolean updateBtnDisabled;
    private boolean deleteBtnDisabled;


    @PostConstruct
    public void init() {
        newTypeProperty = new TypeProperty();
        typeProperties = new ArrayList<TypeProperty>();
        selectedTypeProperties = new ArrayList<TypeProperty>();
        updateBtnDisabled = true;
        deleteBtnDisabled = true;
        newType = new Type();
        setValuesToLists();
        selectedType = sessionStateBean.getTypeProxy();
        selectedTypeProperty = new TypeProperty();
        UserInfo userInfo = login.getUserInfo();
        setAttributes(userInfo);
        if (isSecondary()){
            selectedType.setId(Constants.TypesManager.CMIS_SECONDARY);
            newType.setCreatable(false);
            newType.setFileable(false);
            newType.setControllableAcl(false);
            newType.setControllablePolicy(false);
        }
    }

    public void addNewMetaData() {
        updateBtnDisabled = false;
        deleteBtnDisabled = false;
        getTypeProperties().add(newTypeProperty);
        newTypeProperty = new TypeProperty();
    }

    public void updateSelectedMetaData(){
        System.out.println(selectedTypeProperty.toString());
    }

    public void deleteMetaData() {
        for (TypeProperty property : selectedTypeProperties) {
            typeProperties.remove(property);
        }
        selectedTypeProperties.clear();
        if (typeProperties.size() == 0) {
            updateBtnDisabled = true;
            deleteBtnDisabled = true;
        } else if (selectedTypeProperties.size() == 1) {
            updateBtnDisabled = false;
            deleteBtnDisabled = true;
        }
    }

    public void onRowSelection(){
        if (selectedTypeProperties.size() == 1) {
            selectedTypeProperty = selectedTypeProperties.get(0);
            updateBtnDisabled = false;
        } else {
            selectedTypeProperty = null;
            updateBtnDisabled = true;
        }
        deleteBtnDisabled = false;
        StringBuilder builder = new StringBuilder();
        for (TypeProperty property : selectedTypeProperties){
            builder.append(property.getId());
            builder.append("; ");
        }
        MessageUtils.printInfo("Selected : " + builder.toString());
    }

    private void setAttributes(UserInfo usrInf) {
        try {
            TypeDefinition typeDefinition = service.getTypeDefinition(usrInf, selectedType);
            newType.setCreatable(typeDefinition.isCreatable());
            newType.setFileable(typeDefinition.isFileable());
            newType.setQueryable(typeDefinition.isQueryable());
            newType.setIncludedInSupertypeQuery(typeDefinition.isIncludedInSupertypeQuery());
            newType.setFulltextIndexed(typeDefinition.isFulltextIndexed());
            newType.setControllableAcl(typeDefinition.isControllableAcl());
            newType.setControllablePolicy(typeDefinition.isControllablePolicy());
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_TO_INIT_VIEW, e);
        }
    }

    public String createType() {
        try {
            UserInfo userInfo = login.getUserInfo();
            newType.setBaseTypeId(selectedType.getBaseType());
            newType.setParentTypeId(selectedType.getId());
            newType.setProperties(typeProperties);
            service.createType(userInfo, newType);
            MessageUtils.printInfo(newType.getDisplayName() + Constants.Messages.TYPE_CREATED);
            return Constants.Navigation.TO_MAIN_PAGE;
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_CREATE_TYPE, e);
        }
        return Constants.Navigation.TO_CURRENT_PAGE;
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
    public boolean isSecondary(){
        return selectedType.getBaseType().equals(Constants.TypesManager.CMIS_SECONDARY);
    }


    public Type getType() {
        return newType;
    }

    public void setType(Type type) {
        newType = type;
    }

    public String getBaseType() {
        return selectedType.getBaseType();
    }

    public String getParentType() {
        return selectedType.getId();
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
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

    public List<String> getCardinalitylityValues()
    {
        return cardinalityValues;
    }

    public List<String> getPropertyTypeValuesValues() {
        return  propertyTypeValues;
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

    public List<TypeProperty> getSelectedTypeProperties() {
        return selectedTypeProperties;
    }

    public void setSelectedTypeProperties(List<TypeProperty> selectedTypeProperties) {
        this.selectedTypeProperties = selectedTypeProperties;
    }

    public boolean isUpdateBtnDisabled() {
        return updateBtnDisabled;
    }

    public void setUpdateBtnDisabled(boolean updateBtnDisabled) {
        this.updateBtnDisabled = updateBtnDisabled;
    }

    public boolean isDeleteBtnDisabled() {
        return deleteBtnDisabled;
    }

    public void setDeleteBtnDisabled(boolean deleteBtnDisabled) {
        this.deleteBtnDisabled = deleteBtnDisabled;
    }
}
