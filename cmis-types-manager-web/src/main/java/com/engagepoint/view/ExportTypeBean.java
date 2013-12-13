package com.engagepoint.view;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/13/13
 * Time: 11:26 AM
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



    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        selectedTypeId = navigationBean.getTypeProxy().getId();

    }

    public void exportType() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/xml");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + selectedTypeId + ".xml" + "\"");
        try {
            OutputStream responseOutputStream = externalContext.getResponseOutputStream();
            service.exportType(userInfo, responseOutputStream, selectedTypeId);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        } catch (CmisExportException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        } catch (IOException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while exporting type", e);
        }
        facesContext.responseComplete();
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
