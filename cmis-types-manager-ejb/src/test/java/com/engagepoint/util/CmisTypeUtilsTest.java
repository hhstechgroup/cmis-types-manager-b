package com.engagepoint.util;

import com.engagepoint.exception.AppException;
import com.engagepoint.pojo.TypeDefinitionImpl;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CmisTypeUtils.class, XMLUtils.class, CustomXMLConverter.class})
public class CmisTypeUtilsTest {
    private InputStream xmlStream;
    private InputStream jsonStream;
    private List<AbstractTypeDefinition> xmlList;
    private List<TypeDefinition> jsonList;
    private TypeDefinitionImpl type;

    @Before
    public void before() {
        xmlList = new ArrayList<AbstractTypeDefinition>();
        jsonList = new ArrayList<TypeDefinition>();
        type = new TypeDefinitionImpl();
        type.setId("test");
        type.setLocalName("test");
        type.setLocalNamespace("test");
        type.setDisplayName("test");
        type.setQueryName("test");
        type.setDescription("test");
        type.setBaseTypeId(BaseTypeId.CMIS_DOCUMENT);
        type.setParentTypeId("CrossReferenceType");
    }

    @Test
    public void testReadFromXML() throws AppException {
        Assert.assertNotNull(getClass().getResource("/files/testTreeTypeXML.xml"));
        xmlStream = getClass().getResourceAsStream("/files/testTreeTypeXML.xml");
        Assert.assertNotNull(xmlStream);
        mockStatic(CmisTypeUtils.class);
        when(CmisTypeUtils.readFromXML(xmlStream)).thenReturn(xmlList);
        Assert.assertEquals(xmlList, CmisTypeUtils.readFromXML(xmlStream));
        try {
            xmlStream.close();
        } catch (IOException e) {
            Assert.fail("IOException : " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testWriteToXML() throws FileNotFoundException, AppException {
//        OutputStream stream1 = new FileOutputStream(new File(getClass().getResource("/files/testTreeTypeXML.xml").getFile()));
//        Session session = mock(Session.class);
//        CmisTypeUtils.writeToXML(session, type, stream1, session.getTypeDescendants(null, 0, false));
//        try {
//            stream1.close();
//        } catch (IOException e) {
//            Assert.fail("IOException : " + e.getMessage());
//        }
    }

    @Test
    public void testReadFromJSON() throws AppException {
        Assert.assertNotNull(getClass().getResource("/files/testTypeJSON.json"));
        jsonStream = getClass().getResourceAsStream("/files/testTypeJSON.json");
        Assert.assertNotNull(jsonStream);
        mockStatic(CmisTypeUtils.class);
        when(CmisTypeUtils.readFromJSON(jsonStream)).thenReturn(jsonList);
        Assert.assertEquals(jsonList, CmisTypeUtils.readFromJSON(jsonStream));
        try {
            jsonStream.close();
        } catch (IOException e) {
            Assert.fail("IOException : " + e.getMessage());
        }
    }

    @Test
    public void testGetCorrectPropertyMapWithoutChangeTypeDefinition(){
        Session session = mock(Session.class);
        TypeDefinition typeDefinition = mock(TypeDefinition.class);
        Assert.assertNotNull(CmisTypeUtils.getCorrectPropertyMapWithoutChangeTypeDefinition(session, typeDefinition));
    }

    @Test
    public void testGetCorrectTypeDefinition(){
        Session session = mock(Session.class);
        TypeDefinition typeDefinition = mock(TypeDefinition.class);
        Assert.assertNotNull(CmisTypeUtils.getCorrectTypeDefinition(session, typeDefinition));
    }
}