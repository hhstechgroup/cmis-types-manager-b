package com.engagepoint.pojo;

/**
 * User: AlexDenisenko
 * Date: 09.01.14
 * Time: 11:25
 */

import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSession.class);
    private List<Repository> repositories;
    private Session session;

    public ClientSession(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public Session createSession(int index) {
        session = repositories.get(index).createSession();
        return getSession();
    }

    public Session getSession() {
        return session;
    }

}
