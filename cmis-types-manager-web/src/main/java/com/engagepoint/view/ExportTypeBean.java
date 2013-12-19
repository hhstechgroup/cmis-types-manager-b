package com.engagepoint.view;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/12/13
 * Time: 16:26 AM
 */

import com.engagepoint.utils.MessageUtils;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import com.engagepoint.utils.StringUtils;
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


@ManagedBean
@ViewScoped
public class ExportTypeBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportTypeBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;

    private UserInfo userInfo;

    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;

    private boolean xmlOrJson;
    private boolean includeChildren;


    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        xmlOrJson = true;
    }

    public void exportType() {
        String selectedTypeId = sessionStateBean.getTypeProxy().getId();
        String message = "";
        if (StringUtils.isEmpty(selectedTypeId)) {
            message = "Selected type can't be Null or Empty string";
            MessageUtils.printError(message);
            LOGGER.error(message);
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            try {
                OutputStream responseOutputStream = externalContext.getResponseOutputStream();
                if (xmlOrJson) {
                    externalContext.setResponseContentType("application/xml");
                    externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + selectedTypeId + ".xml" + "\"");
                    service.exportTypeToXML(userInfo, responseOutputStream, selectedTypeId, includeChildren);
                } else {
                    externalContext.setResponseContentType("application/json");
                    externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + selectedTypeId + ".json" + "\"");
                    service.exportTypeToJSON(userInfo, responseOutputStream, selectedTypeId, includeChildren);
                }
                message = "Selected type is exported successfully";
                LOGGER.info(message);
            } catch (IOException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error("Error while exporting type", e);
            } catch (CmisException e) {
                MessageUtils.printError(e.getMessage());
                LOGGER.error("Error while exporting type", e);
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

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
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
