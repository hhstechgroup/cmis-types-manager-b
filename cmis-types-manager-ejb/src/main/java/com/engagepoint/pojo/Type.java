package com.engagepoint.pojo;

import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 03.12.13
 * Time: 15:09
 */
public class Type {
    private String id;
    private String localName;
    private String localNamespace;
    private String displayName;
    private String queryName;
    private String description;
    private String baseTypeId;
    private String parentTypeId;
    private boolean isCreatable;
    private boolean isFileable;
    private boolean isQueryable;
    private boolean isFulltextIndexed;
    private boolean isIncludedInSupertypeQuery;
    private boolean isControllablePolicy;
    private boolean isControllableAcl;

    private List<PropertyDefinitionImpl> properties;


    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
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

    public boolean isFileable() {
        return isFileable;
    }

    public void setFileable(boolean fileable) {
        isFileable = fileable;
    }

    public String getLocalNamespace() {
        return localNamespace;
    }

    public void setLocalNamespace(String localNamespace) {
        this.localNamespace = localNamespace;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseTypeId() {
        return baseTypeId;
    }

    public void setBaseTypeId(String baseTypeId) {
        this.baseTypeId = baseTypeId;
    }

    public String getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(String parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public boolean isQueryable() {
        return isQueryable;
    }

    public void setQueryable(boolean queryable) {
        isQueryable = queryable;
    }

    public boolean isFulltextIndexed() {
        return isFulltextIndexed;
    }

    public void setFulltextIndexed(boolean fulltextIndexed) {
        isFulltextIndexed = fulltextIndexed;
    }

    public boolean isIncludedInSupertypeQuery() {
        return isIncludedInSupertypeQuery;
    }

    public void setIncludedInSupertypeQuery(boolean includedInSupertypeQuery) {
        isIncludedInSupertypeQuery = includedInSupertypeQuery;
    }

    public boolean isControllablePolicy() {
        return isControllablePolicy;
    }

    public void setControllablePolicy(boolean controllablePolicy) {
        isControllablePolicy = controllablePolicy;
    }

    public boolean isControllableAcl() {
        return isControllableAcl;
    }

    public void setControllableAcl(boolean controllableAcl) {
        isControllableAcl = controllableAcl;
    }


    public List<PropertyDefinitionImpl> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDefinitionImpl> properties) {
        this.properties = properties;
    }
}
