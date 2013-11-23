package com.engagepoint;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
public class ContentBean implements Serializable {
    @EJB
    private CMISService folderService;
    @Inject
    private LoginBean loginBean;

    public List<String> getRootFolders() {
        LoginInfo loginInfo = loginBean.getLoginInfo();
        if (!loginInfo.isEmpty()) {
            List<String> folders = folderService.getRootFolders(loginInfo);
            return folders;
        }
        return null;
    }
}