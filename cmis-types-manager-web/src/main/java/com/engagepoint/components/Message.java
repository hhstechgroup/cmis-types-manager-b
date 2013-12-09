package com.engagepoint.components;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * User: AlexDenisenko
 * Date: 07.12.13
 * Time: 22:02
 */
public class Message {

    public static void printInfo(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text, ""));
    }

    public static void printInfo(String text, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text, detail));
    }

    public static void printError(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, ""));
    }

    public static void printError(String text, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, text, detail));
    }

    public static void printWarn(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, text, ""));
    }

    public static void printWarn(String text, String detail) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, text, detail));
    }
}
