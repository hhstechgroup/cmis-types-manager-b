package com.engagepoint.ejb;

import com.engagepoint.exception.AppException;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.util.List;
import java.util.Map;

/**
 * User: AlexDenisenko
 * Date: 29.11.13
 * Time: 11:37
 */
@Singleton
@LocalBean
public class CmisConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(CmisConnection.class);
    private SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    public List<Repository> getRepositories(Map <String, String> parameters) throws AppException {
        List<Repository> repositoryList;
        try {
            repositoryList = sessionFactory.getRepositories(parameters);
        } catch (CmisBaseException e) {
            LOGGER.error(e.getMessage(), e);
            throw new AppException(e.getMessage());
        }
        return repositoryList;
    }

    @Deprecated
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
