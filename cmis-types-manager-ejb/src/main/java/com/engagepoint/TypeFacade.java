package com.engagepoint;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qnex.
 */
@Stateless
@LocalBean
public class TypeFacade {
    private final SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    public List<Tree<ObjectType>> getRootFolders(final LoginInfo loginInfo) {
        List<Tree<ObjectType>> trees = new ArrayList<Tree<ObjectType>>();
        Session session = getSession(loginInfo);
        return trees;
    }

    private Session getSession(final LoginInfo loginInfo) {
        Map<String, String> parameters = new HashMap<String, String>() {
            {
                put(SessionParameter.USER, loginInfo.getUserName());
                put(SessionParameter.USER, loginInfo.getUserName());
                put(SessionParameter.PASSWORD, loginInfo.getPassword());
                put(SessionParameter.ATOMPUB_URL, loginInfo.getUrl());
                put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                put(SessionParameter.REPOSITORY_ID, "test");
            }
        };
        return sessionFactory.createSession(parameters);
    }

}
