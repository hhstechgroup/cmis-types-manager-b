package com.engagepoint.component;

import com.engagepoint.util.MessageUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import static com.engagepoint.constant.FileConstants.JSON_PATTERN;
import static com.engagepoint.constant.FileConstants.XML_PATTERN;

/**
 * User: victor.klymenko
 * Date: 11/25/13
 * Time: 12:03 PM
 */
public class ExportValidator implements Validator {
    private static final String SUMMARY = "Item is not selected!";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        if (!isCorrectFileType(value)) {
            throw new ValidatorException(MessageUtils.getErrorMessage(SUMMARY, null));
        }
    }

    private boolean isCorrectFileType(Object value) {
        return (value != null) && (value.equals(XML_PATTERN) || value.equals(JSON_PATTERN));
    }
}