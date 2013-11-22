package dag;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Created by Qnex.
 */
@Stateless
@LocalBean
public class FolderFacade {
    private final SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    public Folder getRootFolder(LoginInfo loginInfo) {
        return getSession(loginInfo).getRootFolder();
    }
    private Session getSession(LoginInfo loginInfo) {

        return sessionFactory.createSession(loginInfo.parameters);
    }

}
