package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
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
    private TypeProxy selectedType;
    private String stringMutability;

    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;




    @PostConstruct
    public void init() {
        try {
            selectedType = navigationBean.getTypeProxy();
            UserInfo userInfo = login.getUserInfo();
            typeDefinition = service.getTypeDefinition(userInfo, selectedType);
            mutabilityToString();
        } catch (CmisException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to initialise type view", e);
        }
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

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }
    public String mutabilityToString(){
        String delete;
        String upData;
        String create;
        if(typeDefinition.getTypeMutability().canDelete() == true){
            delete = "Delete";
        }else {
            delete = "";
        }
        if ( typeDefinition.getTypeMutability().canCreate() ==true){
            create ="Create";
        }else {
            create="";
        }
        if ( typeDefinition.getTypeMutability().canUpdate() ==true){
            upData ="Update";
        }else {
            upData="";
        }
        stringMutability =  create +" "+ upData +" "+ delete ;
        return stringMutability;
    }

    public String getStringMutability() {
        return stringMutability;
    }

    public void setStringMutability(String stringMutability) {
        this.stringMutability = stringMutability;
    }
}

