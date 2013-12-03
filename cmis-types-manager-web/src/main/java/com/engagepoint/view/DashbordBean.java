package com.engagepoint.view;


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
 * User: alexdenisenko
 * Date: 11/24/13
 * Time: 0:24 PM
 */
@ManagedBean
@ViewScoped
public class DashbordBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TreeNode root;
    private TreeNode selectedNode;
    //private List<String> repositories;
    private String selectedType;
    private Boolean isShowDialog;
    @ManagedProperty(value = "#{navigation}")
    private NavigationBean navigationBean;

    @PostConstruct
    public void init() throws CmisConnectException {
        System.out.println("init DashbordBean!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        initSelector();
        initTreeTable();
        this.isShowDialog = false;
    }

//    private void initSelector() throws CmisConnectException {
//        repositories = service.getRepositoriesNames(login.getUserInfo());
//    }
    

   

    private void initTreeTable() throws CmisConnectException {
        root = new DefaultTreeNode("Root", null);
        UserInfo userInfo = login.getUserInfo();
//        List<CmisType> types = service.getTreeTypes(userInfo);
        List<TypeProxy> typeProxies = service.getTypeInfo(userInfo);
        int firstTypeId = 0;
        selectedType = typeProxies.get(firstTypeId).getId();
        addTypesToTree(typeProxies, root);
    }

    public String goTypePage() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedType", selectedType);
        return navigationBean.toViewType();
    }

    public String goCreatePage() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedType", selectedType);
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
        TypeProxy typeProxy = (TypeProxy) event.getTreeNode().getData();
        selectedType = typeProxy.getId();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
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
//
//    public List<String> getRepositories() {
//        return repositories;
//    }


    public Boolean getShowDialog() {
        return isShowDialog;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public void hide() {
        this.isShowDialog = false;
    }

    public void show() {
        this.isShowDialog = true;
    }

    public String deleteType() {
        try {
            UserInfo userInfo = login.getUserInfo();
            service.deleteType(userInfo, selectedType);
            FacesContext.getCurrentInstance().addMessage("infoPanel", new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted type" + selectedType, ""));
        } catch (CmisConnectException e) {
            FacesContext.getCurrentInstance().addMessage("infoPanel", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        } catch (CmisTypeDeleteException e) {
            FacesContext.getCurrentInstance().addMessage("infoPanel", new FacesMessage(FacesMessage.SEVERITY_ERROR, "The type <"+ selectedType + "> cannot be deleted", ""));
        }


        this.isShowDialog = false;
        return "";
    }
}