package com.engagepoint.view;

import com.engagepoint.utils.MessageUtils;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
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
import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 01.12.13
 * Time: 11:46
 */
@ManagedBean
@ViewScoped
public class ViewTypeBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TypeDefinition typeDefinition;

    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;

    @PostConstruct
    public void init() {
        try {
            TypeProxy selectedType = sessionStateBean.getTypeProxy();
            UserInfo userInfo = login.getUserInfo();
            typeDefinition = service.getTypeDefinition(userInfo, selectedType);
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_INIT_TYPE_VIEW, e);
        }
    }

    public String mutabilityToString(){
        StringBuilder builder = new StringBuilder();
        if(typeDefinition.getTypeMutability().canDelete()){
            builder.append(Constants.TypesManager.MUTABILITY_DELETE_DISPLAY_NAME);
        }
        if ( typeDefinition.getTypeMutability().canCreate()){
            builder.append(Constants.TypesManager.MUTABILITY_CREATE_DISPLAY_NAME);
        }
        if ( typeDefinition.getTypeMutability().canUpdate()){
            builder.append(Constants.TypesManager.MUTABILITY_UPDATE_DISPLAY_NAME);
        }
        return builder.toString();
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public List<PropertyDefinition> getPropertyDefinitions() {
        Collection<PropertyDefinition<?>> values = typeDefinition.getPropertyDefinitions().values();
        return new ArrayList<PropertyDefinition>(values);
    }

    public TypeDefinition getTypeDefinition() {
        return typeDefinition;
    }

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
    }
}

