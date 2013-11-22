package dag;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 * Created by Qnex.
 */

@Named
@SessionScoped
public class LoginBean {
    private LoginInfo loginInfo = new LoginInfo();

    @PostConstruct
    public void init() {
        loginInfo.setUserName("test");
        loginInfo.setPassword("test");
        loginInfo.setUrl("http://winctrl-tdl6ti6:8080/chemistry-opencmis-server-fileshare-1.0.0-SNAPSHOT/atom11");
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public String getUserName() {
        return loginInfo.getUserName();
    }

    public void setUserName(String userName) {
        this.loginInfo.setUserName(userName);
    }

    public String getPassword() {
        return loginInfo.getPassword();
    }

    public void setPassword(String password) {
        this.loginInfo.setPassword(password);
    }

    public String getUrl() {
        return loginInfo.getUrl();
    }

    public void setUrl(String url) {
        this.loginInfo.setUrl(url);
    }
}
