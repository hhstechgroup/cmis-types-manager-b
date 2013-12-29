package com.engagepoint.bean;

import com.engagepoint.service.TypeProxy;
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

    public TypeProxy getType() {
        return typeProxy;
    }

    public void setType(TypeProxy typeProxy) {
        this.typeProxy = typeProxy;
    }
}
