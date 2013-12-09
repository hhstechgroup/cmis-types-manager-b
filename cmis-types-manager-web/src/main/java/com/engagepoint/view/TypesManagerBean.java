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
import javax.faces.context.FacesContext;
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
    private Logger log = LoggerFactory.getLogger(TypesManagerBean.class);
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private TreeNode root;
    private TreeNode selectedNode;
    private TypeProxy selectedType;
    private Boolean isShowDeleteDialog;
    private static final String TREE_DATA = "Root";

    @PostConstruct
    public void init() {
        initTree();
        hideDeleteDialog();
    }

    private void initTree() {
        try {
            root = new DefaultTreeNode(TREE_DATA, null);
            UserInfo userInfo = login.getUserInfo();
            List<TypeProxy> typeProxies = service.getTypeInfo(userInfo);
            int firstTypeId = 0;
            selectedType = typeProxies.get(firstTypeId);
            addTypesToTree(typeProxies, root);
        } catch (CmisConnectException e) {
            Message.printInfo(e.getMessage());
            log.error("Unable to initialization tree", e);
        }
    }

    public String goTypePage() {
        setParameterToFlash();
        return navigationBean.toViewType();
    }

    public String goCreatePage() {
        setParameterToFlash();
        return navigationBean.toCreateType();
    }

    public String goToIndex() {
        return navigationBean.toMainPage();
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

    public String deleteType() {
        try {
            int firstTypeId = 0;
            UserInfo userInfo = login.getUserInfo();
            List<TypeProxy> typeProxies = service.getTypeInfo(userInfo);
            service.deleteType(userInfo, selectedType);
            Message.printInfo("Deleted type " + selectedType.getDisplayName());
            initTree();
            selectedType = typeProxies.get(firstTypeId);
        } catch (CmisConnectException e) {
            Message.printInfo(e.getMessage());
            log.error("Error while deleting type", e);
        } catch (CmisTypeDeleteException e) {
            Message.printInfo("The type <" + selectedType.getDisplayName() + "> cannot be deleted");
            log.error("Unable to delete type", e);
        }
        hideDeleteDialog();
        return "";
    }

    public Boolean isShowDeleteDialog() {
        return isShowDeleteDialog;
    }

    public void showDeleteDialog() {
        this.isShowDeleteDialog = true;
    }

    public void hideDeleteDialog() {
        this.isShowDeleteDialog = false;
    }

    private void setParameterToFlash() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedType", selectedType);
    }

    public TypeProxy getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(TypeProxy selectedType) {
        this.selectedType = selectedType;
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
}