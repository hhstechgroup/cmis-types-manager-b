package com.engagepoint;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
public class MainBean implements Serializable {
    @EJB
    private CMISService service;
    @Inject
    private LoginController login;

    public List<String> getRootFolders() {
//        TODO Find out what the problem is and put check
        LoginInfo loginInfo = login.getLoginInfo();
        List<String> folders = service.getRootFolders(loginInfo);
        return folders;
    }
}