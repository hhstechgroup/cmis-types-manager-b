package com.engagepoint.util;

import com.engagepoint.exception.AppException;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
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

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;

/**
 * User: AlexDenisenko
 * Date: 13.12.13
 * Time: 13:34
 */
public final class CMISTypeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CMISTypeUtils.class);
    public static final String CHARSET_NAME = "UTF-8";

    private CMISTypeUtils() {
    }

    public static List<AbstractTypeDefinition> readFromXML(InputStream stream) throws AppException {
        notNull(stream);
        try {
            XMLStreamReader parser = XMLUtils.createParser(stream);
            isTrue(XMLUtils.findNextStartElemenet(parser), "Can't find next start element");
            return CustomXMLConverter.convertTypeDefinition(parser);
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
    }

    public static void writeToXML(Session session, TypeDefinition type, OutputStream stream,
                                  List<Tree<ObjectType>> typeDescendants) throws AppException {
        throwIfNull(type, stream);
        try {
            XMLStreamWriter writer = XMLUtils.createWriter(stream);
            XMLUtils.startXmlDocument(writer);
            CustomXMLConverter.writeTypeDefinitions(session, writer, type, typeDescendants);
            XMLUtils.endXmlDocument(writer);
            writer.close();
        } catch (XMLStreamException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
    }

    public static List<TypeDefinition> readFromJSON(InputStream stream) throws AppException {
        notNull(stream);
        try {
            JSONParser parser = new JSONParser();
            Object json = parser.parse(new InputStreamReader(stream, CHARSET_NAME));
            if (!(json instanceof Map)) {
                throw new AppException("Invalid stream! Not a type definition!");
            }
            return CustomJSONConverter.convertTypeDefinitions((Map<String, Object>) json);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        } catch (JSONParseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
    }

    public static void writeToJSON(Session session, TypeDefinition type, OutputStream stream,
                                   List<Tree<ObjectType>> typeDescendants) throws AppException {
        throwIfNull(type, stream);
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(stream, CHARSET_NAME));
            CustomJSONConverter.writeTypeDefinitions(session, type, typeDescendants).writeJSONString(writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
    }

    public static Map<String, PropertyDefinition<?>> getCorrectPropertyMapWithoutChangeTypeDefinition(Session session, TypeDefinition typeDefinition){
        Map<String, PropertyDefinition<?>> correctDef = new HashMap<String, PropertyDefinition<?>>(typeDefinition.getPropertyDefinitions());
        doCorrectPropertyMap(session, typeDefinition, correctDef);
        return correctDef;
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

    private static void throwIfNull(TypeDefinition type, OutputStream stream) {
        notNull(type, "Type must be set!");
        notNull(stream, "Output stream must be set!");
    }

}
