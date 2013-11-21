package dag;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qnex.
 */

@Singleton
@Startup
public class Connector implements ConnectorLocal {
    private SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    @Override
    public Session getSession(String atomUrl) {
        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, "test");
        parameter.put(SessionParameter.PASSWORD, "test");
        parameter.put(SessionParameter.ATOMPUB_URL, atomUrl);
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, "test");
//        Repository repository = sessionFactory.getRepositories(parameter).get(0);
        return sessionFactory.createSession(parameter);
    }

}
