package com.engagepoint.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * User: AlexDenisenko
 * Date: 07.12.13
 * Time: 22:02
 */
public class MessageUtils {
    private MessageUtils() {
    }

    public static void printInfo(String text) {
        getFacesContext().addMessage(null, getInfoMessage(text, ""));
    }

    public static void printInfo(String text, String detail) {
        getFacesContext().addMessage(null, getInfoMessage(text, detail));
    }

    public static void printError(String text) {
        getFacesContext().addMessage(null, getErrorMessage(text, ""));
    }

    public static void printError(String text, String detail) {
        getFacesContext().addMessage(null, getErrorMessage(text, detail));
    }

    public static void printWarn(String text) {
        getFacesContext().addMessage(null, getWarnMessage(text, ""));
    }

    public static void printWarn(String text, String detail) {
        getFacesContext().addMessage(null, getWarnMessage(text, detail));
    }

    public static FacesMessage getWarnMessage(String text, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_WARN, text, detail);
    }

    public static FacesMessage getErrorMessage(String text, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR, text, detail);
    }

    public static FacesMessage getInfoMessage(String text, String detail) {
        return new FacesMessage(FacesMessage.SEVERITY_INFO, text, detail);
    }

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
