package com.engagepoint.ejb;

import com.engagepoint.exception.CmisException;
import com.engagepoint.pojo.UserInfo;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: victor.klymenko
 * Date: 1/3/14
 * Time: 12:31 PM
 */
public class TestCmisConnection {
    private UserInfo userInfoMock;
    private SessionFactory sessionFactoryMock;
    private Session mockedSession;
    private List mockedRepositoryList;
    private CmisConnection cmisConnection = new CmisConnection();

    @Before
    public void beforeRun() {
        userInfoMock = mock(UserInfo.class);
        userInfoMock.setUsername("test");
        userInfoMock.setPassword("test");
        userInfoMock.setUrl("http://lab16:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11");
        sessionFactoryMock = mock(SessionFactory.class);
        mockedSession = mock(Session.class);
        mockedRepositoryList = mock(List.class);
        cmisConnection.setSessionFactory(sessionFactoryMock);
    }

    @Test
    public void testGetSession() throws CmisException {
        when(sessionFactoryMock.createSession(userInfoMock.getAtomPubParameters())).thenReturn(mockedSession);
        Assert.assertEquals("Session is wrong", mockedSession, cmisConnection.getSession(userInfoMock));
    }

    @Test(expected = NullPointerException.class)
    public void testGetRepositories() throws CmisException {
        when(sessionFactoryMock.getRepositories(userInfoMock.getAtomPubParameters())).thenReturn(mockedRepositoryList);
        Assert.assertEquals("Repository list is wrong", mockedRepositoryList, cmisConnection.getRepositories(userInfoMock));
    }
}
