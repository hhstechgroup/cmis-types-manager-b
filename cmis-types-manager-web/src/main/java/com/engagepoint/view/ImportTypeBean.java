package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private InputStream stream;
    private String fileName;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void upload(FileUploadEvent event) {
        try {
            fileName = event.getFile().getFileName();
//            Message.printInfo(fileName + " is uploaded.");
            stream = event.getFile().getInputstream();
        } catch (IOException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to upload file", e);
        }
    }

    public String importTypes() {
        try {
            if (stream != null) {
                UserInfo userInfo = login.getUserInfo();
                if (fileName.contains("xml")) {
                    service.importTypeFromXml(userInfo, stream);
                } else {
                    service.importTypeFromJson(userInfo, stream);
                }
                Message.printInfo("Type imported successful!");
            } else {
                Message.printInfo("File is not selected");
            }
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while import type", e);
        } catch (XMLStreamException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while pars file", e);
        } catch (CmisCreateException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while pars file", e);
        } catch (JSONParseException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to create type", e);
        }
        return navigationBean.toMainPage();
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }
}
