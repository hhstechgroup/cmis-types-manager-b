package com.engagepoint.util;

import com.engagepoint.pojo.TypeDefinitionImpl;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CmisTypeUtils.class, XMLUtils.class, CustomXMLConverter.class})
public class CustomXMLConverterTest {

    private XMLStreamReader mockedXmlStreamReader;
    private List<AbstractTypeDefinition> testAbstractTypeDefinitionList;
    private Session mockedSession;
    private TypeDefinitionImpl typeDefinition;
    private List<Tree<ObjectType>> typeDescendants;
    private XMLStreamWriter mockedWriter;


    @Before
    public void beforeRun() {
        mockedSession = mock(Session.class);
        typeDefinition = new TypeDefinitionImpl();
        typeDefinition.setId("test");
        typeDefinition.setLocalName("test");
        typeDefinition.setLocalNamespace("test");
        typeDefinition.setDisplayName("test");
        typeDefinition.setQueryName("test");
        typeDefinition.setDescription("test");
        typeDefinition.setBaseTypeId(BaseTypeId.CMIS_DOCUMENT);
        mockedWriter = mock(XMLStreamWriter.class);
    }

    @Test
    public void testConvertTypeDefinitionTree() {
        String[] testTypeIdList = {"rel1", "rel11", "rel12"};

        try {
            Assert.assertNotNull(getClass().getResourceAsStream("/files/testTreeTypeXML.xml"));
            mockedXmlStreamReader = XMLUtils.createParser(getClass().getResourceAsStream("/files/testTreeTypeXML.xml"));
            Assert.assertNotNull(mockedXmlStreamReader);
            Assert.assertTrue(XMLUtils.findNextStartElemenet(mockedXmlStreamReader));
            Assert.assertEquals("Uncorrected NamespaceURI", "http://chemistry.apache.org/", mockedXmlStreamReader.getNamespaceURI());
            testAbstractTypeDefinitionList = CustomXMLConverter.convertTypeDefinition(mockedXmlStreamReader);
        } catch (XMLStreamException e) {
            Assert.fail("XMLStreamException : " + e.getMessage());
        }

        Assert.assertEquals("Uncorrected list length", testAbstractTypeDefinitionList.size(), 3);
        int index = 0;
        for (AbstractTypeDefinition typeDefinition : testAbstractTypeDefinitionList) {
            Assert.assertEquals("Type Id not equals", typeDefinition.getId(), testTypeIdList[index++]);
        }
    }

    @Test
    public void testConvertTypeDefinition() {

        Assert.assertNotNull(getClass().getResourceAsStream("/files/testTypeXML.xml"));
        try {
            mockedXmlStreamReader = XMLUtils.createParser(getClass().getResourceAsStream("/files/testTypeXML.xml"));
            Assert.assertNotNull(mockedXmlStreamReader);
            Assert.assertTrue(XMLUtils.findNextStartElemenet(mockedXmlStreamReader));
            Assert.assertNotEquals("Uncorrected NamespaceURI", "http://chemistry.apache.org/", mockedXmlStreamReader.getNamespaceURI());
            testAbstractTypeDefinitionList = CustomXMLConverter.convertTypeDefinition(mockedXmlStreamReader);
        } catch (XMLStreamException e) {
            Assert.fail("XMLStreamException : " + e.getMessage());
        }

        Assert.assertEquals("Uncorrected list length", testAbstractTypeDefinitionList.size(), 1);
        for (AbstractTypeDefinition typeDefinition : testAbstractTypeDefinitionList) {
            Assert.assertEquals("Type Id not equals", typeDefinition.getId(), "cmis:folder");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testWriteTypeDefinitions() {
        try {
            typeDescendants = mock(List.class);
            CustomXMLConverter.writeTypeDefinitions(mockedSession, mockedWriter, typeDefinition, typeDescendants);
        } catch (XMLStreamException e) {
            Assert.fail("XMLStreamException : " + e.getMessage());
        }
    }

}