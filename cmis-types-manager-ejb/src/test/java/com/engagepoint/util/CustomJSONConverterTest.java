package com.engagepoint.util;

import com.engagepoint.ejb.CmisConnection;
import com.engagepoint.pojo.Type;
import com.engagepoint.pojo.UserInfo;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.net.www.content.text.plain;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
public class CustomJSONConverterTest {
    public Type type;
    public List<TypeDefinition> typeDefinitionList;
    public Map<String,Object> map;

    @Before
    public void beforeRun() {
    }

    @Test
    public void testConvertTypeDefinitions(){
        Assert.assertNotNull(CustomJSONConverter.convertTypeDefinitions(null));
        Assert.assertNotNull(CustomJSONConverter.convertTypeDefinitions(map));
    }
    @Test
    public void testConvert(){

    }
    @Test
    public void testWriteTypeDefinitions(){

    }
    @Test
    public void testWriteChildrenType(){

    }
}
