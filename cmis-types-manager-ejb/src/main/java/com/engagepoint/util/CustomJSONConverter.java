package com.engagepoint.util;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.*;
import org.apache.chemistry.opencmis.commons.impl.JSONConverter;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.chemistry.opencmis.commons.impl.JSONConstants.*;

/**
 * OpenCMIS objects to JSON converter.
 */
public final class CustomJSONConverter {

    private CustomJSONConverter() {
    }

    public static List<TypeDefinition> convertTypeDefinitions(final Map<String, Object> jsonRaw) {
        List<TypeDefinition> typeDefinitionList = new ArrayList<TypeDefinition>();
        if (jsonRaw == null) {
            return typeDefinitionList;
        }
        Map<String, Object> json;
        if (JSONConverter.getString(jsonRaw, JSON_TYPE_ID) != null) {
            TypeDefinition typeDefinitionOne = JSONConverter.convertTypeDefinition(jsonRaw);
            typeDefinitionList.add(typeDefinitionOne);
        } else {
            List<Object> listObject = new ArrayList<Object>(jsonRaw.values());
            for (Object o : listObject) {
                json = JSONConverter.getMap(o);
                TypeDefinition typeDefinitionOne = JSONConverter.convertTypeDefinition(json);
                typeDefinitionList.add(typeDefinitionOne);
            }
        }
        return typeDefinitionList;
    }

    public static JSONObject convert(final TypeDefinition type, Session session) {
        if (type == null) {
            return null;
        }
        JSONObject result = new JSONObject();
        result.put(JSON_TYPE_ID, type.getId());
        result.put(JSON_TYPE_LOCALNAME, type.getLocalName());
        result.put(JSON_TYPE_LOCALNAMESPACE, type.getLocalNamespace());
        JSONConverter.setIfNotNull(JSON_TYPE_DISPLAYNAME, type.getDisplayName(), result);
        JSONConverter.setIfNotNull(JSON_TYPE_QUERYNAME, type.getQueryName(), result);
        JSONConverter.setIfNotNull(JSON_TYPE_DESCRIPTION, type.getDescription(), result);
        result.put(JSON_TYPE_BASE_ID, JSONConverter.getJSONEnumValue(type.getBaseTypeId()));
        JSONConverter.setIfNotNull(JSON_TYPE_PARENT_ID, type.getParentTypeId(), result);
        result.put(JSON_TYPE_CREATABLE, type.isCreatable());
        result.put(JSON_TYPE_FILEABLE, type.isFileable());
        result.put(JSON_TYPE_QUERYABLE, type.isQueryable());
        result.put(JSON_TYPE_FULLTEXT_INDEXED, type.isFulltextIndexed());
        result.put(JSON_TYPE_INCLUDE_IN_SUPERTYPE_QUERY, type.isIncludedInSupertypeQuery());
        result.put(JSON_TYPE_CONTROLABLE_POLICY, type.isControllablePolicy());
        result.put(JSON_TYPE_CONTROLABLE_ACL, type.isControllableAcl());

        if (type.getTypeMutability() != null) {
            TypeMutability typeMutability = type.getTypeMutability();
            JSONObject typeMutabilityJson = new JSONObject();

            typeMutabilityJson.put(JSON_TYPE_TYPE_MUTABILITY_CREATE, typeMutability.canCreate());
            typeMutabilityJson.put(JSON_TYPE_TYPE_MUTABILITY_UPDATE, typeMutability.canUpdate());
            typeMutabilityJson.put(JSON_TYPE_TYPE_MUTABILITY_DELETE, typeMutability.canDelete());

            JSONConverter.convertExtension(typeMutability, typeMutabilityJson);

            result.put(JSON_TYPE_TYPE_MUTABILITY, typeMutabilityJson);
        }

        if (type instanceof DocumentTypeDefinition) {
            result.put(JSON_TYPE_VERSIONABLE, ((DocumentTypeDefinition) type).isVersionable());
            result.put(JSON_TYPE_CONTENTSTREAM_ALLOWED,
                    JSONConverter.getJSONEnumValue(((DocumentTypeDefinition) type).getContentStreamAllowed()));
        }

        if (type instanceof RelationshipTypeDefinition) {
            result.put(JSON_TYPE_ALLOWED_SOURCE_TYPES,
                    JSONConverter.getJSONArrayFromList(((RelationshipTypeDefinition) type).getAllowedSourceTypeIds()));
            result.put(JSON_TYPE_ALLOWED_TARGET_TYPES,
                    JSONConverter.getJSONArrayFromList(((RelationshipTypeDefinition) type).getAllowedTargetTypeIds()));
        }

        if ((type.getPropertyDefinitions() != null) && (!type.getPropertyDefinitions().isEmpty())) {
            JSONObject propertyDefs = new JSONObject();
            for (PropertyDefinition<?> pd : CmisTypeUtils.getCorrectPropertyMapWithoutChangeTypeDefinition(session, type).values()) {
                if (pd != null) {
                    propertyDefs.put(pd.getId(), JSONConverter.convert(pd));
                }
            }

            result.put(JSON_TYPE_PROPERTY_DEFINITIONS, propertyDefs);
        }

        JSONConverter.convertExtension(type, result);

        return result;
    }

    public static JSONObject writeTypeDefinitions(Session session, final TypeDefinition type, List<Tree<ObjectType>> treeList) {
        JSONObject result = null;
        if (type != null) {
            result = convert(type, session);
            if (treeList != null && !treeList.isEmpty()) {
                JSONObject object = new JSONObject();
                object.put(type.getId(), result);
                writeChildrenType(object, session, treeList);
                result = object;
            }
        }
        return result;
    }

    public static void writeChildrenType(JSONObject result, Session session, List<Tree<ObjectType>> treeList) {
        for (Tree<ObjectType> node : treeList) {
            result.put(node.getItem().getId(), convert(node.getItem(), session));
            if (node.getChildren() != null) {
                writeChildrenType(result, session, node.getChildren());
            }
        }
    }

}
