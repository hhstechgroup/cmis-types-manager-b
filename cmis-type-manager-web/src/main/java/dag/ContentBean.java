package dag;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("contentBean")
@RequestScoped
public class ContentBean {
    @EJB
    private FolderFacade folderService;
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