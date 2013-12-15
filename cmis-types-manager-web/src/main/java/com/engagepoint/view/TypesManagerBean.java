package com.engagepoint.view;


import com.engagepoint.components.Message;
import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.exceptions.CmisTypeDeleteException;
import com.engagepoint.services.CmisService;
import com.engagepoint.services.TypeProxy;
import com.engagepoint.services.UserInfo;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * User: AlexDenisenko
 * Date: 11/24/13
 * Time: 0:24 PM
 */
@ManagedBean
@ViewScoped
public class TypesManagerBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TypesManagerBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;

    private UserInfo userInfo;
    private TreeNode root;
    private TreeNode selectedNode;
    private TypeProxy selectedType;
    private List<TypeProxy> typeProxies;

    private static final String TREE_DATA = "Root";
    private static final int FIRST_TYPE_ID = 0;

    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        initTree();
    }

    private void initTree() {
        try {
            root = new DefaultTreeNode(TREE_DATA, null);
            typeProxies = service.getTypeInfo(userInfo);
            setSelectedType();
            navigationBean.setTypeProxy(selectedType);
            addTypesToTree(typeProxies, root);
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Unable to initialise tree", e);
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeExpand(NodeExpandEvent event) {
        Message.printInfo("Expanded", event.getTreeNode().toString());
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        Message.printInfo("Collapsed", event.getTreeNode().toString());
    }

    public void onNodeSelect(NodeSelectEvent event) {
        selectedType = (TypeProxy) event.getTreeNode().getData();
        navigationBean.setTypeProxy(selectedType);
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        Message.printInfo("Unselected", event.getTreeNode().toString());
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public void deleteType() {
        try {
            service.deleteType(userInfo, selectedType);
            Message.printInfo("Deleted type " + selectedType.getDisplayName());
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while deleting type", e);
        } catch (CmisTypeDeleteException e) {
            Message.printError("The type <" + selectedType.getDisplayName() + "> cannot be deleted");
            LOGGER.error("Unable to delete type", e);
        }
    }

    public void deleteType(UserInfo userInfo, TypeProxy selectedType) {
        if (typeHasSubtypes(selectedType)) {
            List<TypeProxy> selectedTypeChildren = selectedType.getChildren();
            for (TypeProxy selectedTypeChild : selectedTypeChildren) {
                deleteType(userInfo, selectedTypeChild);
            }
        }
        try {
            service.deleteType(userInfo, selectedType);
            Message.printInfo("Deleted type " + selectedType.getDisplayName());
        } catch (CmisConnectException e) {
            Message.printError(e.getMessage());
            LOGGER.error("Error while deleting type", e);
        } catch (CmisTypeDeleteException e) {
            Message.printError("The type <" + selectedType.getDisplayName() + "> cannot be deleted");
            LOGGER.error("Unable to delete type", e);
        }
    }

    public void deleteTypeWithSubtypes() {
        if (typeHasSubtypes(selectedType)) {
            deleteType(userInfo, selectedType);
        } else {
            deleteType();
        }
        initTree();
    }

    public boolean typeHasSubtypes(TypeProxy proxy) {
        return !proxy.getChildren().isEmpty();
    }

    public TypeProxy getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(TypeProxy selectedType) {
        this.selectedType = selectedType;
    }

    public void setSelectedType() {
        if (navigationBean.getTypeProxy() == null) {
            selectedType = typeProxies.get(FIRST_TYPE_ID);
            navigationBean.setTypeProxy(selectedType);
        } else {
            selectedType = navigationBean.getTypeProxy();
        }
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    private void addTypesToTree(List<TypeProxy> types, TreeNode parent) {
        for (TypeProxy type : types) {
            TreeNode node = new DefaultTreeNode(type, parent);
            if (!type.getChildren().isEmpty()) {
                addTypesToTree(type.getChildren(), node);
            }
        }
    }

    public String getDeleteMessage() {
        if (typeHasSubtypes(selectedType)) {
            return "\"" + selectedType.getDisplayName() + "\" type has children. Are you sure you want to delete?";
        } else {
            return "Are you sure you want to delete \"" + selectedType.getDisplayName() + "\" type ?";
        }
    }
}