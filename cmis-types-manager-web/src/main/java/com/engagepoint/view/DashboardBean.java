package com.engagepoint.view;


import com.engagepoint.exceptions.CmisConnectException;
import com.engagepoint.services.CmisService;

import com.engagepoint.services.CmisType;
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
public class DashboardBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TreeNode root;
    private TreeNode selectedNode;
    private List<String> repositories;

    @PostConstruct
    public void init() throws CmisConnectException {
        System.out.println("init DashbordBean!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        initSelector();
        initTreeTable();
    }

    private void initSelector() throws CmisConnectException {
        repositories = service.getRepositoriesNames(login.getUserInfo());
    }

    private void initTreeTable() throws CmisConnectException {
        UserInfo userInfo = login.getUserInfo();
        root = new DefaultTreeNode("Root", null);
        List<CmisType> types = service.getTreeTypes(userInfo);
        addTypesToTableTree(types, root);
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
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());

        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void addTypesToTableTree(List<CmisType> cmisTypes, TreeNode parent) {
        for (CmisType cmisType : cmisTypes) {
            TreeNode node = new DefaultTreeNode(cmisType.getName(), parent);
            if (!cmisType.getChildren().isEmpty()) {
                addTypesToTableTree(cmisType.getChildren(), node);
            }
        }
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }
}