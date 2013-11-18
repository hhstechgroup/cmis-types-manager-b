package dag;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "repo", eager = true)
@RequestScoped
public class Repo {
    @EJB(name = "DagEJB")
    private Dag dag;
    private String message = "Repo World!";

    public String getMessage() {
        return dag.getAllFolders();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}