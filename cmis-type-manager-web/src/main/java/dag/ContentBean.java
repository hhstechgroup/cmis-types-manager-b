package dag;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;

import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("contentBean")
@RequestScoped
public class ContentBean {
    @EJB
    private FolderFacade folderService;
    @Inject
    private LoginBean loginBean;

    public List<String> getRootFolder() {
        List<String> folders = new ArrayList<String>();
        Folder root = folderService.getRootFolder(loginBean.getLoginInfo());
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject child : children) {
            folders.add(child.getName());
        }
        return folders;
    }
}