package com.engagepoint.bean;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/12/13
 * Time: 16:26 AM
 */

import com.engagepoint.constant.Constants;
import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.pojo.UserInfo;
import com.engagepoint.util.CustomStringUtils;
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


@ManagedBean
@ViewScoped
public class ExportTypeBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportTypeBean.class);
    @EJB
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;

    private UserInfo userInfo;

    @ManagedProperty(value = "#{selectedTypeHolder}")
    private SelectedTypeHolder selectedTypeHolder;

    private boolean xmlOrJson;
    private boolean includeChildren;


    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        xmlOrJson = true;
    }

    public void exportType() {
        String selectedTypeId = selectedTypeHolder.getType().getId();
        String message;
        if (CustomStringUtils.isEmpty(selectedTypeId)) {
            message = Constants.Messages.SELECTED_TYPE_NOT_EMPTY;
            MessageUtils.printError(message);
            LOGGER.error(message);
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                if (xmlOrJson) {
                    service.exportTypeToXML(userInfo, out, selectedTypeId, includeChildren);
                } else {
                    service.exportTypeToJSON(userInfo, out, selectedTypeId, includeChildren);
                }
                byte[] arr = out.toByteArray();
                OutputStream responseOutputStream = externalContext.getResponseOutputStream();

                SimpleDateFormat sdf = new SimpleDateFormat("_dd-M-yyyy_HH-mm-SS");
                String date = sdf.format(new Date());
                if(xmlOrJson){
                    externalContext.setResponseContentType("application/xml");
                    externalContext.setResponseHeader(Constants.Strings.DISPOSITION, CustomStringUtils.concatenate(Constants.Strings.ATTACHMENT_FILE_NAME, selectedTypeId, date, ".xml", Constants.Strings.QUOTE));
                } else {
                    externalContext.setResponseContentType("application/json");
                    externalContext.setResponseHeader(Constants.Strings.DISPOSITION, CustomStringUtils.concatenate(Constants.Strings.ATTACHMENT_FILE_NAME, selectedTypeId, date, ".json", Constants.Strings.QUOTE));
                }
                responseOutputStream.write(arr);
                IOUtils.closeQuietly(responseOutputStream);
                message = Constants.Messages.EXPORT_SUCCESSFULL;
                LOGGER.info(message);
            } catch (IOException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error(Constants.Messages.ERROR_EXPORT_TYPE, e);
            } catch (CmisException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error(Constants.Messages.ERROR_EXPORT_TYPE, e);
            } finally {
                facesContext.responseComplete();
            }
        }
    }

    public String redirect(){
        return Constants.Navigation.TO_CURRENT_PAGE;
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

    public boolean isXmlOrJson() {
        return xmlOrJson;
    }

    public void setXmlOrJson(boolean xmlOrJson) {
        this.xmlOrJson = xmlOrJson;
    }

    public boolean isIncludeChildren() {
        return includeChildren;
    }

    public void setIncludeChildren(boolean includeChildren) {
        this.includeChildren = includeChildren;
    }
}
