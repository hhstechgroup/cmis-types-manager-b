package com.engagepoint.services;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisTypeDeleteException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
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
 * User: AlexDenisenko
 * Date: 18/15/13
 * Time: 1:34 PM
 */
@Stateless
@LocalBean
public class CmisService {
    @EJB
    private CmisConnection connection;

    public List<TypeProxy> getTypeInfo(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, true);
        return getTypeProxies(descendants);
    }

    public Prototype getPrototypeById(final UserInfo userInfo, TypeProxy type) throws CmisConnectException {
        Session session = getSession(userInfo);
        ObjectType objectType = session.getTypeDefinition(type.getId());
        return getPrototype(objectType);
    }

    public List<String> getNamesOfRootFolders(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<String> folders = new ArrayList<String>();
        Folder root = session.getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            folders.add(child.getName());
        }
        return folders;
    }

    public void createType(final UserInfo userInfo, Prototype prototype) throws CmisConnectException {
        Session session = getSession(userInfo);
        CmisTypeBuilder builder = new CmisTypeBuilder();
        builder.setPrototype(prototype);
        builder.buildType();
        session.createType(builder.getType());
    }

    public void deleteType(final UserInfo userInfo, TypeProxy proxy) throws CmisConnectException, CmisTypeDeleteException {
        Session session = getSession(userInfo);
        try {
            ObjectType type = session.getTypeDefinition(proxy.getId());
            TypeMutability typeMutability = type.getTypeMutability();
            if (typeMutability != null && Boolean.TRUE.equals(typeMutability.canDelete())) {
                session.deleteType(type.getId());
            } else {
                throw new CmisTypeDeleteException("Type is not deleted") ;
            }
        } catch (Exception e) {
            throw new CmisTypeDeleteException(e.getMessage());
        }

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

    public boolean isUserExist(final UserInfo userInfo) throws CmisConnectException {
        return getSession(userInfo) != null;
    }

    //TODO    @deprecated
    public List<Prototype> getTreeTypes(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, true);
        return getPrototypes(descendants);
    }

    //TODO Think about get repositories
    public List<Repository> getRepositories(final UserInfo userInfo) throws CmisConnectException {
        Map<String, String> parameters = getParameters(userInfo);
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

    //TODO    @deprecated
    private List<Prototype> getPrototypes(List<Tree<ObjectType>> treeList) {
        List<Prototype> cmisTypeList = new ArrayList<Prototype>();
        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getPrototype(tree.getItem()));
        }
        return cmisTypeList;
    }

    private Prototype getPrototype(ObjectType objectType) {
        Prototype prototype = new Prototype();
        prototype.setId(objectType.getId());
        prototype.setBaseTypeId(objectType.getBaseTypeId().value());
        prototype.setParentTypeId(objectType.getParentTypeId());
        prototype.setLocalName(objectType.getLocalName());
        prototype.setDisplayName(objectType.getDisplayName());
        prototype.setQueryName(objectType.getQueryName());
        prototype.setDescription(objectType.getDescription());
        prototype.setLocalNamespace(objectType.getLocalNamespace());
        prototype.setCreatable(objectType.isCreatable());
        prototype.setQueryable(objectType.isQueryable());
        prototype.setFileable(objectType.isFileable());
        prototype.setControllableAcl(objectType.isControllableAcl());
        prototype.setControllablePolicy(objectType.isControllablePolicy());
        prototype.setFulltextIndexed(objectType.isFulltextIndexed());
        prototype.setIncludedInSupertypeQuery(objectType.isIncludedInSupertypeQuery());
        prototype.setPropertyDefinitions(objectType.getPropertyDefinitions());
        return prototype;
    }

    private List<TypeProxy> getTypeProxies(List<Tree<ObjectType>> treeList) {
        List<TypeProxy> cmisTypeList = new ArrayList<TypeProxy>();
        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getTypeProxy(tree.getItem()));
        }
        return cmisTypeList;
    }

    private TypeProxy getTypeProxy(ObjectType objectType) {
        TypeProxy typeProxy = new TypeProxy();
        typeProxy.setId(objectType.getId());
        typeProxy.setDisplayName(objectType.getDisplayName());
        typeProxy.setBaseType(objectType.getBaseTypeId().value());
        List<TypeProxy> children = new ArrayList<TypeProxy>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeProxy(child));
        }
        typeProxy.setChildren(children);
        return typeProxy;
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
