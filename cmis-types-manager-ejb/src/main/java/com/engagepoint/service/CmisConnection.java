package com.engagepoint.service;

import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * User: AlexDenisenko
 * Date: 29.11.13
 * Time: 11:37
 */
@Singleton
@LocalBean
public class CmisConnection {
    private SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
