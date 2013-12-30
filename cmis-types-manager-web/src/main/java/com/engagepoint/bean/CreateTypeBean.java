package com.engagepoint.bean;

import com.engagepoint.constant.Constants;
import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.service.*;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class CreateTypeBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTypeBean.class);
    @EJB
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;
    private TypeDefinitionImpl newType;
    private List<PropertyDefinitionImpl> propertyDefinitions;
    private TypeProxy selectedType;
    private List<String> cardinalityValues;
    private List<String> propertyTypeValues;
    private List<String> updatabilityValues;
    private PropertyDefinitionImpl newTypeProperty;
    private TypeProperty selectedTypeProperty;
    private List<TypeProperty> selectedTypeProperties;
    private TypeDelegate typeDelegate;

    @PostConstruct
    public void init() {
        newType = new TypeDefinitionImpl();
        selectedType = sessionStateBean.getType();
        typeDelegate = new TypeDelegate(newType);

    }

    public TypeDelegate getTypeDelegate() {
        return typeDelegate;
    }

    public void setTypeDelegate(TypeDelegate typeDelegate) {
        this.typeDelegate = typeDelegate;
    }

    public String createType() {
        try {
            UserInfo userInfo = login.getUserInfo();
            newType.setBaseTypeId(BaseTypeId.fromValue(selectedType.getBaseType()));
            newType.setParentTypeId(selectedType.getId());
            service.createType(userInfo, newType);
            MessageUtils.printInfo(newType.getDisplayName() + Constants.Messages.TYPE_CREATED);
            return Constants.Navigation.TO_MAIN_PAGE;
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_CREATE_TYPE, e);
        }
        return Constants.Navigation.TO_CURRENT_PAGE;
    }

    public TypeDefinitionImpl getTypeDefinition() {
        return newType;
    }

    public void setTypeDefinition(TypeDefinitionImpl type) {
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

    public final class TypeDelegate {
        private TypeDefinitionImpl typeDefinition;

        private TypeDelegate(TypeDefinitionImpl typeDefinition) {
            this.typeDefinition = typeDefinition;
        }

        public boolean isCreatable() {
            return is(typeDefinition.isCreatable());
        }

        public void setCreatable(boolean creatable) {
            typeDefinition.setIsCreatable(creatable);
        }

        public boolean isFileable() {
            return is(typeDefinition.isFileable());
        }

        public void setFileable(boolean fileable) {
            typeDefinition.setIsFileable(fileable);
        }

        public boolean isQueryable() {
            return is(typeDefinition.isQueryable());
        }

        public void setQueryable(boolean queryable) {
            typeDefinition.setIsQueryable(queryable);
        }

        public boolean isFulltextIndexed() {
            return is(typeDefinition.isFulltextIndexed());
        }

        public void setFulltextIndexed(boolean fulltextIndexed) {
            typeDefinition.setIsFulltextIndexed(fulltextIndexed);
        }

        public boolean isIncludedInSupertypeQuery() {
            return is(typeDefinition.isIncludedInSupertypeQuery());
        }

        public void setIncludedInSupertypeQuery(boolean includedInSupertypeQuery) {
            typeDefinition.setIsIncludedInSupertypeQuery(includedInSupertypeQuery);
        }

        public boolean isControllablePolicy() {
            return is(typeDefinition.isControllablePolicy());
        }

        public void setControllablePolicy(boolean controllablePolicy) {
            typeDefinition.setIsControllablePolicy(controllablePolicy);
        }

        public boolean isControllableAcl() {
            return is(typeDefinition.isControllableAcl());
        }

        public void setControllableAcl(boolean controllableAcl) {
            typeDefinition.setIsControllableAcl(controllableAcl);
        }

        public boolean is(Boolean b) {
            return b != null && b;
        }

    }
}
