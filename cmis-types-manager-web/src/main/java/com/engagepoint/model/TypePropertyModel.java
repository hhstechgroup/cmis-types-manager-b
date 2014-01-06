package com.engagepoint.model;

import com.engagepoint.pojo.PropertyDefinitionImpl;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.List;

/**
 * Created by michael.vakulik on 12/23/13.
 */
public class TypePropertyModel extends ListDataModel<PropertyDefinitionImpl> implements SelectableDataModel<PropertyDefinitionImpl>, Serializable {

    public TypePropertyModel() {
    }

    public TypePropertyModel(List<PropertyDefinitionImpl> list){
        super(list);
    }

    @Override
    public Object getRowKey(PropertyDefinitionImpl typeProperty) {
        return typeProperty.getId();
    }

    @Override
    public PropertyDefinitionImpl getRowData(String s) {
        List<PropertyDefinitionImpl> typeProperties = (List<PropertyDefinitionImpl>) getWrappedData();
        for(PropertyDefinitionImpl typeProperty : typeProperties) {
            if (StringUtils.equals(typeProperty.getId(), s)) {
                return typeProperty;
            }
        }
        return null;
    }
}
