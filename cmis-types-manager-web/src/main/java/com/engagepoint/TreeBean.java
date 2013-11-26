package com.engagepoint;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class TreeBean implements Serializable {
    @EJB
    private CmisService service;
    @Inject
    private LoginController login;
    private TreeNode root;

    private TreeNode selectedNode;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        LoginInfo loginInfo = login.getLoginInfo();
        List<Tree<ObjectType>> trees = service.getTreeTypes(loginInfo);
        if (trees != null) {
            addTypesToTreeNode(trees, root);
        }
    }

    public TreeBean() {


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

    private void addTypesToTreeNode(List<Tree<ObjectType>> trees, TreeNode parent) {
        for (Tree<ObjectType> tree : trees) {
            TreeNode node = new DefaultTreeNode(tree.getItem().getDisplayName(), parent);
            if (!tree.getChildren().isEmpty()) {
                addTypesToTreeNode(tree.getChildren(), node);
            }
        }
    }
}