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
//        2nd navigation level
        model = new DefaultMenuModel();
        viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();
        MenuItem typesManagement = new MenuItem();
        typesManagement.setId("index");
        typesManagement.setValue("Types Management");
        typesManagement.setUrl("/dashboard/index.xhtml");
        if (viewId.startsWith("/dashboard/")) {
            typesManagement.setStyleClass("active");
        }
//        Create page added, 3rd navigation level
        MenuItem create = new MenuItem();
        create.setId("create");
        create.setValue("Create type");
        create.setUrl("/dashboard/crud/create.xhtml");
        if (viewId.startsWith("/dashboard/")) {
            create.setStyleClass("active");
        }
        create.setParent(typesManagement);

        MenuItem view = new MenuItem();
        view.setId("view");
        view.setValue("View type");
        view.setUrl("/dashboard/crud/type.xhtml");
        if (viewId.startsWith("/dashboard/")) {
            view.setStyleClass("active");
        }
        view.setParent(typesManagement);

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
}

