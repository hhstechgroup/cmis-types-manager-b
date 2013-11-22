package dag;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qnex.
 */

@Named
@SessionScoped
public class LoginBean {
    private LoginInfo loginInfo;
    private String userName;
    private String password;

    @PostConstruct
    public void init() {
        loginInfo = new LoginInfo();
        Map<String, String> parameters = new HashMap<String, String>();
        String atomUrl = "http://winctrl-tdl6ti6:8080/chemistry-opencmis-server-fileshare-1.0.0-SNAPSHOT/atom11";
        String userName = "test";
        String password = "test";
        String repositoryId = "test";
        parameters.put(SessionParameter.USER, userName);
        parameters.put(SessionParameter.PASSWORD, password);
        parameters.put(SessionParameter.ATOMPUB_URL, atomUrl);
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameters.put(SessionParameter.REPOSITORY_ID, repositoryId);
        loginInfo.parameters = parameters;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
