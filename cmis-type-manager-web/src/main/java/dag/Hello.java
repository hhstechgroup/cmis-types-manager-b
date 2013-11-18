package dag;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "hello", eager = true)
@RequestScoped
public class Hello {
    @EJB(name = "DagEJB")
    private Dag dag;
    private String message = "Hello World!";

    public String getMessage() {
        return dag.sayHey();
    }

    public void setMessage(String message) {
        this.message = message;
    }
}