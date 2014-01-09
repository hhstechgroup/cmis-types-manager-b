package com.engagepoint.bean;

import com.engagepoint.ejb.Service;
import com.engagepoint.exception.AppException;
import com.engagepoint.pojo.Type;
import com.engagepoint.util.MessageUtils;
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

import static com.engagepoint.constant.MessageConstants.UNABLE_INIT_TYPE_VIEW;
import static com.engagepoint.constant.NameConstants.*;

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
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TypeDefinition type;

    @ManagedProperty(value = "#{selectedTypeHolderBean}")
    private SelectedTypeHolderBean selectedTypeHolder;

    @PostConstruct
    public void init() {
        try {
            Type selectedType = selectedTypeHolder.getType();
            type = service.findTypeById(login.getClientSession().getSession(), selectedType.getId());
        } catch (AppException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_INIT_TYPE_VIEW, e);
        }
    }

    public String mutabilityToString(){
        StringBuilder builder = new StringBuilder();
        if(type.getTypeMutability().canDelete()){
            builder.append(MUTABILITY_DELETE_DISPLAY_NAME);
        }
        if ( type.getTypeMutability().canCreate()){
            builder.append(MUTABILITY_CREATE_DISPLAY_NAME);
        }
        if ( type.getTypeMutability().canUpdate()){
            builder.append(MUTABILITY_UPDATE_DISPLAY_NAME);
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
        Collection<PropertyDefinition<?>> values = type.getPropertyDefinitions().values();
        return new ArrayList<PropertyDefinition>(values);
    }

    public TypeDefinition getType() {
        return type;
    }

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }
}

