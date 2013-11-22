package dag;

/**
 * Created with IntelliJ IDEA.
 * User: michael.vakulik
 * Date: 11/22/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginInfo {
    private String login;
    private String password;
    private String url;

    public LoginInfo() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
