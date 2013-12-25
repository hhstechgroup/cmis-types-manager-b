package com.engagepoint.view;

import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/25/13
 * Time: 3:11 PM
 */

@ManagedBean
@RequestScoped
public class RepositoryInfoBean implements Serializable{

    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;

    public RepositoryInfo repositoryInfo;

    @PostConstruct
    public void init() {
       repositoryInfo = sessionStateBean.getRepositoryInfo();
    }

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
    }
}


