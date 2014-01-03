package com.engagepoint.bean;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import static com.engagepoint.constant.NameConstants.*;
import static com.engagepoint.constant.NavigationConstants.*;

/**
 * User: sergii.serba
 * Date: 11/18/13
 * Time: 6:06 PM
 */
@ManagedBean
@RequestScoped
public class TopMenuBarBean {
    private MenuModel model;

    @PostConstruct
    public void initModel() {
        model = new DefaultMenuModel();
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();
        addMenuItem(viewId, DASHBOARD_MID, DASHBOARD_ROOT_VIEW, MAIN_ADDRESS, DASHBOARD_LABEL);
        addMenuItem(viewId, CONFIGURATION_MID, CONFIGURATION_ROOT_VIEW, MAIN_ADDRESS, CONFIGURATION_LABEL);
        addMenuItem(viewId, INFO_MID, INFO_ROOT_VIEW, MAIN_ADDRESS, INFO_LABEL);
    }

    private void addMenuItem(String viewId, String mID, String rootView, String address, String label) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(mID);
        if (viewId.startsWith(rootView)) {
            menuItem.setStyleClass(STYLE_CLASS_ACTIVE);
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