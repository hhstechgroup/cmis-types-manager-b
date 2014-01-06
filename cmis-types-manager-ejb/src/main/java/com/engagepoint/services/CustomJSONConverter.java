package com.engagepoint.services;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.*;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.definitions.*;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.CmisEnumHelper;
import org.apache.chemistry.opencmis.commons.impl.JSONConverter;
import org.apache.chemistry.opencmis.commons.impl.TypeCache;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.*;
import org.apache.chemistry.opencmis.commons.impl.json.JSONArray;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.apache.chemistry.opencmis.commons.impl.JSONConstants.*;

/**
 * OpenCMIS objects to JSON converter.
 */
public final class CustomJSONConverter {

    private CustomJSONConverter() {
    }

    public enum PropertyMode {
        OBJECT, QUERY, CHANGE
    }

    /**
     * Converts a repository info object.
     */
    public static JSONObject convert(final RepositoryInfo repositoryInfo, final String repositoryUrl,
                                     final String rootUrl) {
        if (repositoryInfo == null) {
            return null;
        }

        JSONObject result = new JSONObject();

        result.put(JSON_REPINFO_ID, repositoryInfo.getId());
        result.put(JSON_REPINFO_NAME, repositoryInfo.getName());
        result.put(JSON_REPINFO_DESCRIPTION, repositoryInfo.getDescription());
        result.put(JSON_REPINFO_VENDOR, repositoryInfo.getVendorName());
        result.put(JSON_REPINFO_PRODUCT, repositoryInfo.getProductName());
        result.put(JSON_REPINFO_PRODUCT_VERSION, repositoryInfo.getProductVersion());
        result.put(JSON_REPINFO_ROOT_FOLDER_ID, repositoryInfo.getRootFolderId());
        result.put(JSON_REPINFO_CAPABILITIES, convert(repositoryInfo.getCapabilities()));
        setIfNotNull(JSON_REPINFO_ACL_CAPABILITIES, convert(repositoryInfo.getAclCapabilities()), result);
        result.put(JSON_REPINFO_CHANGE_LOG_TOKEN, repositoryInfo.getLatestChangeLogToken());
        result.put(JSON_REPINFO_CMIS_VERSION_SUPPORTED, repositoryInfo.getCmisVersionSupported());
        setIfNotNull(JSON_REPINFO_THIN_CLIENT_URI, repositoryInfo.getThinClientUri(), result);
        setIfNotNull(JSON_REPINFO_CHANGES_INCOMPLETE, repositoryInfo.getChangesIncomplete(), result);

        JSONArray changesOnType = new JSONArray();
        if (repositoryInfo.getChangesOnType() != null) {
            for (BaseTypeId type : repositoryInfo.getChangesOnType()) {
                if (type != null) {
                    changesOnType.add(getJSONStringValue(type.value()));
                }
            }
        }
        result.put(JSON_REPINFO_CHANGES_ON_TYPE, changesOnType);

        setIfNotNull(JSON_REPINFO_PRINCIPAL_ID_ANONYMOUS, repositoryInfo.getPrincipalIdAnonymous(), result);
        setIfNotNull(JSON_REPINFO_PRINCIPAL_ID_ANYONE, repositoryInfo.getPrincipalIdAnyone(), result);

        if (repositoryInfo.getExtensionFeatures() != null && !repositoryInfo.getExtensionFeatures().isEmpty()) {
            JSONArray extendedFeatures = new JSONArray();

            for (ExtensionFeature feature : repositoryInfo.getExtensionFeatures()) {
                JSONObject jsonFeature = new JSONObject();

                setIfNotNull(JSON_FEATURE_ID, feature.getId(), jsonFeature);
                setIfNotNull(JSON_FEATURE_URL, feature.getUrl(), jsonFeature);
                setIfNotNull(JSON_FEATURE_COMMON_NAME, feature.getCommonName(), jsonFeature);
                setIfNotNull(JSON_FEATURE_VERSION_LABEL, feature.getVersionLabel(), jsonFeature);
                setIfNotNull(JSON_FEATURE_DESCRIPTION, feature.getDescription(), jsonFeature);

                if (feature.getFeatureData() != null && !feature.getFeatureData().isEmpty()) {
                    JSONObject data = new JSONObject();
                    data.putAll(feature.getFeatureData());
                    jsonFeature.put(JSON_FEATURE_DATA, data);
                }

                convertExtension(feature, jsonFeature);

                extendedFeatures.add(jsonFeature);
            }

            result.put(JSON_REPINFO_EXTENDED_FEATURES, extendedFeatures);
        }

        result.put(JSON_REPINFO_REPOSITORY_URL, repositoryUrl);
        result.put(JSON_REPINFO_ROOT_FOLDER_URL, rootUrl);

        convertExtension(repositoryInfo, result);

        return result;
    }

    public static RepositoryCapabilities convertRepositoryCapabilities(final Map<String, Object> json) {
        if (json == null) {
            return null;
        }

        RepositoryCapabilitiesImpl result = new RepositoryCapabilitiesImpl();

        result.setCapabilityContentStreamUpdates(getEnum(json, JSON_CAP_CONTENT_STREAM_UPDATABILITY,
                CapabilityContentStreamUpdates.class));
        result.setCapabilityChanges(getEnum(json, JSON_CAP_CHANGES, CapabilityChanges.class));
        result.setCapabilityRendition(getEnum(json, JSON_CAP_RENDITIONS, CapabilityRenditions.class));
        result.setSupportsGetDescendants(getBoolean(json, JSON_CAP_GET_DESCENDANTS));
        result.setSupportsGetFolderTree(getBoolean(json, JSON_CAP_GET_FOLDER_TREE));
        result.setSupportsMultifiling(getBoolean(json, JSON_CAP_MULTIFILING));
        result.setSupportsUnfiling(getBoolean(json, JSON_CAP_UNFILING));
        result.setSupportsVersionSpecificFiling(getBoolean(json, JSON_CAP_VERSION_SPECIFIC_FILING));
        result.setIsPwcSearchable(getBoolean(json, JSON_CAP_PWC_SEARCHABLE));
        result.setIsPwcUpdatable(getBoolean(json, JSON_CAP_PWC_UPDATABLE));
        result.setAllVersionsSearchable(getBoolean(json, JSON_CAP_ALL_VERSIONS_SEARCHABLE));
        result.setOrderByCapability(getEnum(json, JSON_CAP_ORDER_BY, CapabilityOrderBy.class));
        result.setCapabilityQuery(getEnum(json, JSON_CAP_QUERY, CapabilityQuery.class));
        result.setCapabilityJoin(getEnum(json, JSON_CAP_JOIN, CapabilityJoin.class));
        result.setCapabilityAcl(getEnum(json, JSON_CAP_ACL, CapabilityAcl.class));

        Map<String, Object> creatablePropertyTypesJson = getMap(json.get(JSON_CAP_CREATABLE_PROPERTY_TYPES));
        if (creatablePropertyTypesJson != null) {
            CreatablePropertyTypesImpl creatablePropertyTypes = new CreatablePropertyTypesImpl();

            List<Object> canCreateJson = getList(creatablePropertyTypesJson
                    .get(JSON_CAP_CREATABLE_PROPERTY_TYPES_CANCREATE));
            if (canCreateJson != null) {
                Set<PropertyType> canCreate = EnumSet.noneOf(PropertyType.class);

                for (Object o : canCreateJson) {
                    try {
                        if (o != null) {
                            canCreate.add(PropertyType.fromValue(o.toString()));
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }

                creatablePropertyTypes.setCanCreate(canCreate);
            }

            convertExtension(creatablePropertyTypesJson, creatablePropertyTypes, CAP_CREATABLE_PROPERTY_TYPES_KEYS);

            result.setCreatablePropertyTypes(creatablePropertyTypes);
        }

        Map<String, Object> newTypeSettableAttributesJson = getMap(json.get(JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES));
        if (newTypeSettableAttributesJson != null) {
            NewTypeSettableAttributesImpl newTypeSettableAttributes = new NewTypeSettableAttributesImpl();

            newTypeSettableAttributes.setCanSetId(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_ID));
            newTypeSettableAttributes.setCanSetLocalName(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_LOCALNAME));
            newTypeSettableAttributes.setCanSetLocalNamespace(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_LOCALNAMESPACE));
            newTypeSettableAttributes.setCanSetDisplayName(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_DISPLAYNAME));
            newTypeSettableAttributes.setCanSetQueryName(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_QUERYNAME));
            newTypeSettableAttributes.setCanSetDescription(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_DESCRIPTION));
            newTypeSettableAttributes.setCanSetCreatable(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_CREATEABLE));
            newTypeSettableAttributes.setCanSetFileable(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_FILEABLE));
            newTypeSettableAttributes.setCanSetQueryable(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_QUERYABLE));
            newTypeSettableAttributes.setCanSetFulltextIndexed(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_FULLTEXTINDEXED));
            newTypeSettableAttributes.setCanSetIncludedInSupertypeQuery(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_INCLUDEDINSUPERTYTPEQUERY));
            newTypeSettableAttributes.setCanSetControllablePolicy(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_CONTROLABLEPOLICY));
            newTypeSettableAttributes.setCanSetControllableAcl(getBoolean(newTypeSettableAttributesJson,
                    JSON_CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_CONTROLABLEACL));

            convertExtension(newTypeSettableAttributesJson, newTypeSettableAttributes,
                    CAP_NEW_TYPE_SETTABLE_ATTRIBUTES_KEYS);

            result.setNewTypeSettableAttributes(newTypeSettableAttributes);
        }

        // handle extensions
        convertExtension(json, result, CAP_KEYS);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static AclCapabilities convertAclCapabilities(final Map<String, Object> json) {
        if (json == null) {
            return null;
        }

        AclCapabilitiesDataImpl result = new AclCapabilitiesDataImpl();

        result.setSupportedPermissions(getEnum(json, JSON_ACLCAP_SUPPORTED_PERMISSIONS, SupportedPermissions.class));
        result.setAclPropagation(getEnum(json, JSON_ACLCAP_ACL_PROPAGATION, AclPropagation.class));

        List<Object> permissions = getList(json.get(JSON_ACLCAP_PERMISSIONS));
        if (permissions != null) {
            List<PermissionDefinition> permissionDefinitionList = new ArrayList<PermissionDefinition>();

            for (Object permission : permissions) {
                Map<String, Object> permissionMap = getMap(permission);
                if (permissionMap != null) {
                    PermissionDefinitionDataImpl permDef = new PermissionDefinitionDataImpl();

                    permDef.setId(getString(permissionMap, JSON_ACLCAP_PERMISSION_PERMISSION));
                    permDef.setDescription(getString(permissionMap, JSON_ACLCAP_PERMISSION_DESCRIPTION));

                    convertExtension(permissionMap, permDef, ACLCAP_PERMISSION_KEYS);

                    permissionDefinitionList.add(permDef);
                }
            }

            result.setPermissionDefinitionData(permissionDefinitionList);
        }

        List<Object> permissionMapping = getList(json.get(JSON_ACLCAP_PERMISSION_MAPPING));
        if (permissionMapping != null) {
            Map<String, PermissionMapping> permMap = new HashMap<String, PermissionMapping>();

            for (Object permission : permissionMapping) {
                Map<String, Object> permissionMap = getMap(permission);
                if (permissionMap != null) {
                    PermissionMappingDataImpl mapping = new PermissionMappingDataImpl();

                    String key = getString(permissionMap, JSON_ACLCAP_MAPPING_KEY);
                    mapping.setKey(key);

                    Object perms = permissionMap.get(JSON_ACLCAP_MAPPING_PERMISSION);
                    if (perms instanceof List) {
                        List<String> permList = new ArrayList<String>();

                        for (Object perm : (List<Object>) perms) {
                            if (perm != null) {
                                permList.add(perm.toString());
                            }
                        }

                        mapping.setPermissions(permList);
                    }

                    convertExtension(permissionMap, mapping, ACLCAP_MAPPING_KEYS);

                    permMap.put(key, mapping);
                }
            }

            result.setPermissionMappingData(permMap);
        }

        // handle extensions
        convertExtension(json, result, ACLCAP_KEYS);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<TypeDefinition> convertTypeDefinition(final Map<String, Object> jsonRaw) {
        if (jsonRaw == null) {
            return null;
        }

        List<TypeDefinition> typeDefinitionList = new LinkedList<TypeDefinition>();

        Map<String, Object> json = null;
        if (getString(jsonRaw, JSON_TYPE_ID) != null) {
            TypeDefinition typeDefinitionOne = convertOneTypeDefinition(jsonRaw);
            typeDefinitionList.add(typeDefinitionOne);
        } else {
            List<Object> listObject = new LinkedList<Object>(jsonRaw.values());
            int sizeOfListObject = listObject.size();
            for (Object o : listObject) {
                json = getMap(o);
                TypeDefinition typeDefinitionOne = convertOneTypeDefinition(json);
                typeDefinitionList.add(typeDefinitionOne);
            }
        }

        return typeDefinitionList;
    }

    private static TypeDefinition convertOneTypeDefinition(final Map<String, Object> json){
        AbstractTypeDefinition result = null;

        String id = getString(json, JSON_TYPE_ID);

        // find base type
        BaseTypeId baseType = getEnum(json, JSON_TYPE_BASE_ID, BaseTypeId.class);
        if (baseType == null) {
            throw new CmisRuntimeException("Invalid base type: " + id);
        }

        switch (baseType) {
            case CMIS_FOLDER:
                result = new FolderTypeDefinitionImpl();
                break;
            case CMIS_DOCUMENT:
                result = new DocumentTypeDefinitionImpl();

                ((DocumentTypeDefinitionImpl) result).setContentStreamAllowed(getEnum(json,
                        JSON_TYPE_CONTENTSTREAM_ALLOWED, ContentStreamAllowed.class));
                ((DocumentTypeDefinitionImpl) result).setIsVersionable(getBoolean(json, JSON_TYPE_VERSIONABLE));

                break;
            case CMIS_RELATIONSHIP:
                result = new RelationshipTypeDefinitionImpl();

                Object allowedSourceTypes = json.get(JSON_TYPE_ALLOWED_SOURCE_TYPES);
                if (allowedSourceTypes instanceof List) {
                    List<String> types = new ArrayList<String>();
                    for (Object type : ((List<Object>) allowedSourceTypes)) {
                        if (type != null) {
                            types.add(type.toString());
                        }
                    }

                    ((RelationshipTypeDefinitionImpl) result).setAllowedSourceTypes(types);
                }

                Object allowedTargetTypes = json.get(JSON_TYPE_ALLOWED_TARGET_TYPES);
                if (allowedTargetTypes instanceof List) {
                    List<String> types = new ArrayList<String>();
                    for (Object type : ((List<Object>) allowedTargetTypes)) {
                        if (type != null) {
                            types.add(type.toString());
                        }
                    }

                    ((RelationshipTypeDefinitionImpl) result).setAllowedTargetTypes(types);
                }

                break;
            case CMIS_POLICY:
                result = new PolicyTypeDefinitionImpl();
                break;
            case CMIS_ITEM:
                result = new ItemTypeDefinitionImpl();
                break;
            case CMIS_SECONDARY:
                result = new SecondaryTypeDefinitionImpl();
                break;
            default:
                throw new CmisRuntimeException("Type '" + id + "' does not match a base type!");
        }

        result.setBaseTypeId(baseType);
        result.setDescription(getString(json, JSON_TYPE_DESCRIPTION));
        result.setDisplayName(getString(json, JSON_TYPE_DISPLAYNAME));
        result.setId(id);
        result.setIsControllableAcl(getBoolean(json, JSON_TYPE_CONTROLABLE_ACL));
        result.setIsControllablePolicy(getBoolean(json, JSON_TYPE_CONTROLABLE_POLICY));
        result.setIsCreatable(getBoolean(json, JSON_TYPE_CREATABLE));
        result.setIsFileable(getBoolean(json, JSON_TYPE_FILEABLE));
        result.setIsFulltextIndexed(getBoolean(json, JSON_TYPE_FULLTEXT_INDEXED));
        result.setIsIncludedInSupertypeQuery(getBoolean(json, JSON_TYPE_INCLUDE_IN_SUPERTYPE_QUERY));
        result.setIsQueryable(getBoolean(json, JSON_TYPE_QUERYABLE));
        result.setLocalName(getString(json, JSON_TYPE_LOCALNAME));
        result.setLocalNamespace(getString(json, JSON_TYPE_LOCALNAMESPACE));
        result.setParentTypeId(getString(json, JSON_TYPE_PARENT_ID));
        result.setQueryName(getString(json, JSON_TYPE_QUERYNAME));

        Map<String, Object> typeMutabilityJson = getMap(json.get(JSON_TYPE_TYPE_MUTABILITY));
        if (typeMutabilityJson != null) {
            TypeMutabilityImpl typeMutability = new TypeMutabilityImpl();

            typeMutability.setCanCreate(getBoolean(typeMutabilityJson, JSON_TYPE_TYPE_MUTABILITY_CREATE));
            typeMutability.setCanUpdate(getBoolean(typeMutabilityJson, JSON_TYPE_TYPE_MUTABILITY_UPDATE));
            typeMutability.setCanDelete(getBoolean(typeMutabilityJson, JSON_TYPE_TYPE_MUTABILITY_DELETE));

            convertExtension(typeMutabilityJson, typeMutability, JSON_TYPE_TYPE_MUTABILITY_KEYS);

            result.setTypeMutability(typeMutability);
        }

        Map<String, Object> propertyDefinitions = getMap(json.get(JSON_TYPE_PROPERTY_DEFINITIONS));
        if (propertyDefinitions != null) {
            for (Object propDef : propertyDefinitions.values()) {
                result.addPropertyDefinition(convertPropertyDefinition(getMap(propDef)));
            }
        }

        // handle extensions
        convertExtension(json, result, TYPE_KEYS);

        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static PropertyDefinition<?> convertPropertyDefinition(final Map<String, Object> json) {
        if (json == null) {
            return null;
        }

        AbstractPropertyDefinition<?> result = null;

        String id = getString(json, JSON_PROPERTY_ID);

        // find property type
        PropertyType propertyType = getEnum(json, JSON_PROPERTY_TYPE_PROPERTY_TYPE, PropertyType.class);
        if (propertyType == null) {
            throw new CmisRuntimeException("Invalid property type '" + id + "'! Data type not set!");
        }

        // find
        Cardinality cardinality = getEnum(json, JSON_PROPERTY_TYPE_CARDINALITY, Cardinality.class);
        if (cardinality == null) {
            throw new CmisRuntimeException("Invalid property type '" + id + "'! Cardinality not set!");
        }

        switch (propertyType) {
            case STRING:
                result = new PropertyStringDefinitionImpl();
                ((PropertyStringDefinitionImpl) result).setMaxLength(getInteger(json, JSON_PROPERTY_TYPE_MAX_LENGTH));
                ((PropertyStringDefinitionImpl) result)
                        .setChoices(convertChoicesString(json.get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case ID:
                result = new PropertyIdDefinitionImpl();
                ((PropertyIdDefinitionImpl) result).setChoices(convertChoicesString(json.get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case BOOLEAN:
                result = new PropertyBooleanDefinitionImpl();
                ((PropertyBooleanDefinitionImpl) result).setChoices(convertChoicesBoolean(json
                        .get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case INTEGER:
                result = new PropertyIntegerDefinitionImpl();
                ((PropertyIntegerDefinitionImpl) result).setMinValue(getInteger(json, JSON_PROPERTY_TYPE_MIN_VALUE));
                ((PropertyIntegerDefinitionImpl) result).setMaxValue(getInteger(json, JSON_PROPERTY_TYPE_MAX_VALUE));
                ((PropertyIntegerDefinitionImpl) result).setChoices(convertChoicesInteger(json
                        .get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case DATETIME:
                result = new PropertyDateTimeDefinitionImpl();
                ((PropertyDateTimeDefinitionImpl) result).setDateTimeResolution(getEnum(json,
                        JSON_PROPERTY_TYPE_RESOLUTION, DateTimeResolution.class));
                ((PropertyDateTimeDefinitionImpl) result).setChoices(convertChoicesDateTime(json
                        .get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case DECIMAL:
                result = new PropertyDecimalDefinitionImpl();
                ((PropertyDecimalDefinitionImpl) result).setMinValue(getDecimal(json, JSON_PROPERTY_TYPE_MIN_VALUE));
                ((PropertyDecimalDefinitionImpl) result).setMaxValue(getDecimal(json, JSON_PROPERTY_TYPE_MAX_VALUE));
                ((PropertyDecimalDefinitionImpl) result).setPrecision(getIntEnum(json, JSON_PROPERTY_TYPE_PRECISION,
                        DecimalPrecision.class));
                ((PropertyDecimalDefinitionImpl) result).setChoices(convertChoicesDecimal(json
                        .get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case HTML:
                result = new PropertyHtmlDefinitionImpl();
                ((PropertyHtmlDefinitionImpl) result).setChoices(convertChoicesString(json.get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            case URI:
                result = new PropertyUriDefinitionImpl();
                ((PropertyUriDefinitionImpl) result).setChoices(convertChoicesString(json.get(JSON_PROPERTY_TYPE_CHOICE)));
                break;
            default:
                throw new CmisRuntimeException("Property type '" + id + "' does not match a data type!");
        }

        // default value
        Object defaultValue = json.get(JSON_PROPERTY_TYPE_DEAULT_VALUE);
        if (defaultValue != null) {
            if (defaultValue instanceof List) {
                List values = new ArrayList();
                for (Object value : (List) defaultValue) {
                    values.add(getCMISValue(value, propertyType));
                }
                result.setDefaultValue(values);
            } else {
                result.setDefaultValue((List) Collections.singletonList(getCMISValue(defaultValue, propertyType)));
            }
        }

        // generic
        result.setId(id);
        result.setPropertyType(propertyType);
        result.setCardinality(cardinality);
        result.setLocalName(getString(json, JSON_PROPERTY_TYPE_LOCALNAME));
        result.setLocalNamespace(getString(json, JSON_PROPERTY_TYPE_LOCALNAMESPACE));
        result.setQueryName(getString(json, JSON_PROPERTY_TYPE_QUERYNAME));
        result.setDescription(getString(json, JSON_PROPERTY_TYPE_DESCRIPTION));
        result.setDisplayName(getString(json, JSON_PROPERTY_TYPE_DISPLAYNAME));
        result.setIsInherited(getBoolean(json, JSON_PROPERTY_TYPE_INHERITED));
        result.setIsOpenChoice(getBoolean(json, JSON_PROPERTY_TYPE_OPENCHOICE));
        result.setIsOrderable(getBoolean(json, JSON_PROPERTY_TYPE_ORDERABLE));
        result.setIsQueryable(getBoolean(json, JSON_PROPERTY_TYPE_QUERYABLE));
        result.setIsRequired(getBoolean(json, JSON_PROPERTY_TYPE_REQUIRED));
        result.setUpdatability(getEnum(json, JSON_PROPERTY_TYPE_UPDATABILITY, Updatability.class));

        // handle extensions
        convertExtension(json, result, PROPERTY_TYPE_KEYS);

        return result;
    }

    /**
     * Converts choices.
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Choice<String>> convertChoicesString(final Object choices) {
        if (!(choices instanceof List)) {
            return null;
        }

        List<Choice<String>> result = new ArrayList<Choice<String>>();

        for (Object obj : (List) choices) {
            Map<String, Object> choiceMap = getMap(obj);
            if (choiceMap != null) {
                ChoiceImpl<String> choice = new ChoiceImpl<String>();
                choice.setDisplayName(getString(choiceMap, JSON_PROPERTY_TYPE_CHOICE_DISPLAYNAME));

                Object choiceValue = choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_VALUE);
                List<String> values = new ArrayList<String>();
                if (choiceValue instanceof List) {
                    for (Object value : (List) choiceValue) {
                        values.add((String) getCMISValue(value, PropertyType.STRING));
                    }
                } else {
                    values.add((String) getCMISValue(choiceValue, PropertyType.STRING));
                }
                choice.setValue(values);

                choice.setChoice(convertChoicesString(choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_CHOICE)));

                result.add(choice);
            }
        }

        return result;
    }

    /**
     * Converts choices.
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Choice<Boolean>> convertChoicesBoolean(final Object choices) {
        if (!(choices instanceof List)) {
            return null;
        }

        List<Choice<Boolean>> result = new ArrayList<Choice<Boolean>>();

        for (Object obj : (List) choices) {
            Map<String, Object> choiceMap = getMap(obj);
            if (choiceMap != null) {
                ChoiceImpl<Boolean> choice = new ChoiceImpl<Boolean>();
                choice.setDisplayName(getString(choiceMap, JSON_PROPERTY_TYPE_CHOICE_DISPLAYNAME));

                Object choiceValue = choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_VALUE);
                List<Boolean> values = new ArrayList<Boolean>();
                if (choiceValue instanceof List) {
                    for (Object value : (List) choiceValue) {
                        values.add((Boolean) getCMISValue(value, PropertyType.BOOLEAN));
                    }
                } else {
                    values.add((Boolean) getCMISValue(choiceValue, PropertyType.BOOLEAN));
                }
                choice.setValue(values);

                choice.setChoice(convertChoicesBoolean(choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_CHOICE)));

                result.add(choice);
            }
        }

        return result;
    }

    /**
     * Converts choices.
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Choice<BigInteger>> convertChoicesInteger(final Object choices) {
        if (!(choices instanceof List)) {
            return null;
        }

        List<Choice<BigInteger>> result = new ArrayList<Choice<BigInteger>>();

        for (Object obj : (List) choices) {
            Map<String, Object> choiceMap = getMap(obj);
            if (choiceMap != null) {
                ChoiceImpl<BigInteger> choice = new ChoiceImpl<BigInteger>();
                choice.setDisplayName(getString(choiceMap, JSON_PROPERTY_TYPE_CHOICE_DISPLAYNAME));

                Object choiceValue = choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_VALUE);
                List<BigInteger> values = new ArrayList<BigInteger>();
                if (choiceValue instanceof List) {
                    for (Object value : (List) choiceValue) {
                        values.add((BigInteger) getCMISValue(value, PropertyType.INTEGER));
                    }
                } else {
                    values.add((BigInteger) getCMISValue(choiceValue, PropertyType.INTEGER));
                }
                choice.setValue(values);

                choice.setChoice(convertChoicesInteger(choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_CHOICE)));

                result.add(choice);
            }
        }

        return result;
    }

    /**
     * Converts choices.
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Choice<BigDecimal>> convertChoicesDecimal(final Object choices) {
        if (!(choices instanceof List)) {
            return null;
        }

        List<Choice<BigDecimal>> result = new ArrayList<Choice<BigDecimal>>();

        for (Object obj : (List) choices) {
            Map<String, Object> choiceMap = getMap(obj);
            if (choiceMap != null) {
                ChoiceImpl<BigDecimal> choice = new ChoiceImpl<BigDecimal>();
                choice.setDisplayName(getString(choiceMap, JSON_PROPERTY_TYPE_CHOICE_DISPLAYNAME));

                Object choiceValue = choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_VALUE);
                List<BigDecimal> values = new ArrayList<BigDecimal>();
                if (choiceValue instanceof List) {
                    for (Object value : (List) choiceValue) {
                        values.add((BigDecimal) getCMISValue(value, PropertyType.DECIMAL));
                    }
                } else {
                    values.add((BigDecimal) getCMISValue(choiceValue, PropertyType.DECIMAL));
                }
                choice.setValue(values);

                choice.setChoice(convertChoicesDecimal(choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_CHOICE)));

                result.add(choice);
            }
        }

        return result;
    }

    /**
     * Converts choices.
     */
    @SuppressWarnings({"rawtypes"})
    private static List<Choice<GregorianCalendar>> convertChoicesDateTime(final Object choices) {
        if (!(choices instanceof List)) {
            return null;
        }

        List<Choice<GregorianCalendar>> result = new ArrayList<Choice<GregorianCalendar>>();

        for (Object obj : (List) choices) {
            Map<String, Object> choiceMap = getMap(obj);
            if (choiceMap != null) {
                ChoiceImpl<GregorianCalendar> choice = new ChoiceImpl<GregorianCalendar>();
                choice.setDisplayName(getString(choiceMap, JSON_PROPERTY_TYPE_CHOICE_DISPLAYNAME));

                Object choiceValue = choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_VALUE);
                List<GregorianCalendar> values = new ArrayList<GregorianCalendar>();
                if (choiceValue instanceof List) {
                    for (Object value : (List) choiceValue) {
                        values.add((GregorianCalendar) getCMISValue(value, PropertyType.DATETIME));
                    }
                } else {
                    values.add((GregorianCalendar) getCMISValue(choiceValue, PropertyType.DATETIME));
                }
                choice.setValue(values);

                choice.setChoice(convertChoicesDateTime(choiceMap.get(JSON_PROPERTY_TYPE_CHOICE_CHOICE)));

                result.add(choice);
            }
        }

        return result;
    }

    /**
     * Converts a rendition.
     */
    public static RenditionData convertRendition(final Map<String, Object> json) {
        if (json == null) {
            return null;
        }

        RenditionDataImpl result = new RenditionDataImpl();

        result.setBigHeight(getInteger(json, JSON_RENDITION_HEIGHT));
        result.setKind(getString(json, JSON_RENDITION_KIND));
        result.setBigLength(getInteger(json, JSON_RENDITION_LENGTH));
        result.setMimeType(getString(json, JSON_RENDITION_MIMETYPE));
        result.setRenditionDocumentId(getString(json, JSON_RENDITION_DOCUMENT_ID));
        result.setStreamId(getString(json, JSON_RENDITION_STREAM_ID));
        result.setTitle(getString(json, JSON_RENDITION_TITLE));
        result.setBigWidth(getInteger(json, JSON_RENDITION_WIDTH));

        convertExtension(json, result, RENDITION_KEYS);

        return result;
    }

    /**
     * Converts a list of renditions.
     */
    public static List<RenditionData> convertRenditions(final List<Object> json) {
        if (json == null) {
            return null;
        }

        List<RenditionData> result = new ArrayList<RenditionData>();

        for (Object obj : json) {
            RenditionData rendition = convertRendition(getMap(obj));
            if (rendition != null) {
                result.add(rendition);
            }
        }

        return result;
    }


    /**
     * Converts bulk update data.
     */


    /**
     * Converts bulk update data lists.
     */


    /**
     * Converts bulk update data.
     */





    @SuppressWarnings("unchecked")
    public static void convertExtension(final Map<String, Object> source, final ExtensionsData target,
                                        final Set<String> cmisKeys) {
        if (source == null) {
            return;
        }

        List<CmisExtensionElement> extensions = null;

        for (Map.Entry<String, Object> element : source.entrySet()) {
            if (cmisKeys.contains(element.getKey())) {
                continue;
            }

            if (extensions == null) {
                extensions = new ArrayList<CmisExtensionElement>();
            }

            if (element.getValue() instanceof Map) {
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null,
                        convertExtension((Map<String, Object>) element.getValue())));
            } else if (element.getValue() instanceof List) {
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null,
                        convertExtension((List<Object>) element.getValue())));
            } else {
                String value = (element.getValue() == null ? null : element.getValue().toString());
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null, value));
            }
        }

        target.setExtensions(extensions);
    }

    @SuppressWarnings("unchecked")
    public static List<CmisExtensionElement> convertExtension(final Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        List<CmisExtensionElement> extensions = new ArrayList<CmisExtensionElement>();

        for (Map.Entry<String, Object> element : map.entrySet()) {
            if (element.getValue() instanceof Map) {
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null,
                        convertExtension((Map<String, Object>) element.getValue())));
            } else if (element.getValue() instanceof List) {
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null,
                        convertExtension((List<Object>) element.getValue())));
            } else {
                String value = (element.getValue() == null ? null : element.getValue().toString());
                extensions.add(new CmisExtensionElementImpl(null, element.getKey(), null, value));
            }
        }

        return extensions;
    }

    @SuppressWarnings("unchecked")
    public static List<CmisExtensionElement> convertExtension(final List<Object> list) {
        if (list == null) {
            return null;
        }

        List<CmisExtensionElement> extensions = new ArrayList<CmisExtensionElement>();

        int i = 0;
        for (Object element : list) {
            if (element instanceof Map) {
                extensions.add(new CmisExtensionElementImpl(null, "" + i, null,
                        convertExtension((Map<String, Object>) element)));
            } else if (element instanceof List) {
                extensions.add(new CmisExtensionElementImpl(null, "" + i, null,
                        convertExtension((List<Object>) element)));
            } else {
                String value = (element == null ? null : element.toString());
                extensions.add(new CmisExtensionElementImpl(null, "" + i, null, value));
            }

            i++;
        }

        return extensions;
    }

    // -----------------------------------------------------------------

    public static Object getCMISValue(final Object value, final PropertyType propertyType) {
        if (value == null) {
            return null;
        }

        switch (propertyType) {
            case STRING:
            case ID:
            case HTML:
            case URI:
                if (value instanceof String) {
                    return value;
                }
                throw new CmisRuntimeException("Invalid String value!");
            case BOOLEAN:
                if (value instanceof Boolean) {
                    return value;
                }
                throw new CmisRuntimeException("Invalid Boolean value!");
            case INTEGER:
                if (value instanceof BigInteger) {
                    return value;
                }
                throw new CmisRuntimeException("Invalid Integer value!");
            case DECIMAL:
                if (value instanceof BigDecimal) {
                    return value;
                }
                throw new CmisRuntimeException("Invalid Decimal value!");
            case DATETIME:
                if (value instanceof Number) {
                    GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
                    cal.setTimeInMillis(((Number) value).longValue());
                    return cal;
                }
                throw new CmisRuntimeException("Invalid DateTime value!");
        }

        throw new CmisRuntimeException("Unkown property type!");
    }

    public static JSONArray getJSONArrayFromList(final List<?> list) {
        if (list == null) {
            return null;
        }

        JSONArray result = new JSONArray();
        result.addAll(list);

        return result;
    }

    public static String getJSONPropertyDataType(final PropertyData<?> property) {
        if (property instanceof PropertyBoolean) {
            return PropertyType.BOOLEAN.value();
        } else if (property instanceof PropertyId) {
            return PropertyType.ID.value();
        } else if (property instanceof PropertyInteger) {
            return PropertyType.INTEGER.value();
        } else if (property instanceof PropertyDateTime) {
            return PropertyType.DATETIME.value();
        } else if (property instanceof PropertyDecimal) {
            return PropertyType.DECIMAL.value();
        } else if (property instanceof PropertyHtml) {
            return PropertyType.HTML.value();
        } else if (property instanceof PropertyString) {
            return PropertyType.STRING.value();
        } else if (property instanceof PropertyUri) {
            return PropertyType.URI.value();
        }

        return null;
    }

    public static void setIfNotNull(String name, Object obj, JSONObject json) {
        if (obj != null) {
            json.put(name, obj);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(final Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Map) {
            return (Map<String, Object>) o;
        }

        throw new CmisRuntimeException("Expected a JSON object but found a "
                + (o instanceof List ? "JSON array" : o.getClass().getSimpleName()) + ": " + o.toString());
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getList(final Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof List) {
            return (List<Object>) o;
        }

        throw new CmisRuntimeException("Expected a JSON array but found a "
                + (o instanceof List ? "JSON object" : o.getClass().getSimpleName()) + ": " + o.toString());
    }

    public static String getString(final Map<String, Object> json, final String key) {
        Object obj = json.get(key);
        return obj == null ? null : obj.toString();
    }

    public static Boolean getBoolean(final Map<String, Object> json, final String key) {
        Object obj = json.get(key);

        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }

        return null;
    }

    public static BigInteger getInteger(final Map<String, Object> json, final String key) {
        Object obj = json.get(key);

        if (obj instanceof BigInteger) {
            return (BigInteger) obj;
        }

        return null;
    }

    public static BigDecimal getDecimal(final Map<String, Object> json, final String key) {
        Object obj = json.get(key);

        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }

        if (obj instanceof BigInteger) {
            return new BigDecimal((BigInteger) obj);
        }

        return null;
    }

    public static GregorianCalendar getDateTime(final Map<String, Object> json, final String key) {
        Object obj = json.get(key);

        if (obj instanceof Number) {
            GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis(((Number) obj).longValue());
            return cal;
        }

        return null;
    }

    public static <T extends Enum<T>> T getEnum(final Map<String, Object> json, final String key, final Class<T> clazz) {
        return CmisEnumHelper.fromValue(getString(json, key), clazz);
    }

    public static <T extends Enum<T>> T getIntEnum(final Map<String, Object> json, final String key,
                                                   final Class<T> clazz) {
        return CmisEnumHelper.fromValue(getInteger(json, key), clazz);
    }
}
