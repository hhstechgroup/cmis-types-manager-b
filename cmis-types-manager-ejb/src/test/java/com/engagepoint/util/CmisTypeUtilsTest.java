package com.engagepoint.util;

import com.engagepoint.exception.AppException;
import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractTypeDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CmisTypeUtils.class, XMLUtils.class, CustomXMLConverter.class})
public class CmisTypeUtilsTest {
    private InputStream stream;
    private List<AbstractTypeDefinition> list;

    @Before
    public void before() {
        list = new ArrayList<AbstractTypeDefinition>();
//        mockStatic(XMLUtils.class);
    }

    @Test
    public void assertResourceXmlFile() {
        Assert.assertNotNull(getClass().getResource("/files/rel1.xml"));
    }

    @Test
    public void verifyGetStream() {
        stream = getClass().getResourceAsStream("/files/rel1.xml");
        Assert.assertNotNull(stream);
    }

    @Test
    public void assertEqualsList() throws AppException {
        mockStatic(CmisTypeUtils.class);
        when(CmisTypeUtils.readFromXML(stream)).thenReturn(list);
        Assert.assertEquals(list, CmisTypeUtils.readFromXML(stream));
    }

    @Test
    public void verifyCreateParserExecution() throws AppException {
        mockStatic(XMLUtils.class);
        verify(XMLUtils.class, times(1));
    }

    @Test
    public void verifyConvertTypeDefinitionExecution() throws AppException {
        mockStatic(CustomXMLConverter.class);
        verify(CustomXMLConverter.class, times(1));
    }

    @Test
    public void verifyReadFromXMLExecution() {
        mockStatic(CmisTypeUtils.class);
        verify(CmisTypeUtils.class, times(1));
    }

    @Test
    public void testReadFromXML(){

    }
}
