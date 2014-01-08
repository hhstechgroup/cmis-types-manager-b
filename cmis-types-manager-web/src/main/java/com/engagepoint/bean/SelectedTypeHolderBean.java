package com.engagepoint.bean;

import com.engagepoint.pojo.Type;

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
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void clearType() {
        type = null;
    }
}
