package com.engagepoint.services;

import com.engagepoint.exceptions.CmisException;
import com.engagepoint.exceptions.CmisTypeDeleteException;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CmisService.class);
    @EJB
    private CmisConnection connection;

    //  TODO rename this method
    public List<TypeProxy> getTypeInfo(UserInfo userInfo) throws CmisException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, true);
        return getTypeProxies(descendants);
    }

    public TypeDefinition getTypeDefinition(UserInfo userInfo, TypeProxy type) throws CmisException {
        Session session = getSession(userInfo);
        try {
            return session.getTypeDefinition(type.getId());
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        }
    }

    public List<String> getNamesOfRootFolders(UserInfo userInfo) throws CmisException {
        Session session = getSession(userInfo);
        List<String> folders = new ArrayList<String>();
        Folder root = session.getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            folders.add(child.getName());
        }
        return folders;
    }

    public void createType(UserInfo userInfo, Type type) throws CmisException {
        Session session = getSession(userInfo);
        TypeDefinition typeDefinition = getTypeDefinition(type);
        try {
            session.createType(typeDefinition);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
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
            propertyDef.setDescription(property.getDescription());
            propertyDef.setQueryName(property.getQueryName());
            propertyDef.setCardinality(Cardinality.fromValue(property.getCardinality().toLowerCase()));
            propertyDef.setPropertyType(PropertyType.fromValue(property.getPropertyType().toLowerCase()));
            propertyDef.setIsRequired(property.getRequired());
            propertyDef.setIsInherited(property.getInherited());
            propertyDef.setUpdatability(Updatability.fromValue(property.getUpdatability().toLowerCase()));
            propertyDef.setIsQueryable(property.getQueryable());
            propertyDef.setIsOrderable(property.getOrderable());
            propertyDef.setLocalNamespace(property.getLocalNamespace());
            propertyDefinitionMap.put(property.getId(), propertyDef);
        }
        return propertyDefinitionMap;
    }

    public void importTypeFromXml(UserInfo userInfo, InputStream stream) throws CmisException, XMLStreamException {
        try {
            Session session = getSession(userInfo);
            List<AbstractTypeDefinition> definitionList;
            try {
                definitionList = CustomTypeUtils.readFromXML(stream);
                if (definitionList != null) {
                    for (AbstractTypeDefinition definition : definitionList) {
                        if (!definition.getId().equals(definition.getBaseTypeId().value())) {
                            session.createType(CustomTypeUtils.getCorrectTypeDefinition(session, definition));
                        }
                    }
                }
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        }
    }

    public void importTypeFromJson(UserInfo userInfo, InputStream stream) throws CmisException, JSONParseException {
        try {
            Session session = getSession(userInfo);
            try {
                TypeDefinition typeDefinition = TypeUtils.readFromJSON(stream);
                session.createType(typeDefinition);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        }
    }

    public void exportTypeToXML(UserInfo userInfo, OutputStream out, String typeId, boolean includeChildren) throws CmisException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> typeDescendants = null;
        if (includeChildren) {
            typeDescendants = session.getTypeDescendants(typeId, -1, true);
        }
        try {
            CustomTypeUtils.writeToXML(session, session.getTypeDefinition(typeId), out, typeDescendants);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public void exportTypeToJSON(UserInfo userInfo, OutputStream out, String typeId, boolean includeChildren) throws CmisException {
        Session session = getSession(userInfo);
        List<Tree<ObjectType>> typeDescendants = null;
        if (includeChildren) {
            typeDescendants = session.getTypeDescendants(typeId, -1, true);
        }
        try {
            CustomTypeUtils.writeToJSON(session, session.getTypeDefinition(typeId), out, typeDescendants);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }

    }

    public void deleteType(UserInfo userInfo, TypeProxy proxy) throws CmisException, CmisTypeDeleteException {
        Session session = getSession(userInfo);
        try {
            ObjectType type = session.getTypeDefinition(proxy.getId());
            TypeMutability typeMutability = type.getTypeMutability();
            if (typeMutability != null && Boolean.TRUE.equals(typeMutability.canDelete())) {
                session.deleteType(type.getId());
            } else {
                throw new CmisTypeDeleteException("Type is not deleted");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        }

    }

    public String getDefaultRepository(UserInfo userInfo) throws CmisException {
        List<Repository> repositories = getRepositories(userInfo);
        return repositories.get(0).getId();
    }

    public boolean isUserExists(UserInfo userInfo) throws CmisException {
        return getSession(userInfo) != null;
    }

    //  TODO change the logic to retrieve the parameters and check when it call
    public List<Repository> getRepositories(UserInfo userInfo) throws CmisException {
        Map<String, String> parameters = userInfo.getAtomPubParameters();
        List<Repository> repositories;
        try {
            repositories = connection.getSessionFactory().getRepositories(parameters);
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
        }
        return repositories;
    }

    private Session getSession(UserInfo userInfo) throws CmisException {
        Map<String, String> parameters = userInfo.getAtomPubParameters();
        Session session;
        try {
            session = connection.getSessionFactory().createSession(parameters);
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CmisException(e.getMessage());
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
        typeProxy.setTypeMutability(objectType.getTypeMutability());
        List<TypeProxy> children = new ArrayList<TypeProxy>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeProxyFromCmis(child));
        }
        typeProxy.setChildren(children);
        return typeProxy;
    }

    public RepositoryInfoImpl getRepositoryInfo(UserInfo userInfo) throws CmisException {
        Session session = getSession(userInfo);
        return new RepositoryInfoImpl(session.getRepositoryInfo());
    }


}
