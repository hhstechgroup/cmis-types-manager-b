package com.engagepoint.util;


import com.engagepoint.ejb.CmisConnection;
import com.engagepoint.pojo.TypeDefinitionImpl;
import com.engagepoint.pojo.UserInfo;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;


/**
 * User: victor.klymenko
 * Date: 1/8/14
 * Time: 1:29 PM
 */
public class CustomJSONConverterTest {
    private UserInfo userInfoMock;
    private SessionFactory sessionFactoryMock;
    private Session mockedSession;
    private CmisConnection cmisConnection = new CmisConnection();
    private TypeDefinitionImpl typeDefinition;
    private List mockedRepositoryList;
    private List<Tree<ObjectType>> typeDescendants;
    private Map<String, Object> mockedMap;
    private static final String CHARSET_NAME = "UTF-8";
    private Object json;
    private List<String> expectedList;
    private List<String> actualList;

    @Before
    public void beforeRun() {
        expectedList = new ArrayList<String>();
        actualList = new ArrayList<String>();
        expectedList.add("rel1");
        expectedList.add("rel11");
        expectedList.add("rel12");
        typeDefinition = new TypeDefinitionImpl();
        typeDefinition.setId("MyType");
        typeDefinition.setDisplayName("MyTypeName");
        userInfoMock = new UserInfo();
        userInfoMock.setUsername("test");
        userInfoMock.setPassword("test");
        userInfoMock.setUrl("http://lab16:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11");
        mockedRepositoryList = mock(List.class);
        sessionFactoryMock = mock(SessionFactory.class);
        mockedSession = mock(Session.class);
        cmisConnection.setSessionFactory(sessionFactoryMock);
        try {
            InputStream stream = getClass().getResourceAsStream("/files/testTypeJSON.json");
            JSONParser parser = new JSONParser();
            json = parser.parse(new InputStreamReader(stream, CHARSET_NAME));
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONParseException e) {
            e.printStackTrace();
        }
        mockedMap = (Map<String, Object>) json;
    }

    @Test
    public void getJSONFromTypeInByteArrayIsTestFileExists() {
        Assert.assertNotNull(getClass().getResource("/files/testTypeJSON.json"));

    }

    @Test
    public void testConvertTypeDefinitionsNotNull() {
        Assert.assertNotNull(CustomJSONConverter.convertTypeDefinitions(null));
    }

    @Test
    public void testConvertTypeDefinitionsHasMap() {
        Assert.assertNotNull(CustomJSONConverter.convertTypeDefinitions(mockedMap));
    }

    @Test
    public void testConvertTypeDefinitionsEqualsKey() {
        for (Map.Entry<String, Object> entry : mockedMap.entrySet()) {
            actualList.add(entry.getKey());
        }
        Assert.assertEquals(expectedList, actualList);

    }

    @Test
    public void testConvert() {
        Assert.assertNotNull(CustomJSONConverter.convert(typeDefinition, mockedSession));
    }

    @Test
    public void testWriteTypeDefinitions() {
        Assert.assertNotNull(CustomJSONConverter.writeTypeDefinitions(mockedSession, typeDefinition, typeDescendants));
    }

}
