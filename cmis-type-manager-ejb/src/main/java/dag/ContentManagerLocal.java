package dag;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ContentManagerLocal {
    List<String> getRootFolderContent();
}
