package com.engagepoint.util;

import org.apache.chemistry.opencmis.commons.impl.XMLUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CmisTypeUtils.class, XMLUtils.class})
public class CustomXMLConverterTest {

    private XMLStreamReader mockedXmlStreamReader;

    @Before
    public void beforeRun() throws XMLStreamException {
        PowerMockito.mockStatic(XMLUtils.class);


    }

    @Test
    public void testConvertTypeDefinition() throws XMLStreamException{

        Assert.assertNotNull(getClass().getResourceAsStream("/files/rel1.xml"));
        mockedXmlStreamReader = XMLUtils.createParser(getClass().getResourceAsStream("/files/rel1.xml"));


        PowerMockito.mockStatic(CustomXMLConverter.class);
        Assert.assertNotNull("Error", CustomXMLConverter.convertTypeDefinition(mockedXmlStreamReader));
        Assert.assertTrue("",CustomXMLConverter.convertTypeDefinition(mockedXmlStreamReader).isEmpty());
    }

}

