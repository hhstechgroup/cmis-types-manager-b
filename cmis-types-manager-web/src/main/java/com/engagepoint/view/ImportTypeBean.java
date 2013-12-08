package com.engagepoint.view;

import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisCreateException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.UserInfo;
import org.primefaces.event.FileUploadEvent;

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
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private InputStream stream;

    public void upload(FileUploadEvent event) {
        Message.print(event.getFile().getFileName() + " is uploaded.");
        try {
            stream = event.getFile().getInputstream();
        } catch (IOException e) {
            Message.print(e.getMessage());
        }
    }

    public void importTypes() {
        UserInfo userInfo = login.getUserInfo();
        try {
            if (stream != null) {
                service.importType(userInfo, stream);
                Message.print("Type imported successful!");
            } else {
                Message.print("File is not selected");
            }
        } catch (CmisConnectException e) {
            Message.print(e.getMessage());
        } catch (XMLStreamException e) {
            Message.print(e.getMessage());
        } catch (CmisCreateException e) {
            Message.print(e.getMessage());

        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }
}
