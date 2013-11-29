package com.engagepoint;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

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
public class CmisService {
    public List<String> getRootFolders(final LoginInfo loginInfo)throws CmisConnectException{
        Session session = getSession(loginInfo);
        List<String> folders = new ArrayList<String>();
        Folder root = session.getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            folders.add(child.getName());
        }
        return folders;
    }

    public List<Tree<ObjectType>> getTreeTypes(final LoginInfo loginInfo)throws CmisConnectException {
        Session session = getSession(loginInfo);
        List<Tree<ObjectType>> descendants = null;
        if (session != null) {
            descendants = session.getTypeDescendants(null, -1, true);
        }
        return descendants;
    }

    public boolean isValidUser(final LoginInfo loginInfo) throws CmisConnectException {
        return (getSession(loginInfo) != null);
    }

    private Session getSession(final LoginInfo loginInfo) throws CmisConnectException {
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<String, String>() {
            {
                put(SessionParameter.USER, loginInfo.getUsername());
                put(SessionParameter.PASSWORD, loginInfo.getPassword());
                put(SessionParameter.ATOMPUB_URL, loginInfo.getUrl());
                put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                put(SessionParameter.REPOSITORY_ID, "A1");
            }
        };
        Session session;
        try {
            session = sessionFactory.createSession(parameters);
        } catch (RuntimeException e) {
            throw new CmisConnectException(e.getMessage());
        }
        return session;
    }

}
