package dag;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless(name = "ContentManager")
public class ContentManager implements ContentManagerLocal {
    public ContentManager() {
    }

    @Override
    public List<String> getRootFolderContent() {
        String atomUrl = "http://winctrl-tdl6ti6:8080/chemistry-opencmis-server-fileshare-1.0.0-SNAPSHOT/atom11";
        Session session = getSession(atomUrl);
        List<String> folders = new ArrayList<String>();
        if (session != null) {
            Folder root = session.getRootFolder();
            ItemIterable<CmisObject> children = root.getChildren();
            for (CmisObject child : children) {
                folders.add("[" + child.getName() + "]" + " which is of type " + child.getType().getDisplayName());
            }

        }
        return folders;
    }

    private Session getSession(String atomUrl) {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, "test");
        parameter.put(SessionParameter.PASSWORD, "test");
        parameter.put(SessionParameter.ATOMPUB_URL, atomUrl);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, "test");
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
//        Repository repository = sessionFactory.getRepositories(parameter).get(0);

        return sessionFactory.createSession(parameter);
    }

}
