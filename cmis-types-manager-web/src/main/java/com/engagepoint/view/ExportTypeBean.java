package com.engagepoint.view;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/12/13
 * Time: 16:26 AM
 */

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisExportException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
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

    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;

    private String selectedTypeId;
    private FacesContext facesContext;
    ExternalContext externalContext;


    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        selectedTypeId = navigationBean.getTypeProxy().getId();

    }

    public void exportType() {
        facesContext = FacesContext.getCurrentInstance();
        externalContext = facesContext.getExternalContext();


        try {
            OutputStream responseOutputStream = externalContext.getResponseOutputStream();
            if (true) {
                exportCurrentType(responseOutputStream);
            } else {
                exportTreeType(responseOutputStream);
            }
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (IOException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        }
        facesContext.responseComplete();
    }

    private void exportCurrentType(OutputStream responseOutputStream) {
        try {
            if (true) {
                externalContext.setResponseContentType("application/xml");
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + selectedTypeId + ".xml" + "\"");
                service.exportTypeToXML(userInfo, responseOutputStream, selectedTypeId);
            } else {
                externalContext.setResponseContentType("application/json");
                externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + selectedTypeId + ".json" + "\"");
                service.exportTypeToJSON(userInfo, responseOutputStream, selectedTypeId);
            }
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        } catch (CmisExportException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        }
    }

    //TODO Implements export type tree with children's
    private void exportTreeType(OutputStream responseOutputStream) {

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
