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

/**
 * Created by IntelliJ IDEA.
 * User: Dag
 * Date: 2011-03-10
 * Time: 20.41
 * To change this template use File | Settings | File Templates.
 */
@Stateless(name = "DagEJB")
public class DagBean implements Dag {
    public DagBean() {
    }

    @Override
    public String getAllFolders() {
        String atomUrl = "http://winctrl-tdl6ti6:8080/chemistry-opencmis-server-inmemory-0.10.0/atom11";
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, "");
        parameter.put(SessionParameter.PASSWORD, "");
        parameter.put(SessionParameter.ATOMPUB_URL, atomUrl);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, "A1");
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Session session = sessionFactory.createSession(parameter);
//        Repository repository = sessionFactory.getRepositories(parameter).get(0);
        List<String> folders = new ArrayList<String>();
        if (session != null) {
            Folder root = session.getRootFolder();
            ItemIterable<CmisObject> children = root.getChildren();
            for (CmisObject child : children) {
                folders.add(child.getName());
            }
        }
        return folders.get(0);
    }

}
