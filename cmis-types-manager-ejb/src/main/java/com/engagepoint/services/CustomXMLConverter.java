package com.engagepoint.services;

import org.apache.chemistry.opencmis.commons.data.*;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.impl.XMLWalker;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import static org.apache.chemistry.opencmis.commons.impl.XMLConstants.*;

public class CustomXMLConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmisService.class);

    private CustomXMLConverter() {
    }

    public static List<AbstractTypeDefinition> convertTypeDefinitionFromTree(XMLStreamReader parser) throws XMLStreamException {
        return TYPE_TREE_DEF_PARSER.walk(parser);
    }


    private static final XMLWalker<List<AbstractTypeDefinition>> TYPE_TREE_DEF_PARSER = new XMLWalker<List<AbstractTypeDefinition>>() {
        @Override
        protected List<AbstractTypeDefinition> prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            List<AbstractTypeDefinition> result = new ArrayList<AbstractTypeDefinition>();
            return result;
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, List<AbstractTypeDefinition> target) throws XMLStreamException {
            target.add(TYPE_DEF_PARSER.walk(parser));
            return true;
        }
    };

    private static final XMLWalker<AbstractTypeDefinition> TYPE_DEF_PARSER = new XMLWalker<AbstractTypeDefinition>() {
        @Override
        protected AbstractTypeDefinition prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {

            AbstractTypeDefinition result = null;

            String typeAttr = parser.getAttributeValue(NAMESPACE_XSI, "type");
            if (typeAttr != null) {
                if (typeAttr.endsWith(ATTR_DOCUMENT_TYPE)) {
                    result = new DocumentTypeDefinitionImpl();
                } else if (typeAttr.endsWith(ATTR_FOLDER_TYPE)) {
                    result = new FolderTypeDefinitionImpl();
                } else if (typeAttr.endsWith(ATTR_RELATIONSHIP_TYPE)) {
                    result = new RelationshipTypeDefinitionImpl();
                    ((RelationshipTypeDefinitionImpl) result).setAllowedSourceTypes(new ArrayList<String>());
                    ((RelationshipTypeDefinitionImpl) result).setAllowedTargetTypes(new ArrayList<String>());
                } else if (typeAttr.endsWith(ATTR_POLICY_TYPE)) {
                    result = new PolicyTypeDefinitionImpl();
                } else if (typeAttr.endsWith(ATTR_ITEM_TYPE)) {
                    result = new ItemTypeDefinitionImpl();
                } else if (typeAttr.endsWith(ATTR_SECONDARY_TYPE)) {
                    result = new SecondaryTypeDefinitionImpl();
                }
            }

            if (result == null) {
                throw new CmisInvalidArgumentException("Cannot read type definition!");
            }

            return result;
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AbstractTypeDefinition target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_TYPE_ID)) {
                    target.setId(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_LOCALNAME)) {
                    target.setLocalName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_LOCALNAMESPACE)) {
                    target.setLocalNamespace(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_DISPLAYNAME)) {
                    target.setDisplayName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_QUERYNAME)) {
                    target.setQueryName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_DESCRIPTION)) {
                    target.setDescription(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_BASE_ID)) {
                    BaseTypeId baseType = readEnum(parser, BaseTypeId.class);
                    if (baseType == null) {
                        throw new CmisInvalidArgumentException("Invalid base type!");
                    }

                    target.setBaseTypeId(baseType);
                    return true;
                }

                if (isTag(name, TAG_TYPE_PARENT_ID)) {
                    target.setParentTypeId(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_CREATABLE)) {
                    target.setIsCreatable(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_FILEABLE)) {
                    target.setIsFileable(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_QUERYABLE)) {
                    target.setIsQueryable(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_FULLTEXT_INDEXED)) {
                    target.setIsFulltextIndexed(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_INCLUDE_IN_SUPERTYPE_QUERY)) {
                    target.setIsIncludedInSupertypeQuery(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_CONTROLABLE_POLICY)) {
                    target.setIsControllablePolicy(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_CONTROLABLE_ACL)) {
                    target.setIsControllableAcl(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_TYPE_MUTABILITY)) {
                    target.setTypeMutability(TYPE_MUTABILITY_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_PROP_DEF_STRING) || isTag(name, TAG_TYPE_PROP_DEF_ID)
                        || isTag(name, TAG_TYPE_PROP_DEF_BOOLEAN) || isTag(name, TAG_TYPE_PROP_DEF_INTEGER)
                        || isTag(name, TAG_TYPE_PROP_DEF_DATETIME) || isTag(name, TAG_TYPE_PROP_DEF_DECIMAL)
                        || isTag(name, TAG_TYPE_PROP_DEF_HTML) || isTag(name, TAG_TYPE_PROP_DEF_URI)) {
                    target.addPropertyDefinition(PROPERTY_TYPE_PARSER.walk(parser));
                    return true;
                }

                if (target instanceof DocumentTypeDefinitionImpl) {
                    if (isTag(name, TAG_TYPE_VERSIONABLE)) {
                        ((DocumentTypeDefinitionImpl) target).setIsVersionable(readBoolean(parser));
                        return true;
                    }

                    if (isTag(name, TAG_TYPE_CONTENTSTREAM_ALLOWED)) {
                        ((DocumentTypeDefinitionImpl) target).setContentStreamAllowed(readEnum(parser,
                                ContentStreamAllowed.class));
                        return true;
                    }
                }

                if (target instanceof RelationshipTypeDefinitionImpl) {
                    if (isTag(name, TAG_TYPE_ALLOWED_SOURCE_TYPES)) {
                        RelationshipTypeDefinitionImpl relTarget = (RelationshipTypeDefinitionImpl) target;
                        relTarget.setAllowedSourceTypes(addToList(relTarget.getAllowedSourceTypeIds(), readText(parser)));
                        return true;
                    }

                    if (isTag(name, TAG_TYPE_ALLOWED_TARGET_TYPES)) {
                        RelationshipTypeDefinitionImpl relTarget = (RelationshipTypeDefinitionImpl) target;
                        relTarget.setAllowedTargetTypes(addToList(relTarget.getAllowedTargetTypeIds(), readText(parser)));
                        return true;
                    }
                }
            }
            return false;
        }
    };

    private static final XMLWalker<TypeMutabilityImpl> TYPE_MUTABILITY_PARSER = new XMLWalker<TypeMutabilityImpl>() {
        @Override
        protected TypeMutabilityImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new TypeMutabilityImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, TypeMutabilityImpl target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_TYPE_TYPE_MUTABILITY_CREATE)) {
                    target.setCanCreate(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_TYPE_MUTABILITY_UPDATE)) {
                    target.setCanUpdate(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_TYPE_TYPE_MUTABILITY_DELETE)) {
                    target.setCanDelete(readBoolean(parser));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<AbstractPropertyDefinition<?>> PROPERTY_TYPE_PARSER = new XMLWalker<AbstractPropertyDefinition<?>>() {
        @Override
        protected AbstractPropertyDefinition<?> prepareTarget(XMLStreamReader parser, QName name)
                throws XMLStreamException {
            AbstractPropertyDefinition<?> result = null;

            if (isTag(name, TAG_TYPE_PROP_DEF_STRING)) {
                result = new PropertyStringDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_ID)) {
                result = new PropertyIdDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_BOOLEAN)) {
                result = new PropertyBooleanDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_INTEGER)) {
                result = new PropertyIntegerDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_DATETIME)) {
                result = new PropertyDateTimeDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_DECIMAL)) {
                result = new PropertyDecimalDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_HTML)) {
                result = new PropertyHtmlDefinitionImpl();
            } else if (isTag(name, TAG_TYPE_PROP_DEF_URI)) {
                result = new PropertyUriDefinitionImpl();
            }

            if (result == null) {
                throw new CmisInvalidArgumentException("Cannot read property type definition!");
            }

            return result;
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AbstractPropertyDefinition<?> target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_PROPERTY_TYPE_ID)) {
                    target.setId(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_LOCALNAME)) {
                    target.setLocalName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_LOCALNAMESPACE)) {
                    target.setLocalNamespace(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_DISPLAYNAME)) {
                    target.setDisplayName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_QUERYNAME)) {
                    target.setQueryName(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_DESCRIPTION)) {
                    target.setDescription(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_PROPERTY_TYPE)) {
                    PropertyType propType = readEnum(parser, PropertyType.class);
                    if (propType == null) {
                        throw new CmisInvalidArgumentException("Invalid property type!");
                    }

                    target.setPropertyType(propType);
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_CARDINALITY)) {
                    Cardinality cardinality = readEnum(parser, Cardinality.class);
                    if (cardinality == null) {
                        throw new CmisInvalidArgumentException("Invalid cardinality!");
                    }

                    target.setCardinality(cardinality);
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_UPDATABILITY)) {
                    Updatability updatability = readEnum(parser, Updatability.class);
                    if (updatability == null) {
                        throw new CmisInvalidArgumentException("Invalid updatability!");
                    }

                    target.setUpdatability(updatability);
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_INHERITED)) {
                    target.setIsInherited(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_REQUIRED)) {
                    target.setIsRequired(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_QUERYABLE)) {
                    target.setIsQueryable(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_ORDERABLE)) {
                    target.setIsOrderable(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_OPENCHOICE)) {
                    target.setIsOpenChoice(readBoolean(parser));
                    return true;
                }

                if (target instanceof PropertyStringDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyString prop = PROPERTY_STRING_PARSER.walk(parser);
                        ((PropertyStringDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_STRING_PARSER.addToChoiceList(parser, (PropertyStringDefinitionImpl) target);
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_MAX_LENGTH)) {
                        ((PropertyStringDefinitionImpl) target).setMaxLength(readInteger(parser));
                        return true;
                    }
                } else if (target instanceof PropertyIdDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyId prop = PROPERTY_ID_PARSER.walk(parser);
                        ((PropertyIdDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_STRING_PARSER.addToChoiceList(parser, (PropertyIdDefinitionImpl) target);
                        return true;
                    }
                } else if (target instanceof PropertyBooleanDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyBoolean prop = PROPERTY_BOOLEAN_PARSER.walk(parser);
                        ((PropertyBooleanDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_BOOLEAN_PARSER.addToChoiceList(parser, (PropertyBooleanDefinitionImpl) target);
                        return true;
                    }
                } else if (target instanceof PropertyIntegerDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyInteger prop = PROPERTY_INTEGER_PARSER.walk(parser);
                        ((PropertyIntegerDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_INTEGER_PARSER.addToChoiceList(parser, (PropertyIntegerDefinitionImpl) target);
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_MAX_VALUE)) {
                        ((PropertyIntegerDefinitionImpl) target).setMaxValue(readInteger(parser));
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_MIN_VALUE)) {
                        ((PropertyIntegerDefinitionImpl) target).setMinValue(readInteger(parser));
                        return true;
                    }
                } else if (target instanceof PropertyDateTimeDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyDateTime prop = PROPERTY_DATETIME_PARSER.walk(parser);
                        ((PropertyDateTimeDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_DATETIME_PARSER.addToChoiceList(parser, (PropertyDateTimeDefinitionImpl) target);
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_RESOLUTION)) {
                        ((PropertyDateTimeDefinitionImpl) target).setDateTimeResolution(readEnum(parser,
                                DateTimeResolution.class));
                        return true;
                    }
                } else if (target instanceof PropertyDecimalDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyDecimal prop = PROPERTY_DECIMAL_PARSER.walk(parser);
                        ((PropertyDecimalDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_DECIMAL_PARSER.addToChoiceList(parser, (PropertyDecimalDefinitionImpl) target);
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_MAX_VALUE)) {
                        ((PropertyDecimalDefinitionImpl) target).setMaxValue(readDecimal(parser));
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_MIN_VALUE)) {
                        ((PropertyDecimalDefinitionImpl) target).setMinValue(readDecimal(parser));
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_PRECISION)) {
                        try {
                            ((PropertyDecimalDefinitionImpl) target).setPrecision(DecimalPrecision
                                    .fromValue(readInteger(parser)));
                        } catch (IllegalArgumentException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                        return true;
                    }
                } else if (target instanceof PropertyHtmlDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyHtml prop = PROPERTY_HTML_PARSER.walk(parser);
                        ((PropertyHtmlDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_STRING_PARSER.addToChoiceList(parser, (PropertyHtmlDefinitionImpl) target);
                        return true;
                    }
                } else if (target instanceof PropertyUriDefinitionImpl) {
                    if (isTag(name, TAG_PROPERTY_TYPE_DEAULT_VALUE)) {
                        PropertyUri prop = PROPERTY_URI_PARSER.walk(parser);
                        ((PropertyUriDefinitionImpl) target).setDefaultValue(prop.getValues());
                        return true;
                    }

                    if (isTag(name, TAG_PROPERTY_TYPE_CHOICE)) {
                        CHOICE_STRING_PARSER.addToChoiceList(parser, (PropertyUriDefinitionImpl) target);
                        return true;
                    }
                }
            }

            return false;
        }
    };

    private static final ChoiceXMLWalker<String> CHOICE_STRING_PARSER = new ChoiceXMLWalker<String>() {
        @Override
        protected ChoiceImpl<String> createTarget(XMLStreamReader parser, QName name) {
            return new ChoiceImpl<String>();
        }

        @Override
        protected void addValue(XMLStreamReader parser, ChoiceImpl<String> target) throws XMLStreamException {
            target.setValue(addToList(target.getValue(), readText(parser)));
        }

        protected void addChoice(XMLStreamReader parser, ChoiceImpl<String> target) throws XMLStreamException {
            target.setChoice(addToList(target.getChoice(), CHOICE_STRING_PARSER.walk(parser)));
        }
    };

    private static final ChoiceXMLWalker<Boolean> CHOICE_BOOLEAN_PARSER = new ChoiceXMLWalker<Boolean>() {
        @Override
        protected ChoiceImpl<Boolean> createTarget(XMLStreamReader parser, QName name) {
            return new ChoiceImpl<Boolean>();
        }

        @Override
        protected void addValue(XMLStreamReader parser, ChoiceImpl<Boolean> target) throws XMLStreamException {
            target.setValue(addToList(target.getValue(), readBoolean(parser)));
        }

        protected void addChoice(XMLStreamReader parser, ChoiceImpl<Boolean> target) throws XMLStreamException {
            target.setChoice(addToList(target.getChoice(), CHOICE_BOOLEAN_PARSER.walk(parser)));
        }
    };

    private static final ChoiceXMLWalker<BigInteger> CHOICE_INTEGER_PARSER = new ChoiceXMLWalker<BigInteger>() {
        @Override
        protected ChoiceImpl<BigInteger> createTarget(XMLStreamReader parser, QName name) {
            return new ChoiceImpl<BigInteger>();
        }

        @Override
        protected void addValue(XMLStreamReader parser, ChoiceImpl<BigInteger> target) throws XMLStreamException {
            target.setValue(addToList(target.getValue(), readInteger(parser)));
        }

        protected void addChoice(XMLStreamReader parser, ChoiceImpl<BigInteger> target) throws XMLStreamException {
            target.setChoice(addToList(target.getChoice(), CHOICE_INTEGER_PARSER.walk(parser)));
        }
    };

    private static final ChoiceXMLWalker<GregorianCalendar> CHOICE_DATETIME_PARSER = new ChoiceXMLWalker<GregorianCalendar>() {
        @Override
        protected ChoiceImpl<GregorianCalendar> createTarget(XMLStreamReader parser, QName name) {
            return new ChoiceImpl<GregorianCalendar>();
        }

        @Override
        protected void addValue(XMLStreamReader parser, ChoiceImpl<GregorianCalendar> target) throws XMLStreamException {
            target.setValue(addToList(target.getValue(), readDateTime(parser)));
        }

        protected void addChoice(XMLStreamReader parser, ChoiceImpl<GregorianCalendar> target)
                throws XMLStreamException {
            target.setChoice(addToList(target.getChoice(), CHOICE_DATETIME_PARSER.walk(parser)));
        }
    };

    private static final ChoiceXMLWalker<BigDecimal> CHOICE_DECIMAL_PARSER = new ChoiceXMLWalker<BigDecimal>() {
        @Override
        protected ChoiceImpl<BigDecimal> createTarget(XMLStreamReader parser, QName name) {
            return new ChoiceImpl<BigDecimal>();
        }

        @Override
        protected void addValue(XMLStreamReader parser, ChoiceImpl<BigDecimal> target) throws XMLStreamException {
            target.setValue(addToList(target.getValue(), readDecimal(parser)));
        }

        protected void addChoice(XMLStreamReader parser, ChoiceImpl<BigDecimal> target) throws XMLStreamException {
            target.setChoice(addToList(target.getChoice(), CHOICE_DECIMAL_PARSER.walk(parser)));
        }
    };

    private abstract static class ChoiceXMLWalker<T> extends XMLWalker<ChoiceImpl<T>> {

        public void addToChoiceList(XMLStreamReader parser, AbstractPropertyDefinition<T> propDef)
                throws XMLStreamException {
            propDef.setChoices(addToList(propDef.getChoices(), walk(parser)));
        }

        protected abstract ChoiceImpl<T> createTarget(XMLStreamReader parser, QName name);

        @Override
        protected ChoiceImpl<T> prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            ChoiceImpl<T> result = createTarget(parser, name);

            if (parser.getAttributeCount() > 0) {
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    String attr = parser.getAttributeLocalName(i);
                    if (ATTR_PROPERTY_TYPE_CHOICE_DISPLAYNAME.equals(attr)) {
                        result.setDisplayName(parser.getAttributeValue(i));
                    }
                }
            }

            return result;
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, ChoiceImpl<T> target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_PROPERTY_TYPE_CHOICE_VALUE)) {
                    addValue(parser, target);
                    return true;
                }

                if (isTag(name, TAG_PROPERTY_TYPE_CHOICE_CHOICE)) {
                    addChoice(parser, target);
                    return true;
                }
            }

            return false;
        }

        protected abstract void addValue(XMLStreamReader parser, ChoiceImpl<T> target) throws XMLStreamException;

        protected abstract void addChoice(XMLStreamReader parser, ChoiceImpl<T> target) throws XMLStreamException;
    };

    // ---------------------------------
    // --- objects and lists parsers ---
    // ---------------------------------

    private static final XMLWalker<ObjectDataImpl> OBJECT_PARSER = new XMLWalker<ObjectDataImpl>() {
        @Override
        protected ObjectDataImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new ObjectDataImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, ObjectDataImpl target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_OBJECT_PROPERTIES)) {
                    target.setProperties(PROPERTIES_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_ALLOWABLE_ACTIONS)) {
                    target.setAllowableActions(ALLOWABLE_ACTIONS_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_RELATIONSHIP)) {
                    target.setRelationships(addToList(target.getRelationships(), OBJECT_PARSER.walk(parser)));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_CHANGE_EVENT_INFO)) {
                    target.setChangeEventInfo(CHANGE_EVENT_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_ACL)) {
                    target.setAcl(ACL_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_EXACT_ACL)) {
                    target.setIsExactAcl(readBoolean(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_POLICY_IDS)) {
                    target.setPolicyIds(POLICY_IDS_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_OBJECT_RENDITION)) {
                    target.setRenditions(addToList(target.getRenditions(), RENDITION_PARSER.walk(parser)));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<PropertiesImpl> PROPERTIES_PARSER = new XMLWalker<PropertiesImpl>() {
        @Override
        protected PropertiesImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new PropertiesImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, PropertiesImpl target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_PROP_STRING)) {
                    target.addProperty(PROPERTY_STRING_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_ID)) {
                    target.addProperty(PROPERTY_ID_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_BOOLEAN)) {
                    target.addProperty(PROPERTY_BOOLEAN_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_INTEGER)) {
                    target.addProperty(PROPERTY_INTEGER_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_DATETIME)) {
                    target.addProperty(PROPERTY_DATETIME_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_DECIMAL)) {
                    target.addProperty(PROPERTY_DECIMAL_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_HTML)) {
                    target.addProperty(PROPERTY_HTML_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_PROP_URI)) {
                    target.addProperty(PROPERTY_URI_PARSER.walk(parser));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<AllowableActionsImpl> ALLOWABLE_ACTIONS_PARSER = new XMLWalker<AllowableActionsImpl>() {
        @Override
        protected AllowableActionsImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new AllowableActionsImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AllowableActionsImpl target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                try {
                    Action action = Action.fromValue(name.getLocalPart());

                    Set<Action> actions = target.getAllowableActions();

                    if (Boolean.TRUE.equals(readBoolean(parser))) {
                        actions.add(action);
                    }

                    return true;
                } catch (IllegalArgumentException e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new XMLStreamException(e.getMessage());
                }
            }

            return false;
        }
    };

    private static final XMLWalker<ChangeEventInfoDataImpl> CHANGE_EVENT_PARSER = new XMLWalker<ChangeEventInfoDataImpl>() {
        @Override
        protected ChangeEventInfoDataImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new ChangeEventInfoDataImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, ChangeEventInfoDataImpl target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_CHANGE_EVENT_TYPE)) {
                    target.setChangeType(readEnum(parser, ChangeType.class));
                    return true;
                }

                if (isTag(name, TAG_CHANGE_EVENT_TIME)) {
                    target.setChangeTime(readDateTime(parser));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<AccessControlListImpl> ACL_PARSER = new XMLWalker<AccessControlListImpl>() {
        @Override
        protected AccessControlListImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new AccessControlListImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AccessControlListImpl target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_ACL_PERMISSISONS)) {
                    target.setAces(addToList(target.getAces(), ACE_PARSER.walk(parser)));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<AccessControlEntryImpl> ACE_PARSER = new XMLWalker<AccessControlEntryImpl>() {
        @Override
        protected AccessControlEntryImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new AccessControlEntryImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AccessControlEntryImpl target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_ACE_PRINCIPAL)) {
                    target.setPrincipal(PRINCIPAL_PARSER.walk(parser));
                    return true;
                }

                if (isTag(name, TAG_ACE_PERMISSIONS)) {
                    target.setPermissions(addToList(target.getPermissions(), readText(parser)));
                    return true;
                }

                if (isTag(name, TAG_ACE_IS_DIRECT)) {
                    target.setDirect(readBoolean(parser));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<AccessControlPrincipalDataImpl> PRINCIPAL_PARSER = new XMLWalker<AccessControlPrincipalDataImpl>() {
        @Override
        protected AccessControlPrincipalDataImpl prepareTarget(XMLStreamReader parser, QName name)
                throws XMLStreamException {
            return new AccessControlPrincipalDataImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, AccessControlPrincipalDataImpl target)
                throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_ACE_PRINCIPAL_ID)) {
                    target.setPrincipalId(readText(parser));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<PolicyIdListImpl> POLICY_IDS_PARSER = new XMLWalker<PolicyIdListImpl>() {
        @Override
        protected PolicyIdListImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new PolicyIdListImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, PolicyIdListImpl target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_POLICY_ID)) {
                    target.setPolicyIds(addToList(target.getPolicyIds(), readText(parser)));
                    return true;
                }
            }

            return false;
        }
    };

    private static final XMLWalker<RenditionDataImpl> RENDITION_PARSER = new XMLWalker<RenditionDataImpl>() {
        @Override
        protected RenditionDataImpl prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new RenditionDataImpl();
        }

        @Override
        protected boolean read(XMLStreamReader parser, QName name, RenditionDataImpl target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_RENDITION_STREAM_ID)) {
                    target.setStreamId(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_MIMETYPE)) {
                    target.setMimeType(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_LENGTH)) {
                    target.setBigLength(readInteger(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_KIND)) {
                    target.setKind(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_TITLE)) {
                    target.setTitle(readText(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_HEIGHT)) {
                    target.setBigHeight(readInteger(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_WIDTH)) {
                    target.setBigWidth(readInteger(parser));
                    return true;
                }

                if (isTag(name, TAG_RENDITION_DOCUMENT_ID)) {
                    target.setRenditionDocumentId(readText(parser));
                    return true;
                }
            }

            return false;
        }
    };

    // ------------------------
    // --- property parsers ---
    // ------------------------

    private static final PropertyXMLWalker<PropertyStringImpl> PROPERTY_STRING_PARSER = new PropertyStringXMLWalker<PropertyStringImpl>() {
        @Override
        protected PropertyStringImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyStringImpl();
        }
    };

    private static final PropertyXMLWalker<PropertyIdImpl> PROPERTY_ID_PARSER = new PropertyStringXMLWalker<PropertyIdImpl>() {
        @Override
        protected PropertyIdImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyIdImpl();
        }
    };

    private static final PropertyXMLWalker<PropertyHtmlImpl> PROPERTY_HTML_PARSER = new PropertyStringXMLWalker<PropertyHtmlImpl>() {
        @Override
        protected PropertyHtmlImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyHtmlImpl();
        }
    };

    private static final PropertyXMLWalker<PropertyUriImpl> PROPERTY_URI_PARSER = new PropertyStringXMLWalker<PropertyUriImpl>() {
        @Override
        protected PropertyUriImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyUriImpl();
        }
    };

    private static final PropertyXMLWalker<PropertyBooleanImpl> PROPERTY_BOOLEAN_PARSER = new PropertyXMLWalker<PropertyBooleanImpl>() {
        @Override
        protected PropertyBooleanImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyBooleanImpl();
        }

        @Override
        protected void addValue(XMLStreamReader parser, PropertyBooleanImpl target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readBoolean(parser)));
        }
    };

    private static final PropertyXMLWalker<PropertyIntegerImpl> PROPERTY_INTEGER_PARSER = new PropertyXMLWalker<PropertyIntegerImpl>() {
        @Override
        protected PropertyIntegerImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyIntegerImpl();
        }

        @Override
        protected void addValue(XMLStreamReader parser, PropertyIntegerImpl target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readInteger(parser)));
        }
    };

    private static final PropertyXMLWalker<PropertyDecimalImpl> PROPERTY_DECIMAL_PARSER = new PropertyXMLWalker<PropertyDecimalImpl>() {
        @Override
        protected PropertyDecimalImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyDecimalImpl();
        }

        @Override
        protected void addValue(XMLStreamReader parser, PropertyDecimalImpl target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readDecimal(parser)));
        }
    };

    private static final PropertyXMLWalker<PropertyDateTimeImpl> PROPERTY_DATETIME_PARSER = new PropertyXMLWalker<PropertyDateTimeImpl>() {
        @Override
        protected PropertyDateTimeImpl createTarget(XMLStreamReader parser, QName name) {
            return new PropertyDateTimeImpl();
        }

        @Override
        protected void addValue(XMLStreamReader parser, PropertyDateTimeImpl target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readDateTime(parser)));
        }
    };

    private abstract static class PropertyXMLWalker<T extends AbstractPropertyData<?>> extends XMLWalker<T> {

        protected abstract T createTarget(XMLStreamReader parser, QName name);

        @Override
        protected T prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            T result = createTarget(parser, name);

            if (parser.getAttributeCount() > 0) {
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    String attr = parser.getAttributeLocalName(i);
                    if (ATTR_PROPERTY_ID.equals(attr)) {
                        result.setId(parser.getAttributeValue(i));
                    } else if (ATTR_PROPERTY_LOCALNAME.equals(attr)) {
                        result.setLocalName(parser.getAttributeValue(i));
                    } else if (ATTR_PROPERTY_DISPLAYNAME.equals(attr)) {
                        result.setDisplayName(parser.getAttributeValue(i));
                    } else if (ATTR_PROPERTY_QUERYNAME.equals(attr)) {
                        result.setQueryName(parser.getAttributeValue(i));
                    }
                }
            }

            return result;
        }

        protected abstract void addValue(XMLStreamReader parser, T target) throws XMLStreamException;

        @Override
        protected boolean read(XMLStreamReader parser, QName name, T target) throws XMLStreamException {
            if (isCmisNamespace(name)) {
                if (isTag(name, TAG_PROPERTY_VALUE)) {
                    addValue(parser, target);
                    return true;
                }
            }

            return false;
        }

    };

    private abstract static class PropertyStringXMLWalker<T extends AbstractPropertyData<String>> extends
            PropertyXMLWalker<T> {
        @Override
        protected void addValue(XMLStreamReader parser, T target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readText(parser)));
        }
    }
}
