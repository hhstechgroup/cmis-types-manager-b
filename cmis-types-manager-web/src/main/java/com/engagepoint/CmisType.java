package com.engagepoint;

import java.util.List;

/**
 * Created by Qnex.
 */
public class CmisType {
    private String name;
    private String id;
    private boolean isCreatable;
    private boolean isFileable;
    private List<CmisType> children;

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


    public List<CmisType> getChildren() {
        return children;
    }

    public boolean isFileable() {
        return isFileable;
    }

    public void setFileable(boolean fileable) {
        isFileable = fileable;
    }

    public void setChildren(List<CmisType> children) {
        this.children = children;
    }
}
