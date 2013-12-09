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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TreeNode root;
    private TreeNode selectedNode;
    private TypeProxy selectedType;
    private Boolean isShowDeleteDialog = false;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;
    private static final String TREE_DATA = "Root";

    @PostConstruct
    public void init() {
        initTree();
    }

    private void initTree() {
        root = new DefaultTreeNode(TREE_DATA, null);
        try {
            UserInfo userInfo = login.getUserInfo();
            List<TypeProxy> typeProxies = service.getTypeInfo(userInfo);
            int firstTypeId = 0;
            selectedType = typeProxies.get(firstTypeId);
            addTypesToTree(typeProxies, root);
        } catch (CmisConnectException e) {
            Message.print(e.getMessage());
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
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expanded", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        selectedType = (TypeProxy) event.getTreeNode().getData();
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void addTypesToTree(List<TypeProxy> cmisTypes, TreeNode parent) {
        for (TypeProxy cmisType : cmisTypes) {
            TreeNode node = new DefaultTreeNode(cmisType, parent);
            if (!cmisType.getChildren().isEmpty()) {
                addTypesToTree(cmisType.getChildren(), node);
            }
        }
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
            Message.print("Deleted type " + selectedType.getDisplayName());
            initTree();
            selectedType = typeProxies.get(firstTypeId);
        } catch (CmisConnectException e) {
            Message.print(e.getMessage());
        } catch (CmisTypeDeleteException e) {
            Message.print("The type <" + selectedType.getDisplayName() + "> cannot be deleted");
        }
        hideDeleteDialog();
        return "";
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
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
}