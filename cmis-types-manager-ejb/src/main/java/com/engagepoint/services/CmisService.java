package com.engagepoint.services;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.exceptions.CmisExportException;
import com.engagepoint.exceptions.CmisTypeDeleteException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * User: AlexDenisenko
 * Date: 18/15/13
 * Time: 1:34 PM
 */
@Stateless
@LocalBean
public class CmisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmisService.class);
    @EJB
    private CmisConnection connection;

    public List<TypeProxy> getTypeInfo(final UserInfo userInfo) throws CmisConnectException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, true);
        return getTypeProxies(descendants);
    }

    public TypeDefinition getTypeDefinition(final UserInfo userInfo, TypeProxy type) throws CmisConnectException {
        Session session = getSession(userInfo);
        try {
            return session.getTypeDefinition(type.getId());
        } catch (Exception e) {
            throw new CmisConnectException(e.getMessage());
        }
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
        typeDef.setQueryName(type.getQueryName());
        typeDef.setIsControllablePolicy(type.isControllablePolicy());
        typeDef.setIsControllableAcl(type.isControllableAcl());
        typeDef.setIsCreatable(type.isCreatable());
        typeDef.setIsFileable(type.isFileable());
        typeDef.setIsQueryable(type.isQueryable());
        typeDef.setIsFulltextIndexed(type.isFulltextIndexed());
        typeDef.setIsIncludedInSupertypeQuery(type.isIncludedInSupertypeQuery());
        typeDef.setLocalNamespace(type.getLocalNamespace());

        if (type.getProperties() != null) {
            typeDef.setPropertyDefinitions(getPropertyDefinitionMap(type.getProperties()));
        }
        return typeDef;
    }

    private Map<String, PropertyDefinition<?>> getPropertyDefinitionMap(List<TypeProperty> properties) {
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = new LinkedHashMap<String, PropertyDefinition<?>>();
        for (TypeProperty property : properties) {
            PropertyDefinitionImpl propertyDef = new PropertyDefinitionImpl();
            propertyDef.setId(property.getId());
            propertyDef.setDisplayName(property.getDisplayName());
            propertyDef.setLocalName(property.getLocalName());
            propertyDef.setQueryName(property.getQueryName());
            propertyDef.setCardinality(Cardinality.fromValue(property.getCardinality().toLowerCase()));
            propertyDef.setPropertyType(PropertyType.fromValue(property.getPropertyType().toLowerCase()));
            propertyDef.setIsRequired(property.getRequired());
            propertyDef.setIsInherited(property.getInherited());
            propertyDef.setUpdatability(Updatability.fromValue(property.getUpdatability().toLowerCase()));
            propertyDef.setIsOrderable(property.getOrderable());
            propertyDef.setLocalNamespace(property.getLocalNamespace());
            propertyDefinitionMap.put(property.getId(), propertyDef);
        }
        return propertyDefinitionMap;
    }

    public void importTypeFromXml(UserInfo userInfo, InputStream stream) throws CmisConnectException, XMLStreamException, CmisCreateException {
        try {
            Session session = getSession(userInfo);
            try {
                TypeDefinition typeDefinition = TypeUtils.readFromXML(stream);
                session.createType(typeDefinition);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (RuntimeException e) {
            throw new CmisCreateException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void importTypeFromJson(UserInfo userInfo, InputStream stream) throws CmisConnectException, CmisCreateException, JSONParseException {
        try {
            Session session = getSession(userInfo);
            try {
                TypeDefinition typeDefinition = TypeUtils.readFromJSON(stream);
                session.createType(typeDefinition);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (RuntimeException e) {
            throw new CmisCreateException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
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
            LOGGER.error(e.getMessage(), e);
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

    public void exportTypeToXML(final UserInfo userInfo, OutputStream out, String typeId) throws CmisConnectException, CmisExportException {
        Session session = getSession(userInfo);
        try {
            TypeUtils.writeToXML(session.getTypeDefinition(typeId), out);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisExportException(e.getMessage());
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void exportTypeToJSON(final UserInfo userInfo, OutputStream out, String typeId) throws CmisConnectException, CmisExportException {
        Session session = getSession(userInfo);
        try {
            TypeUtils.writeToJSON(session.getTypeDefinition(typeId), out);

        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisExportException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
