package com.engagepoint.bean;

import com.engagepoint.pojo.TypeProxy;

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
public class SelectedTypeHolderBean implements Serializable {
    private TypeProxy typeProxy;

    public TypeProxy getType() {
        return typeProxy;
    }

    public void setType(TypeProxy typeProxy) {
        this.typeProxy = typeProxy;
    }

    public void clearType() {
        typeProxy = null;
    }
}
