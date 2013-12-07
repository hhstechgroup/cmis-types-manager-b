package com.engagepoint.view;

import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.primefaces.event.FileUploadEvent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
public class ImportBean {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private InputStream stream;

    public void upload(FileUploadEvent event) {
        printMessage(event.getFile().getFileName() + " is uploaded.");
        try {
            stream = event.getFile().getInputstream();
        } catch (IOException e) {
            printMessage(e.getMessage());
        }
    }

    public void importTypes() {
        UserInfo userInfo = login.getUserInfo();
        try {
            if (stream != null) {
                service.importType(userInfo, stream);
                printMessage("Type imported successful!");
            } else {
                printMessage("File is not selected");
            }
        } catch (CmisConnectException e) {
            printMessage(e.getMessage());
        } catch (XMLStreamException e) {
            printMessage(e.getMessage());
        } catch (CmisCreateException e) {
            printMessage(e.getMessage());

        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    private void printMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
    }
}
