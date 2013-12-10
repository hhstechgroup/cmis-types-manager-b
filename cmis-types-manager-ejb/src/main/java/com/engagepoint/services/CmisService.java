package com.engagepoint.services;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.exceptions.CmisTypeDeleteException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.*;

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


    //  TODO add catch exception
    public TypeDefinition getTypeDefinition(final UserInfo userInfo, TypeProxy type) throws CmisConnectException {
        Session session = getSession(userInfo);
        return session.getTypeDefinition(type.getId());
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


    public void createType(final UserInfo userInfo, Type type) throws CmisConnectException, CmisCreateException {
        Session session = getSession(userInfo);
        TypeDefinition typeDefinition = getTypeDefinition(type);
        try {
            session.createType(typeDefinition);
        } catch (RuntimeException e) {
            throw new CmisCreateException(e.getMessage());
        }
    }

    private TypeDefinition getTypeDefinition(Type type) {
        TypeDefinitionImpl typeDef = new TypeDefinitionImpl();
        typeDef.setDescription(type.getDescription());
        typeDef.setId(type.getId());
        typeDef.setDisplayName(type.getDisplayName());
        typeDef.setLocalName(type.getLocalName());
        typeDef.setBaseTypeId(BaseTypeId.fromValue(type.getBaseTypeId()));
        typeDef.setParentTypeId(type.getParentTypeId());
        typeDef.setPropertyDefinitions(getPropertyDefinitionMap(type.getProperties()));
        return typeDef;
    }

    private Map<String, PropertyDefinition<?>> getPropertyDefinitionMap(List<TypeProperty> properties) {
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = new LinkedHashMap<String, PropertyDefinition<?>>();
        for (TypeProperty property : properties) {
            PropertyDefinitionImpl definition = new PropertyDefinitionImpl();
            definition.setId(property.getId());
            definition.setDisplayName(property.getDisplayName());
            definition.setLocalName(property.getLocalName());
            definition.setQueryName(property.getQueryName());
            definition.setCardinality(Cardinality.fromValue(property.getCardinality()));
            definition.setPropertyType(PropertyType.fromValue(property.getPropertyType()));
            definition.setIsRequired(property.getRequired());
            definition.setIsInherited(property.getInherited());
            definition.setUpdatability(Updatability.fromValue(property.getUpdatability()));
            definition.setIsOrderable(property.getOrderable());
            definition.setLocalNamespace(property.getLocalNamespace());
            propertyDefinitionMap.put(property.getId(), definition);
        }
        return propertyDefinitionMap;
    }

    public void importType(UserInfo userInfo, InputStream stream) throws CmisConnectException, XMLStreamException, CmisCreateException {
        Session session = getSession(userInfo);
        TypeDefinition typeDefinition = TypeUtils.readFromXML(stream);
        try {
            session.createType(typeDefinition);
        } catch (RuntimeException e) {
            throw new CmisCreateException(e.getMessage());
        }
    }

    public void deleteType(final UserInfo userInfo, TypeProxy proxy) throws CmisConnectException, CmisTypeDeleteException {
        Session session = getSession(userInfo);
        try {
            ObjectType type = session.getTypeDefinition(proxy.getId());
            TypeMutability typeMutability = type.getTypeMutability();
            if (typeMutability != null && Boolean.TRUE.equals(typeMutability.canDelete())) {
                session.deleteType(type.getId());
            } else {
                throw new CmisTypeDeleteException("Type is not deleted");
            }
        } catch (RuntimeException e) {
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


    private List<TypeProxy> getTypeProxies(List<Tree<ObjectType>> treeList) {
        List<TypeProxy> cmisTypeList = new ArrayList<TypeProxy>();
        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getTypeProxyFromCmis(tree.getItem()));
        }
        return cmisTypeList;
    }

    private TypeProxy getTypeProxyFromCmis(ObjectType objectType) {
        TypeProxy typeProxy = new TypeProxy();
        typeProxy.setId(objectType.getId());
        typeProxy.setDisplayName(objectType.getDisplayName());
        typeProxy.setBaseType(objectType.getBaseTypeId().value());
        List<TypeProxy> children = new ArrayList<TypeProxy>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeProxyFromCmis(child));
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
