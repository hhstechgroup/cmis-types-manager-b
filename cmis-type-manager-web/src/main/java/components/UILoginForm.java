package components;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponentBase;

@FacesComponent(value = "com.engagepoint.component.loginForm")
public class UILoginForm extends UIComponentBase implements NamingContainer {

    @Override
    public String getFamily() {
        return "javax.faces.NamingContainer";
    }
}