package com.engagepoint.bean;

import com.engagepoint.ejb.Service;
import com.engagepoint.exception.AppException;
import com.engagepoint.pojo.PropertyDefinitionImpl;
import com.engagepoint.pojo.Type;
import com.engagepoint.pojo.UserInfo;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.engagepoint.constant.MessageConstants.*;
import static com.engagepoint.constant.NameConstants.CMIS_SECONDARY;
import static com.engagepoint.constant.NavigationConstants.TO_CURRENT_PAGE;
import static com.engagepoint.constant.NavigationConstants.TO_MAIN_PAGE;

/**
 * User: AlexDenisenko
 * Date: 11.12.13
 * Time: 12:03
 */

@ManagedBean
@ViewScoped
public class CreateTypeBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTypeBean.class);
    @EJB
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{selectedTypeHolderBean}")
    private SelectedTypeHolderBean selectedTypeHolder;
    private Type newType;
    private List<PropertyDefinitionImpl> typeProperties;
    private Type selectedType;
    private List<String> cardinalityValues;
    private List<String> propertyTypeValues;
    private List<String> updatabilityValues;
    private PropertyDefinitionImpl newTypeProperty;
    private PropertyDefinitionImpl selectedTypeProperty;
    private List<PropertyDefinitionImpl> selectedTypeProperties;
    private boolean updateBtnDisabled;
    private boolean deleteBtnDisabled;
    private TypeDefinition typeDefinition;

    @PostConstruct
    public void init() {
        newTypeProperty = new PropertyDefinitionImpl();
        newTypeProperty.setParent(false);
        selectedTypeProperties = new ArrayList<PropertyDefinitionImpl>();
        updateBtnDisabled = true;
        deleteBtnDisabled = true;
        newType = new Type();
        setValuesToLists();
        selectedType = selectedTypeHolder.getType();
        selectedTypeProperty = new PropertyDefinitionImpl();
        UserInfo userInfo = login.getUserInfo();
        try {
            typeDefinition = service.findTypeById(login.getClientSession().getSession(), selectedType.getId());
        } catch (AppException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_INIT_TYPE_VIEW, e);
        }
        typeProperties = convert(getPropertyDefinitions());
        setAttributes(userInfo);
        if (isSecondary()) {
            selectedType.setId(CMIS_SECONDARY);
            newType.setCreatable(false);
            newType.setFileable(false);
            newType.setControllableAcl(false);
            newType.setControllablePolicy(false);
        }
    }

    public List<PropertyDefinition> getPropertyDefinitions() {
        Collection<PropertyDefinition<?>> values = typeDefinition.getPropertyDefinitions().values();
        return new ArrayList<PropertyDefinition>(values);
    }
//  TODO redividing
    public void addNewMetaData() {
        updateBtnDisabled = true;
        deleteBtnDisabled = true;
        List<String> propertyIdList = new ArrayList<String>();
        for (PropertyDefinitionImpl propertyDefinition : typeProperties) {
            propertyIdList.add(propertyDefinition.getId());
        }
        if (!propertyIdList.contains(newTypeProperty.getId())) {
            typeProperties.add(0,newTypeProperty);
            newTypeProperty = new PropertyDefinitionImpl();
            newTypeProperty.setParent(false);
        } else {
            MessageUtils.printError(ID_ALREADY_EXISTS);
        }
    }

    public void updateSelectedMetaData() {
        LOGGER.info(selectedTypeProperty.toString());
    }

    public void deleteMetaData() {
        for (PropertyDefinitionImpl property : selectedTypeProperties) {
            typeProperties.remove(property);
        }
        selectedTypeProperties.clear();
        if (typeProperties.isEmpty()) {
            updateBtnDisabled = true;
            deleteBtnDisabled = true;
        } else if (selectedTypeProperties.size() == 1) {
            updateBtnDisabled = false;
            deleteBtnDisabled = true;
        }
    }

    public void onRowSelection() {
        //  TODO change the logic to retrieve the parameters and check when it call
        if (!selectedTypeProperties.isEmpty() && selectedTypeProperties.size() < 2) {
            for (PropertyDefinitionImpl property : getTypeProperties()) {
                property.setSelected(false);
            }
            if (!selectedTypeProperties.get(0).getIsParent()) {
                selectedTypeProperties.get(0).setSelected(true);
                selectedTypeProperty = selectedTypeProperties.get(0);
                updateBtnDisabled = false;
                deleteBtnDisabled = false;
            } else {
                deleteBtnDisabled = true;
                updateBtnDisabled = true;
            }
        } else {
            for (PropertyDefinitionImpl property : selectedTypeProperties) {
                if (!property.getIsParent()) {
                    property.setSelected(true);
                    deleteBtnDisabled = false;
                } else {
                    deleteBtnDisabled = true;
                }
            }
            updateBtnDisabled = true;
        }
       // deleteBtnDisabled = false;
    }

    public void onRowUnSelection() {
        for (PropertyDefinitionImpl property : getTypeProperties()) {
            if (!selectedTypeProperties.contains(property)) {
                property.setSelected(false);
            }
        }
        if (!selectedTypeProperties.isEmpty() && selectedTypeProperties.size() < 2) {
            updateBtnDisabled = false;
            selectedTypeProperty = selectedTypeProperties.get(0);
        }
    }

    public void onRowSelection(PropertyDefinitionImpl property) {
        if (selectedTypeProperties.contains(property)) {
            selectedTypeProperties.remove(property);
        } else {
            selectedTypeProperties.add(property);
        }
        if (!selectedTypeProperties.isEmpty() && selectedTypeProperties.size() < 2) {
            selectedTypeProperty = selectedTypeProperties.get(0);
            updateBtnDisabled = false;
        } else {
            selectedTypeProperty = null;
            updateBtnDisabled = true;
        }
        deleteBtnDisabled = false;
    }

    private void setAttributes(UserInfo usrInf) {
        try {
            TypeDefinition typeDefinition = service.findTypeById(login.getClientSession().getSession(), selectedType.getId());
            newType.setCreatable(typeDefinition.isCreatable());
            newType.setFileable(typeDefinition.isFileable());
            newType.setQueryable(typeDefinition.isQueryable());
            newType.setIncludedInSupertypeQuery(typeDefinition.isIncludedInSupertypeQuery());
            newType.setFulltextIndexed(typeDefinition.isFulltextIndexed());
            newType.setControllableAcl(typeDefinition.isControllableAcl());
            newType.setControllablePolicy(typeDefinition.isControllablePolicy());
        } catch (AppException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_INIT_TYPE_VIEW, e);
        }
    }

    public String createType() {
        try {
            newType.setBaseTypeId(selectedType.getBaseTypeId());
            newType.setParentTypeId(selectedType.getId());
            newType.setProperties(typeProperties);
            newType.setChildren(Collections.EMPTY_LIST);
            service.createType(login.getClientSession().getSession(), newType);
//            selectedTypeHolder.setType(newType);
            MessageUtils.printInfo(newType.getDisplayName() + TYPE_CREATED);
            return TO_MAIN_PAGE;
        } catch (AppException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_CREATE_TYPE, e);
        }
        return TO_CURRENT_PAGE;
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

    public boolean isSecondary() {
        return selectedType.getBaseTypeId().equals(CMIS_SECONDARY);
    }


    public Type getType() {
        return newType;
    }

    public void setType(Type type) {
        newType = type;
    }

    public String getBaseType() {
        return selectedType.getBaseTypeId();
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

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    public void deleteAction(PropertyDefinitionImpl property) {
        typeProperties.remove(property);
    }

    public List<PropertyDefinitionImpl> getTypeProperties() {
        return typeProperties;
    }

    public List<String> getUpdatabilityValues() {

        return updatabilityValues;
    }

    public List<String> getCardinalityValues() {
        return cardinalityValues;
    }

    public List<String> getPropertyTypeValuesValues() {
        return propertyTypeValues;
    }

    public PropertyDefinitionImpl getNewTypeProperty() {
        return newTypeProperty;
    }

    public void setNewTypeProperty(PropertyDefinitionImpl newTypeProperty) {
        this.newTypeProperty = newTypeProperty;
    }

    public PropertyDefinitionImpl getSelectedTypeProperty() {
        return selectedTypeProperty;
    }

    public void setSelectedTypeProperty(PropertyDefinitionImpl selectedTypeProperty) {
        this.selectedTypeProperty = selectedTypeProperty;
    }

    public List<PropertyDefinitionImpl> getSelectedTypeProperties() {
        return selectedTypeProperties;
    }

    public void setSelectedTypeProperties(List<PropertyDefinitionImpl> selectedTypeProperties) {
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

    public TypeDefinition getTypeDefinition() {
        return typeDefinition;
    }

    public void setTypeDefinition(TypeDefinition typeDefinition) {
        this.typeDefinition = typeDefinition;
    }

    private List<PropertyDefinitionImpl> convert(List<PropertyDefinition> definition) {
        List<PropertyDefinitionImpl> definitionList = new ArrayList<PropertyDefinitionImpl>();
        for (PropertyDefinition element : definition) {
            PropertyDefinitionImpl propertyDefinition = new PropertyDefinitionImpl();
            propertyDefinition.setId(element.getId());
            propertyDefinition.setDisplayName(element.getDisplayName());
            propertyDefinition.setLocalName(element.getLocalName());
            propertyDefinition.setLocalNamespace(element.getLocalNamespace());
            propertyDefinition.setCardinality(element.getCardinality());
            propertyDefinition.setDescription(element.getDescription());
            propertyDefinition.setPropertyType(element.getPropertyType());
            propertyDefinition.setQueryName(element.getQueryName());
            propertyDefinition.setUpdatability(element.getUpdatability());
            propertyDefinition.setIsInherited(element.isInherited());
            propertyDefinition.setIsOrderable(element.isOrderable());
            propertyDefinition.setIsRequired(element.isRequired());
            propertyDefinition.setIsQueryable(element.isQueryable());
            propertyDefinition.setParent(true);
            definitionList.add(propertyDefinition);
        }
        return definitionList;
    }
}
