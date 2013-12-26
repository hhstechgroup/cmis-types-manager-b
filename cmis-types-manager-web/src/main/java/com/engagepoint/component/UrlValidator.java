package com.engagepoint.component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: victor.klymenko
 * Date: 11/25/13
 * Time: 12:03 PM
 */
public class UrlValidator implements Validator {
    private static final String URL_PATTERN = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
    private static final String SUMMARY = "Url validation failed.";
    private static final String DETAIL = "Invalid Url format.";
    private Pattern pattern;

    public UrlValidator() {
        pattern = Pattern.compile(URL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        Matcher matcher = pattern.matcher(value.toString());
        if (!matcher.matches()) {
            FacesMessage msg = new FacesMessage(SUMMARY, DETAIL);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}