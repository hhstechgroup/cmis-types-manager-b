package com.engagepoint.service;

import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.AbstractPropertyDefinition;

/**
 * User: AlexDenisenko
 * Date: 08.12.13
 * Time: 16:29
 */
public class PropertyDefinitionImpl<T> extends AbstractPropertyDefinition<T> implements PropertyDefinition<T>{
    private boolean selected;
    private Boolean isInherited;
    private Boolean isQueryable;
    private Boolean isOrderable;
    private Boolean isRequired;
    private Boolean isOpenChoice;
    private boolean isParent;



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Boolean getIsInherited() {
        return isInherited;
    }

    public void setIsInherited(Boolean isInherited) {
        this.isInherited = isInherited;
    }

    public Boolean getIsQueryable() {
        return isQueryable;
    }

    public void setIsQueryable(Boolean isQueryable) {
        this.isQueryable = isQueryable;
    }

    public Boolean getIsOrderable() {
        return isOrderable;
    }

    public void setIsOrderable(Boolean isOrderable) {
        this.isOrderable = isOrderable;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Boolean getIsOpenChoice() {
        return isOpenChoice;
    }

    public void setIsOpenChoice(Boolean isOpenChoice) {
        this.isOpenChoice = isOpenChoice;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }
}
