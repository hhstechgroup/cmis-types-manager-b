package com.engagepoint.view;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * User: stanislav.skrebtsov (stanislav.skrebtsov@engagepoint.com)
 * Date: 12.12.13
 * Time: 15:35
 */
@ManagedBean(name = "leftMenuBar")
@RequestScoped
public class LeftMenuBarBean {
    private MenuModel model;
    private UIViewRoot viewRoot;

    @PostConstruct
    public void initModel() {
        model = new DefaultMenuModel();
        viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();
        addMenuItem(viewId, "index", "/dashboard/", "index.xhtml", "Types Management");
        addMenuItem(viewId, "create", "/dashboard/", "create.xhtml", "Create type");
        addMenuItem(viewId, "view", "/dashboard/", "type.xhtml", "View type");
        addMenuItem(viewId, "update", "/dashboard/", "type.xhtml", "Update type");
        addMenuItem(viewId, "delete", "/dashboard/", "type.xhtml", "Delete type");
        addMenuItem(viewId, "export", "/dashboard/", "export.xhtml", "Export type");
        addMenuItem(viewId, "import", "/dashboard/", "import.xhtml", "Import type");
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

