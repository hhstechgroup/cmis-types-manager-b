package com.engagepoint.service;

import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 10.12.13
 * Time: 12:11
 */
public class TypeProperty<T> {
    private String id;
    private String localName;
    private String localNamespace;
    private String queryName;
    private String displayName;
    private String description;
    private String propertyType;
    private String cardinality;
    private List<T> defaultValue;
    private String updatability;
    private Boolean isInherited;
    private Boolean isQueryable;
    private Boolean isOrderable;
    private Boolean isRequired;
    private Boolean isOpenChoice;

    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getLocalNamespace() {
        return localNamespace;
    }

    public void setLocalNamespace(String localNamespace) {
        this.localNamespace = localNamespace;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    public List<T> getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(List<T> defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getUpdatability() {
        return updatability;
    }

    public void setUpdatability(String updatability) {
        this.updatability = updatability;
    }

    public Boolean getInherited() {
        return isInherited;
    }

    public void setInherited(Boolean inherited) {
        isInherited = inherited;
    }

    public Boolean getQueryable() {
        return isQueryable;
    }

    public void setQueryable(Boolean queryable) {
        isQueryable = queryable;
    }

    public Boolean getOrderable() {
        return isOrderable;
    }

    public void setOrderable(Boolean orderable) {
        isOrderable = orderable;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Boolean getOpenChoice() {
        return isOpenChoice;
    }

    public void setOpenChoice(Boolean openChoice) {
        isOpenChoice = openChoice;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "TypeProperty{" +
                "id='" + id + '\'' +
                ", localName='" + localName + '\'' +
                ", localNamespace='" + localNamespace + '\'' +
                ", queryName='" + queryName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", defaultValue=" + defaultValue +
                ", updatability='" + updatability + '\'' +
                ", isInherited=" + isInherited +
                ", isQueryable=" + isQueryable +
                ", isOrderable=" + isOrderable +
                ", isRequired=" + isRequired +
                ", isOpenChoice=" + isOpenChoice +
                ", selected=" + selected +
                '}';
    }
}

