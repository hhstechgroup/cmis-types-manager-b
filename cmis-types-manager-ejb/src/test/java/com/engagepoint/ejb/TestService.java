package com.engagepoint.ejb;

import com.engagepoint.exception.AppException;
import com.engagepoint.pojo.Type;
import com.engagepoint.pojo.UserInfo;
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
    public void before() throws AppException {
        mockedService = mock(Service.class);
        mockedService.setConnection(new CmisConnection());
        mockedUserInfo = mock(UserInfo.class);
        mockedUserInfo.setUsername("test");
        mockedUserInfo.setPassword("test");
        mockedUserInfo.setUrl("http://localhost:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11");

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
    public void testCreateType() throws AppException {

    }

    @Test
    public void testDeleteType() {

    }

    @Test
    public void testImportTypeFromXml() {

    }

    @Test
    public void testImportTypeFromJson() {
    }

    @Test
    public void testExportTypeToXML() {

    }

    @Test
    public void testExportTypeToJSON() {

    }

    @Test
    public void testGetAllTypes() {

    }

    @Test
    public void testGetTypeDefinitionById() {

    }

    @Test
    public void testGetRepositories() {

    }

    @Test
    public void testGetDefaultRepository() {

    }

    @Test
    public void testIsUserExists() throws AppException {

    }
}