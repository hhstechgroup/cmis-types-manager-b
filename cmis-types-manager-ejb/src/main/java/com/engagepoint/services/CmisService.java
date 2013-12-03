package com.engagepoint.services;

import com.engagepoint.exceptions.CmisConnectException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: alexdenisenko
 * Date: 18/15/13
 * Time: 1:34 PM
 */
@Stateless
@LocalBean
public class CmisService {
    @EJB
    private CmisConnection connection;

    public List<CmisType> getTreeTypes(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, true);
       // session.getBinding().close();
        return getCmisTypes(descendants);
    }
       //getNamesOfRootFolders not using:
    public List<String> getNamesOfRootFolders(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<String> folders = new ArrayList<String>();
        Folder root = session.getRootFolder();
        session.getBinding().close();
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            folders.add(child.getName());
        }
        return folders;
    }

    public boolean isUserExist(final UserInfo userInfo) throws CmisConnectException {
        return (getSession(userInfo) != null);
    }

    //getRepositoriesNames not using:
    public List<String> getRepositoriesNames(final UserInfo userInfo) throws CmisConnectException {
        List<String> repositoriesNames = new ArrayList<String>();
        for (Repository repository : getRepositories(userInfo)) {
            repositoriesNames.add(repository.getName());
        }

        return repositoriesNames;
    }

    public String getDefaultRepositoryIdName(final UserInfo userInfo) throws CmisConnectException {
        int firstRepositoryId = 0;
        String defaultRepositoryId = "";
        List<Repository> repositories = getRepositories(userInfo);
        if (!repositories.isEmpty()) {
            defaultRepositoryId = repositories.get(firstRepositoryId).getId();
        }
        return defaultRepositoryId;
    }

    private List<Repository> getRepositories(final UserInfo userInfo) throws CmisConnectException {
        Map<String, String> parameters = getParameters(userInfo);
        //TODO My. cleen
        System.out.println("!!!!!!!!!!!!!!!!!!Before" + parameters.size());
        parameters.remove(SessionParameter.REPOSITORY_ID);
        System.out.println("!!!!!!!!!!!!!!!!!!After" + parameters.size());
        List<Repository> repositories;
        try {
            repositories = connection.getSessionFactory().getRepositories(parameters);
        } catch (CmisBaseException e) {
            throw new CmisConnectException(e.getMessage());
        }
        return repositories;
    }

    private Session getSession(final UserInfo userInfo) throws CmisConnectException {
        Map<String, String> parameters = getParameters(userInfo);
        Session session;
        try {
            session = connection.getSessionFactory().createSession(parameters);
        } catch (CmisBaseException e) {
            throw new CmisConnectException(e.getMessage());
        }
        return session;
    }

    private List<CmisType> getCmisTypes(List<Tree<ObjectType>> treeList) {
        List<CmisType> cmisTypeList = new ArrayList<CmisType>();
        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getCmisType(tree.getItem()));
        }
        return cmisTypeList;
    }

    private CmisType getCmisType(ObjectType objectType) {
        CmisType cmisType = new CmisType();
        cmisType.setName(objectType.getDisplayName());
        cmisType.setId(objectType.getId());
        cmisType.setCreatable(objectType.isCreatable());
        cmisType.setFileable(objectType.isFileable());
        List<CmisType> children = new ArrayList<CmisType>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getCmisType(child));
        }
        cmisType.setChildren(children);
        return cmisType;
    }

    private Map<String, String> getParameters(final UserInfo userInfo) {
        return new HashMap<String, String>() {
            {
                put(SessionParameter.USER, userInfo.getUsername());
                put(SessionParameter.PASSWORD, userInfo.getPassword());
                put(SessionParameter.ATOMPUB_URL, userInfo.getUrl());
                put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                put(SessionParameter.REPOSITORY_ID, userInfo.getRepositoryId());
            }
        };
    }

}
