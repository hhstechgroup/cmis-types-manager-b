package com.engagepoint.ejb;

import com.engagepoint.exception.CmisException;
import com.engagepoint.pojo.Type;
import com.engagepoint.pojo.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;

/**
 * User: victor.klymenko
 * Date: 1/3/14
 * Time: 3:59 PM
 */
public class TestService {
    private UserInfo mockedUserInfo;
    private Service mockedService;
    private Type type;

    @Before
    public void before() throws CmisException {
        mockedService = mock(Service.class);
        mockedService.setConnection(new CmisConnection());
        mockedUserInfo = mock(UserInfo.class);
        mockedUserInfo.setUsername("test");
        mockedUserInfo.setPassword("test");
        mockedUserInfo.setUrl("http://lab16:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11");

        type = new Type();
        type.setId("test");
        type.setLocalName("test");
        type.setLocalNamespace("test");
        type.setDisplayName("test");
        type.setQueryName("test");
        type.setDescription("test");
        type.setBaseTypeId("CrossReferenceType");
        type.setParentTypeId("CrossReferenceType");
        type.setCreatable(true);
        type.setFileable(true);
        type.setQueryable(true);
        type.setFulltextIndexed(true);
        type.setIncludedInSupertypeQuery(true);
        type.setControllablePolicy(true);
        type.setControllableAcl(true);
    }

    @Test
    public void testCreateType() throws CmisException {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testDeleteType() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testImportTypeFromXml() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testImportTypeFromJson() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testExportTypeToXML() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testExportTypeToJSON() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testGetAllTypes() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testGetTypeDefinitionById() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testGetRepositories() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testGetDefaultRepository() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testIsUserExists() throws CmisException {
        Assert.fail("Not yet implemented");
    }
}