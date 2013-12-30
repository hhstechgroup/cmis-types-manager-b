package com.engagepoint.bean;

import com.engagepoint.constant.Constants;
import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.pojo.UserInfo;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private InputStream stream;
    private String fileName;
    @ManagedProperty(value = "#{selectedTypeHolder}")
    private SelectedTypeHolder selectedTypeHolder;
    private boolean importButtonDisabled;

    @PostConstruct
    public void init(){
        importButtonDisabled = true;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void upload(FileUploadEvent event) {
        try {
            fileName = event.getFile().getFileName();
            stream = event.getFile().getInputstream();
            importButtonDisabled = false;
        } catch (IOException e) {
            importButtonDisabled = true;
            fileName = Constants.Strings.EMPTY_STRING;
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_UPLOAD_FILE, e);
        }
    }

    public String importTypes() {
        try {
            if (stream != null) {
                UserInfo userInfo = login.getUserInfo();
                if (fileName.contains(Constants.Strings.XML_PATTERN)) {
                    service.importTypeFromXml(userInfo, stream);
                } else {
                    service.importTypeFromJson(userInfo, stream);
                }
                MessageUtils.printInfo(Constants.Messages.SUCCESS_IMPORT_TYPE);
            } else {
                MessageUtils.printInfo(Constants.Messages.NOT_SELECTED_FILE);
            }
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.ERROR_IMPORT_TYPE, e);
        } catch (XMLStreamException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.ERROR_IMPORT_TYPE, e);
        } catch (JSONParseException e) {
            MessageUtils.printError(Constants.Messages.ERROR_IMPORT_TYPE);
            LOGGER.error(Constants.Messages.ERROR_IMPORT_TYPE, e);
        }
        return Constants.Navigation.TO_MAIN_PAGE;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public SelectedTypeHolder getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolder selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    public boolean isImportButtonDisabled() {
        return importButtonDisabled;
    }

    public void setImportButtonDisabled(boolean importButtonDisabled) {
        this.importButtonDisabled = importButtonDisabled;
    }
}