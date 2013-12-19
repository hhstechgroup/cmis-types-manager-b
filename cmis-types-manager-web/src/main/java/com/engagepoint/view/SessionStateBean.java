package com.engagepoint.view;

import com.engagepoint.services.TypeProxy;

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

    public TypeProxy getTypeProxy() {
        return typeProxy;
    }

    public void setTypeProxy(TypeProxy typeProxy) {
        this.typeProxy = typeProxy;
    }
}
