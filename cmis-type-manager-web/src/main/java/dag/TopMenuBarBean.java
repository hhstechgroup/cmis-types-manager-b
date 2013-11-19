package dag;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Created with IntelliJ IDEA.
 * User: sergii.serba
 * Date: 11/18/13
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "topMenuBar")
@RequestScoped
public class TopMenuBarBean {

    private MenuModel model;
    private UIViewRoot viewRoot;

    @PostConstruct
    public void initModel() {
        model = new DefaultMenuModel();
        viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();
        addMenuItem(viewId, "applyfor", "//stream", "#", "Item1");
        addMenuItem(viewId, "findaplan", "//findaplan", "#", "Item2");
    }

    private void addMenuItem(String viewId, String mID, String rootView, String address, String label) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(mID);
        if (viewId.startsWith(rootView)) {
            menuItem.setStyleClass("active");
        }
        menuItem.setValue(label);
        menuItem.setUrl(rootView + address);
        model.addMenuItem(menuItem);
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
}
