package com.engagepoint.view;

import com.engagepoint.services.TypeProxy;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * User: michael.vakulik
 * Date: 12/2/13
 * Time: 1:44 PM
 */
@ManagedBean
@SessionScoped
public class SessionStateBean implements Serializable {
    private TypeProxy typeProxy;
    private RepositoryInfo repositoryInfo;

    public TypeProxy getTypeProxy() {
        return typeProxy;
    }

    public void setTypeProxy(TypeProxy typeProxy) {
        this.typeProxy = typeProxy;
    }

    public RepositoryInfo getRepositoryInfo() {
        return repositoryInfo;
    }

    public void setRepositoryInfo(RepositoryInfo repositoryInfo) {
        this.repositoryInfo = repositoryInfo;
    }

    public void destroyReposytoryInfo(){
        repositoryInfo = null;
    }
}
