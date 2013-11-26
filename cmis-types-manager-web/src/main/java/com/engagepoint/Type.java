package com.engagepoint;

import java.util.List;

/**
 * Created by Qnex.
 */
public class Type {
    private String name;
    private String id;
    private boolean isCreatable;
    private List<Type> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCreatable() {
        return isCreatable;
    }

    public void setCreatable(boolean creatable) {
        isCreatable = creatable;
    }


    public List<Type> getChildren() {
        return children;
    }

    public void setChildren(List<Type> children) {
        this.children = children;
    }
}
