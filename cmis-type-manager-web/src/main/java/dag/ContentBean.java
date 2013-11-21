package dag;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.List;

@ManagedBean(name = "contentBean", eager = true)
@RequestScoped
public class ContentBean {
    @EJB
    private ContentManagerLocal manager;

    public List<String> getList() {
        return manager.getRootFolderContent();
    }
}