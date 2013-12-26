package com.engagepoint.model;

import com.engagepoint.service.TypeProperty;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.List;

/**
 * Created by michael.vakulik on 12/23/13.
 */
public class TypePropertyModel extends ListDataModel<TypeProperty> implements SelectableDataModel<TypeProperty>, Serializable {

    public TypePropertyModel() {
    }

    public TypePropertyModel(List<TypeProperty> list){
        super(list);
    }

    @Override
    public Object getRowKey(TypeProperty typeProperty) {
        return typeProperty.getId();
    }

    @Override
    public TypeProperty getRowData(String s) {
        List<TypeProperty> typeProperties = (List<TypeProperty>) getWrappedData();
        for(TypeProperty typeProperty : typeProperties) {
            if (StringUtils.equals(typeProperty.getId(), s)) {
                return typeProperty;
            }
        }
        return null;
    }
}
