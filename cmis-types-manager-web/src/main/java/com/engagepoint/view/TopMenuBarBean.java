package com.engagepoint.view;

import com.engagepoint.constants.Constants;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * User: sergii.serba
 * Date: 11/18/13
 * Time: 6:06 PM
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
        addMenuItem(viewId, Constants.TopMenuBar.DASHBOARD_MID, Constants.TopMenuBar.DASHBOARD_ROOT_VIEW, Constants.TopMenuBar.DASHBOARD_ADDRESS, Constants.TopMenuBar.DASHBOARD_LABEL);
        addMenuItem(viewId, Constants.TopMenuBar.CONFIGURATION_MID, Constants.TopMenuBar.CONFIGURATION_ROOT_VIEW, Constants.TopMenuBar.CONFIGURATION_ADDRESS, Constants.TopMenuBar.CONFIGURATION_LABEL);
        addMenuItem(viewId, Constants.TopMenuBar.INFO_MID, Constants.TopMenuBar.INFO_ROOT_VIEW, Constants.TopMenuBar.INFO_ADDRESS, Constants.TopMenuBar.INFO_LABEL);
    }

    private void addMenuItem(String viewId, String mID, String rootView, String address, String label) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(mID);
        if (viewId.startsWith(rootView)) {
            menuItem.setStyleClass(Constants.TopMenuBar.STYLE_CLASS);
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