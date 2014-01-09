package com.engagepoint.bean;

import com.engagepoint.ejb.Service;
import com.engagepoint.exception.AppException;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.engagepoint.constant.FileConstants.*;
import static com.engagepoint.constant.MessageConstants.*;
import static com.engagepoint.constant.NavigationConstants.TO_CURRENT_PAGE;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/12/13
 * Time: 16:26 AM
 */

@ManagedBean
@ViewScoped
public class ExportTypeBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportTypeBean.class);
    @EJB
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{selectedTypeHolderBean}")
    private SelectedTypeHolderBean selectedTypeHolder;
    private boolean includeChildren;
    private String fileType;

    @PostConstruct
    public void init() {
        fileType = XML_PATTERN;
    }

    public void exportType() {
        if (checkNotNull()) {
            try {
                if (fileType.equals(XML_PATTERN)) {
                    exportToXml();
                } else if (fileType.equals(JSON_PATTERN)) {
                    exportToJson();
                }
            } catch (IOException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error(ERROR_EXPORT_TYPE, e);
            } catch (AppException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error(ERROR_EXPORT_TYPE, e);
            }
        } else {
            MessageUtils.printError(SELECTED_TYPE_NOT_EMPTY);
            LOGGER.error(SELECTED_TYPE_NOT_EMPTY);
        }

    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isIncludeChildren() {
        return includeChildren;
    }

    public void setIncludeChildren(boolean includeChildren) {
        this.includeChildren = includeChildren;
    }

    public String redirect() {
        return TO_CURRENT_PAGE;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    private void exportToXml() throws AppException, IOException {
        String typeId = selectedTypeHolder.getType().getId();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.exportTypeToXML(login.getClientSession().getSession(), out, typeId, includeChildren);
        exportFile(out, getFileName(typeId, XML_PATTERN));
    }
    //TODO get knowledge of stream
    private void exportToJson() throws AppException, IOException {
        String typeId = selectedTypeHolder.getType().getId();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        service.exportTypeToJSON(login.getClientSession().getSession(), out, typeId, includeChildren);
        exportFile(out, getFileName(typeId, JSON_PATTERN));
    }

    private void exportFile(ByteArrayOutputStream out, String fileName) throws IOException {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            OutputStream responseOutputStream = externalContext.getResponseOutputStream();
            externalContext.setResponseContentType(CONTENT_TYPE);
            externalContext.setResponseHeader(DISPOSITION, fileName);
            responseOutputStream.write(out.toByteArray());
            IOUtils.closeQuietly(responseOutputStream);
        } finally {
            FacesContext.getCurrentInstance().responseComplete();
            LOGGER.info(EXPORT_SUCCESSFUL);
        }
    }

    private String getFileName(String typeName, String typeExtension) {
        StringBuilder builder = new StringBuilder();
        builder.append(ATTACHMENT_FILE_NAME);
        builder.append(typeName);
        builder.append(getCurrentTime());
        builder.append(".");
        builder.append(typeExtension);
        builder.append(QUOTE);
        return builder.toString();
    }

    private boolean checkNotNull() {
        return (selectedTypeHolder.getType() != null) && (fileType != null);
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_PATTERN);
        return dateFormat.format(new Date());
    }

}
