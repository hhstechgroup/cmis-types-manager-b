package com.engagepoint.services;

import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;

import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 02.12.13
 * Time: 22:40
 */
public class TypeProxy {
    private String id;
    private String displayName;
    private String baseType;
    private TypeMutability typeMutability;

    public TypeMutability getTypeMutability() {
        return typeMutability;
    }

    public void setTypeMutability(TypeMutability typeMutability) {
        this.typeMutability = typeMutability;
    }

    private List<TypeProxy> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public List<TypeProxy> getChildren() {
        return children;
    }

    public void setChildren(List<TypeProxy> children) {
        this.children = children;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }
}
