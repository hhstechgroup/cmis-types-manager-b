package com.engagepoint.ejb;

import com.engagepoint.exception.CmisException;
import com.engagepoint.exception.CmisTypeDeleteException;
import com.engagepoint.pojo.*;
import com.engagepoint.util.CustomTypeUtils;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
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

import static org.apache.chemistry.opencmis.commons.impl.IOUtils.closeQuietly;

/**
 * User: AlexDenisenko
 * Date: 18/15/13
 * Time: 1:34 PM
 */
@Stateless
@LocalBean
public class Service {
    //    TODO check this
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    @EJB
    private CmisConnection connection;

    public void createType(UserInfo userInfo, Type type) throws AppException {
        Session session = connection.getSession(userInfo);
        TypeDefinition typeDefinition = getTypeDefinition(type);
        try {
            session.createType(CmisTypeUtils.getCorrectTypeDefinition(session, typeDefinition));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            session.getBinding().close();
        }
    }

    public void deleteType(UserInfo userInfo, String id) throws AppException {
        Session session = connection.getSession(userInfo);
        try {
            ObjectType type = session.getTypeDefinition(id);
            TypeMutability typeMutability = type.getTypeMutability();
            if (typeMutability != null && typeMutability.canDelete()) {
                session.deleteType(type.getId());
            } else {
                throw new AppException("Type is not deleted");
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            session.getBinding().close();
        }

    }

    public List<Type> findAllTypes(UserInfo userInfo, boolean includePropertyDefinition) throws AppException {
        Session session = connection.getSession(userInfo);
        List<Tree<ObjectType>> descendants = session.getTypeDescendants(null, -1, includePropertyDefinition);
        try {
            descendants = session.getTypeDescendants(null, -1, false);
            return getTypesFromTreeList(descendants);
        } finally {
            session.getBinding().close();
        }
    }

    public TypeDefinition findTypeById(UserInfo userInfo, String id) throws AppException {
        Session session = connection.getSession(userInfo);
        try {
            return session.getTypeDefinition(id);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            session.getBinding().close();
        }
    }

    public void importTypeFromXml(UserInfo userInfo, InputStream stream) throws AppException {
        Session session = connection.getSession(userInfo);
        try {
            List<AbstractTypeDefinition> definitionList;
            try {
                definitionList = CmisTypeUtils.readFromXML(stream);
                if (definitionList != null) {
                    for (AbstractTypeDefinition definition : definitionList) {
                        if (!definition.getId().equals(definition.getBaseTypeId().value())) {
                            session.createType(CmisTypeUtils.getCorrectTypeDefinition(session, definition));
                        }
                    }
                }
            } finally {
                closeQuietly(stream);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            session.getBinding().close();
        }
    }

    public void importTypeFromJson(UserInfo userInfo, InputStream stream) throws AppException {
        Session session = connection.getSession(userInfo);
        try {
            try {
                List<TypeDefinition> typeDefinition = CmisTypeUtils.readFromJSON(stream);
                for (TypeDefinition definition : typeDefinition) {
                    if (!definition.getId().equals(definition.getBaseTypeId().value())) {
                        session.createType(definition);
                    }
                }
            } finally {
                closeQuietly(stream);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            session.getBinding().close();
        }
    }

    public void exportTypeToXML(UserInfo userInfo, OutputStream out, String typeId, boolean includeChildren) throws AppException {
        Session session = connection.getSession(userInfo);
        List<Tree<ObjectType>> typeDescendants = null;
        if (includeChildren) {
            typeDescendants = session.getTypeDescendants(typeId, -1, true);
        }
        try {
            CmisTypeUtils.writeToXML(session, session.getTypeDefinition(typeId), out, typeDescendants);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }  catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            closeQuietly(out);
            session.getBinding().close();
        }

    }

    public void exportTypeToJSON(UserInfo userInfo, OutputStream out, String typeId, boolean includeChildren) throws AppException {
        Session session = connection.getSession(userInfo);
        List<Tree<ObjectType>> typeDescendants = null;
        if (includeChildren) {
            typeDescendants = session.getTypeDescendants(typeId, -1, true);
        }
        try {
            CmisTypeUtils.writeToJSON(session, session.getTypeDefinition(typeId), out, typeDescendants);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } finally {
            closeQuietly(out);
            session.getBinding().close();
        }

    }

    public List<Repository> getRepositories(UserInfo userInfo) throws AppException {
        return connection.getRepositories(userInfo);
    }

    public String getDefaultRepository(UserInfo userInfo) throws AppException {
        List<Repository> repositories = getRepositories(userInfo);
        return repositories.get(0).getId();
    }

    public boolean isUserExists(UserInfo userInfo) throws AppException {
        return connection.getSession(userInfo) != null;
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

    private List<Type> getTypesFromTreeList(List<Tree<ObjectType>> treeList) {
        List<Type> cmisTypeList = new ArrayList<Type>();
        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getTypeFromObjectType(tree.getItem()));
        }
        return cmisTypeList;
    }

    private Map<String, PropertyDefinition<?>> getPropertyDefinitionMap(List<PropertyDefinitionImpl> properties) {
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = new LinkedHashMap<String, PropertyDefinition<?>>();
        for (PropertyDefinitionImpl property : properties) {
            PropertyDefinitionImpl propertyDef = new PropertyDefinitionImpl();
            propertyDef.setId(property.getId());
            propertyDef.setDisplayName(property.getDisplayName());
            propertyDef.setLocalName(property.getLocalName());
            propertyDef.setDescription(property.getDescription());
            propertyDef.setQueryName(property.getQueryName());
            propertyDef.setCardinality(property.getCardinality());
            propertyDef.setPropertyType(property.getPropertyType());
            propertyDef.setIsRequired(property.isRequired());
            propertyDef.setIsInherited(property.isInherited());
            propertyDef.setUpdatability(property.getUpdatability());
            propertyDef.setIsQueryable(property.isQueryable());
            propertyDef.setIsOrderable(property.isOrderable());
            propertyDef.setLocalNamespace(property.getLocalNamespace());
            propertyDefinitionMap.put(property.getId(), propertyDef);
        }
        return propertyDefinitionMap;
    }

    private Type getTypeFromObjectType(ObjectType objectType) {
        Type type = new Type();
        type.setId(objectType.getId());
        type.setDisplayName(objectType.getDisplayName());
        type.setBaseTypeId(objectType.getBaseTypeId().value());
        type.setTypeMutability(objectType.getTypeMutability());
        List<Type> children = new ArrayList<Type>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeFromObjectType(child));
        }
        type.setChildren(children);
        return type;
    }

    @Deprecated
    public void setConnection(CmisConnection connection) {
        this.connection = connection;
    }
}
