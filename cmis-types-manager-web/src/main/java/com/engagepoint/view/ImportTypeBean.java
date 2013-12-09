package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: AlexDenisenko
 * Date: 07.12.13
 * Time: 16:17
 */
@ManagedBean
@ViewScoped
public class ImportTypeBean {
    private Logger log = LoggerFactory.getLogger(ImportTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private InputStream stream;

    public void upload(FileUploadEvent event) {
        try {
            Message.printInfo(event.getFile().getFileName() + " is uploaded.");
            stream = event.getFile().getInputstream();
        } catch (IOException e) {
            Message.printInfo(e.getMessage());
            log.error("Unable to upload file", e);
        }
    }

    public void importTypes() {
        try {
            UserInfo userInfo = login.getUserInfo();
            if (stream != null) {
                service.importType(userInfo, stream);
                Message.printInfo("Type imported successful!");
            } else {
                Message.printInfo("File is not selected");
            }
        } catch (CmisConnectException e) {
            Message.printInfo(e.getMessage());
            log.error("Error while import type", e);
        } catch (XMLStreamException e) {
            Message.printInfo(e.getMessage());
            log.error("Error while pars file", e);
        } catch (CmisCreateException e) {
            Message.printInfo(e.getMessage());
            log.error("Unable to create type", e);
        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }
}
