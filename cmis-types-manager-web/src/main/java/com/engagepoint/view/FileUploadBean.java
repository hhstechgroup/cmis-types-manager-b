package com.engagepoint.view;

/**
 * User: michael.vakulik
 * Date: 12/6/13
 * Time: 2:55 PM
 */

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

import java.util.EventObject;


@ManagedBean
@RequestScoped
public class FileUploadBean {


    public void handleFileUpload(EventObject event) {
        System.out.println("Mrewwww!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String msg = new String("Succesful"+ event.toString() + " is uploaded.");
        //messagesBean.addMessage(FacesMessage.SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
    }
}
