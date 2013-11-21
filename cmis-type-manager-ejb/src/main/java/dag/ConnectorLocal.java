package dag;

import org.apache.chemistry.opencmis.client.api.Session;

import javax.ejb.Local;

/**
 * Created by Qnex.
 */
@Local
public interface ConnectorLocal {
    Session getSession(String atomUrl);
}
