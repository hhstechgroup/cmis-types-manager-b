package com.engagepoint.bean;

import com.engagepoint.constant.R;
import com.engagepoint.ejb.Service;
import com.engagepoint.exception.AppException;
import com.engagepoint.util.MessageUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

import static com.engagepoint.constant.FileConstants.JSON_PATTERN;
import static com.engagepoint.constant.FileConstants.XML_PATTERN;
import static com.engagepoint.constant.MessageConstants.*;
import static com.engagepoint.constant.NavigationConstants.TO_MAIN_PAGE;

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
    private UploadedFile file;
    @ManagedProperty(value = "#{selectedTypeHolderBean}")
    private SelectedTypeHolderBean selectedTypeHolder;

    @PostConstruct
    public void init() {
        final CommandButton importTypeButton = (CommandButton) findComponentById(R.Id.CREATE_TYPE_BTM);
        importTypeButton.setDisabled(true);
    }

    public void upload(FileUploadEvent event) {
        file = event.getFile();
        final CommandButton importTypeButton = (CommandButton) findComponentById(R.Id.CREATE_TYPE_BTM);
        importTypeButton.setDisabled(false);
    }

    public String getFileName() {
        return file != null ? file.getFileName() : StringUtils.EMPTY;
    }

    public String getFileSize() {
        return file != null ? getFileSizeInKilobytes(file.getSize()) : StringUtils.EMPTY;
    }

    public String importTypes() {
        try {
            if (file != null) {
                if (file.getFileName().contains(XML_PATTERN)) {
                    service.importTypeFromXml(login.getClientSession().getSession(), file.getInputstream());
                    MessageUtils.printInfo(SUCCESS_IMPORT_TYPE);
                } else if (file.getFileName().contains(JSON_PATTERN)) {
                    service.importTypeFromJson(login.getClientSession().getSession(), file.getInputstream());
                    MessageUtils.printInfo(SUCCESS_IMPORT_TYPE);
                }
            } else {
                MessageUtils.printInfo(FILE_NOT_SELECTED);
            }
        } catch (AppException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(ERROR_IMPORT_TYPE, e);
        }  catch (IOException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_UPLOAD_FILE, e);
        }
        return TO_MAIN_PAGE;
    }

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    private String getFileSizeInKilobytes(long size) {
        return String.format("%.2f KB", size / 1000.0);
    }

    private UIComponent findComponentById(String id) {
        UIComponent root = FacesContext.getCurrentInstance().getViewRoot();
        return ComponentUtils.findComponent(root, id);
    }

}
