package com.engagepoint.services;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.*;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.CmisVersion;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.XMLConstants;
import org.apache.chemistry.opencmis.commons.impl.XMLConverter;
import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;

import static org.apache.chemistry.opencmis.commons.impl.XMLConstants.*;

/**
 * User: AlexDenisenko
 * Date: 13.12.13
 * Time: 13:34
 */
public class CustomTypeUtils {
    private static final String TAG_TYPE_DEFINITIONS = "typeDefinitions";
    private static final Logger LOG = LoggerFactory.getLogger(CustomTypeUtils.class);

    public static void writeToXML(Session session, TypeDefinition type, OutputStream stream,
                                  List<Tree<ObjectType>> typeDescendants) throws XMLStreamException {
        if (type == null) {
            throw new IllegalArgumentException("Type must be set!");
        }
        if (stream == null) {
            throw new IllegalArgumentException("Output stream must be set!");
        }
        XMLStreamWriter writer = XMLUtils.createWriter(stream);
        XMLUtils.startXmlDocument(writer);
        writeTypeTreeDefinition(session, writer, CmisVersion.CMIS_1_1, XMLConstants.NAMESPACE_CMIS, type, typeDescendants);
        XMLUtils.endXmlDocument(writer);
        writer.close();
    }

    public static void writeTypeTreeDefinition(Session session, XMLStreamWriter writer, CmisVersion cmisVersion, String namespace,
                                               TypeDefinition source,
                                               List<Tree<ObjectType>> typeDescendants) throws XMLStreamException {
        if (source == null) {
            return;
        }

        if (source.getBaseTypeId() == BaseTypeId.CMIS_ITEM) {
            LOG.warn("Receiver only understands CMIS 1.0. It may not able to handle an Item type definition.");
        } else if (source.getBaseTypeId() == BaseTypeId.CMIS_SECONDARY) {
            LOG.warn("Receiver only understands CMIS 1.0. It may not able to handle a Secondary type definition.");
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

    public static void writeTypeDefinition(Session session, XMLStreamWriter writer, CmisVersion cmisVersion, String namespace,
                                           TypeDefinition source, List<Tree<ObjectType>> typeDescendants,
                                           boolean includeChildren) throws XMLStreamException {
        writer.writeStartElement(namespace, TAG_TYPE);
        if (!includeChildren) {
            writer.writeNamespace(XMLConstants.PREFIX_XSI, XMLConstants.NAMESPACE_XSI);
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
            for (PropertyDefinition<?> pd : getCorrectPropertyMapWithoutChangeTypeDefinition(session, source).values()) {
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

    public static List<AbstractTypeDefinition> readFromXML(InputStream stream) throws XMLStreamException {
        if (stream == null) {
            throw new IllegalArgumentException("Input stream must be set!");
        }
        XMLStreamReader parser = XMLUtils.createParser(stream);
        if (!XMLUtils.findNextStartElemenet(parser)) {
            return null;
        }
        List<AbstractTypeDefinition> definitionList = CustomXMLConverter.convertTypeDefinitionFromTree(parser);
        return definitionList;
    }

    public static List<TypeDefinition> readFromJSON(InputStream stream) throws IOException, JSONParseException {
        if (stream == null) {
            throw new IllegalArgumentException("Input stream must be set!");
        }

        JSONParser parser = new JSONParser();
        Object json = parser.parse(new InputStreamReader(stream, "UTF-8"));

        if (!(json instanceof Map)) {
            throw new CmisRuntimeException("Invalid stream! Not a type definition!");
        }

        return CustomJSONConverter.convertTypeDefinition((Map<String, Object>) json);
    }

    public static void writeToJSON(Session session, TypeDefinition type, OutputStream stream,
                                   List<Tree<ObjectType>> typeDescendants) throws IOException {
        if (type == null) {
            throw new IllegalArgumentException("Type must be set!");
        }
        if (stream == null) {
            throw new IllegalArgumentException("Output stream must be set!");
        }

        Writer writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        CustomJSONConverter.convert(session, type, typeDescendants).writeJSONString(writer);
        writer.flush();
    }

    public static Map<String, PropertyDefinition<?>> getCorrectPropertyMapWithoutChangeTypeDefinition(Session session, TypeDefinition typeDefinition){
        Map<String, PropertyDefinition<?>> customPropertydefinition = new HashMap<String, PropertyDefinition<?>>(typeDefinition.getPropertyDefinitions());
        doCorrectPropertyMap(session, typeDefinition, customPropertydefinition);
        return customPropertydefinition;
    }

    public static TypeDefinition getCorrectTypeDefinition(Session session, TypeDefinition typeDefinition) {
        Map<String, PropertyDefinition<?>> propertyDefinitions = typeDefinition.getPropertyDefinitions();
        doCorrectPropertyMap(session, typeDefinition, propertyDefinitions);
        return typeDefinition;
    }

    private static void doCorrectPropertyMap(Session session, TypeDefinition typeDefinition, Map<String, PropertyDefinition<?>> propertyDefinitions){
        String parentTypeId = typeDefinition.getParentTypeId();
        if (parentTypeId != null) {
            Map<String, PropertyDefinition<?>> propertyOfAllParentTypes = getPropertyOfAllParentTypes(session, parentTypeId);
            for (String key : getKeyList(propertyDefinitions.keySet())) {
                if (propertyOfAllParentTypes.containsKey(key)) {
                    propertyDefinitions.remove(key);
                }
            }
        }
    }

    private static Map<String, PropertyDefinition<?>> getPropertyOfAllParentTypes(Session session, String parentTypeId) {
        return  session.getTypeDefinition(parentTypeId).getPropertyDefinitions();
    }

    private static List<String> getKeyList(Set<String> stringSet) {
        List<String> list = new ArrayList<String>();
        for (String s : stringSet) {
            list.add(s);
        }
        return list;
    }

}
