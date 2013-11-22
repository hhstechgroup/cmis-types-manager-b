package dag;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: michael.vakulik
 * Date: 11/21/13
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private LoginInfo loginInfo;

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public boolean isLogged() {
        return loginInfo != null;
    }

    public void doLogout() {
        this.loginInfo = null;
    }

    public String doLogin() {
        if (loginInfo.getLogin().equals("admin") && loginInfo.getPassword().equals("admin")) {
            return "error";
        } else {
            return "goto";
        }
    }


}



