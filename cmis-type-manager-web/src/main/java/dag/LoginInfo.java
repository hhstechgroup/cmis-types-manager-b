package dag;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * User: michael.vakulik
 * Date: 11/22/13
 * Time: 3:45 PM
 */
public class LoginInfo implements Serializable {

    private String login;
    private String password;
    private String url;

    public LoginInfo() {
        this.login = "";
        this.password = "";
        this.url = "";
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

    @Override
    public String toString() {
        return "LoginInfo{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", logged=" + isLogged() +
                '}';
    }

    public void reset() {
        password = "";
        login = "";
        url = "";
    }

    public boolean isLogged() {
        return StringUtils.isNotEmpty(login) &&
                StringUtils.isNotEmpty(password) &&
                StringUtils.isNotEmpty(url);
    }
}
