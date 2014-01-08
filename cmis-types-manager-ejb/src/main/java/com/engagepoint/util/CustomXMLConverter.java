package com.engagepoint.util;

import com.engagepoint.ejb.Service;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.data.*;
import org.apache.chemistry.opencmis.commons.definitions.*;
import org.apache.chemistry.opencmis.commons.enums.*;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.XMLConstants;
import org.apache.chemistry.opencmis.commons.impl.XMLConverter;
import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.apache.chemistry.opencmis.commons.impl.XMLWalker;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.apache.chemistry.opencmis.commons.impl.XMLConstants.*;

public class CustomXMLConverter {
    private static final String TAG_TYPE_DEFINITIONS = "typeDefinitions";
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private CustomXMLConverter() {
    }

    public static List<AbstractTypeDefinition> convertTypeDefinition(XMLStreamReader parser) throws XMLStreamException {
        return parser.getNamespaceURI().equals(NAMESPACE_APACHE_CHEMISTRY) ?
                TYPE_TREE_DEF_PARSER.walk(parser) : Arrays.asList(TYPE_DEF_PARSER.walk(parser));
    }

    private static final XMLWalker<List<AbstractTypeDefinition>> TYPE_TREE_DEF_PARSER = new XMLWalker<List<AbstractTypeDefinition>>() {
        @Override
        protected List<AbstractTypeDefinition> prepareTarget(XMLStreamReader parser, QName name) throws XMLStreamException {
            return new ArrayList<AbstractTypeDefinition>();
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
    }

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

    public static void writeTypeDefinitions(Session session, XMLStreamWriter writer, TypeDefinition source,
                                            List<Tree<ObjectType>> typeDescendants) throws XMLStreamException {
        if (source == null) {
            return;
        }
        if (source.getBaseTypeId() == BaseTypeId.CMIS_ITEM) {
            LOGGER.warn("Receiver only understands CMIS 1.0. It may not able to handle an Item type definition.");
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_SECONDARY) {
            LOGGER.warn("Receiver only understands CMIS 1.0. It may not able to handle a Secondary type definition.");
        }
        if (typeDescendants != null) {
            writer.writeStartElement(XMLConstants.NAMESPACE_APACHE_CHEMISTRY, TAG_TYPE_DEFINITIONS);
            writer.writeNamespace(PREFIX_RESTATOM, XMLConstants.NAMESPACE_RESTATOM);
            writer.writeNamespace(PREFIX_XSI, XMLConstants.NAMESPACE_XSI);
            writer.writeNamespace(PREFIX_APACHE_CHEMISTY, XMLConstants.NAMESPACE_APACHE_CHEMISTRY);
            writer.writeNamespace(PREFIX_CMIS, XMLConstants.NAMESPACE_CMIS);
            writeTypeDefinition(session, writer, CmisVersion.CMIS_1_1, XMLConstants.NAMESPACE_CMIS, source, typeDescendants, true);
            XMLConverter.writeExtensions(writer, source);
            writer.writeEndElement();
        } else {
            writeTypeDefinition(session, writer, CmisVersion.CMIS_1_1, XMLConstants.NAMESPACE_CMIS, source, typeDescendants, false);
        }
    }

    private static void writeTypeDefinition(Session session, XMLStreamWriter writer, CmisVersion cmisVersion, String namespace,
                                           TypeDefinition source, List<Tree<ObjectType>> typeDescendants,
                                           boolean includeChildren) throws XMLStreamException {
        writer.writeStartElement(namespace, TAG_TYPE);
        if (!includeChildren) {
            writer.writeNamespace(XMLConstants.PREFIX_XSI, XMLConstants.NAMESPACE_XSI);
            writer.writeNamespace(PREFIX_CMIS, XMLConstants.NAMESPACE_CMIS);
        }
        if (source.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_DOCUMENT_TYPE);
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_FOLDER) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_FOLDER_TYPE);
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_RELATIONSHIP) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_RELATIONSHIP_TYPE);
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_POLICY) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_POLICY_TYPE);
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_ITEM) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_ITEM_TYPE);
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_SECONDARY) {
            writer.writeAttribute(PREFIX_XSI, NAMESPACE_XSI, "type", PREFIX_CMIS + ":" + ATTR_SECONDARY_TYPE);
        } else {
            throw new CmisRuntimeException("Type definition has no base type id!");
        }
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_ID, source.getId());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_LOCALNAME, source.getLocalName());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_LOCALNAMESPACE, source.getLocalNamespace());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_DISPLAYNAME, source.getDisplayName());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_QUERYNAME, source.getQueryName());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_DESCRIPTION, source.getDescription());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_BASE_ID, source.getBaseTypeId());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_PARENT_ID, source.getParentTypeId());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_CREATABLE, source.isCreatable());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_FILEABLE, source.isFileable());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_QUERYABLE, source.isQueryable());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_FULLTEXT_INDEXED, source.isFulltextIndexed());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_INCLUDE_IN_SUPERTYPE_QUERY,
                source.isIncludedInSupertypeQuery());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_CONTROLABLE_POLICY, source.isControllablePolicy());
        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_CONTROLABLE_ACL, source.isControllableAcl());

        if (cmisVersion != CmisVersion.CMIS_1_0 && source.getTypeMutability() != null) {
            TypeMutability tm = source.getTypeMutability();
            writer.writeStartElement(PREFIX_CMIS, TAG_TYPE_TYPE_MUTABILITY, NAMESPACE_CMIS);
            XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_TYPE_MUTABILITY_CREATE, tm.canCreate());
            XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_TYPE_MUTABILITY_UPDATE, tm.canUpdate());
            XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_TYPE_MUTABILITY_DELETE, tm.canDelete());
            XMLConverter.writeExtensions(writer, tm);
            writer.writeEndElement();
        }
        if (source.getPropertyDefinitions() != null) {
            for (PropertyDefinition<?> pd : CMISTypeUtils.getCorrectPropertyMapWithoutChangeTypeDefinition(session, source).values()) {
                XMLConverter.writePropertyDefinition(writer, cmisVersion, pd);
            }
        }

        if (source instanceof DocumentTypeDefinition) {
            DocumentTypeDefinition docDef = (DocumentTypeDefinition) source;
            XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_VERSIONABLE, docDef.isVersionable());
            XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_CONTENTSTREAM_ALLOWED,
                    docDef.getContentStreamAllowed());
        }

        if (source instanceof RelationshipTypeDefinition) {
            RelationshipTypeDefinition relDef = (RelationshipTypeDefinition) source;
            if (relDef.getAllowedSourceTypeIds() != null) {
                for (String id : relDef.getAllowedSourceTypeIds()) {
                    if (id != null) {
                        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_ALLOWED_SOURCE_TYPES, id);
                    }
                }
            }
            if (relDef.getAllowedTargetTypeIds() != null) {
                for (String id : relDef.getAllowedTargetTypeIds()) {
                    if (id != null) {
                        XMLUtils.write(writer, PREFIX_CMIS, NAMESPACE_CMIS, TAG_TYPE_ALLOWED_TARGET_TYPES, id);
                    }
                }
            }
        }
        XMLConverter.writeExtensions(writer, source);
        writer.writeEndElement();

        if (typeDescendants != null) {
            for (Tree<ObjectType> node : typeDescendants) {
                writeTypeDefinition(session, writer, CmisVersion.CMIS_1_1, XMLConstants.NAMESPACE_CMIS, node.getItem(),
                        node.getChildren(), true);
            }
        }
    }

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

    }

    private abstract static class PropertyStringXMLWalker<T extends AbstractPropertyData<String>> extends
            PropertyXMLWalker<T> {
        @Override
        protected void addValue(XMLStreamReader parser, T target) throws XMLStreamException {
            target.setValues(addToList(target.getValues(), readText(parser)));
        }
    }
}
