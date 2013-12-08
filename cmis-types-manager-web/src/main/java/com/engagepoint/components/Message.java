package com.engagepoint.components;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * User: AlexDenisenko
 * Date: 07.12.13
 * Time: 22:02
 */
public class Message {

    public static void print(String text) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text, ""));
    }
}
