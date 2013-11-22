package dag;

/**
 * Created by Qnex.
 */
public class LoginInfo {
    private String userName;
    private String password;
    private String url;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEmpty(){
        return ((userName == null) && (password == null) && (url == null));
    }
}
