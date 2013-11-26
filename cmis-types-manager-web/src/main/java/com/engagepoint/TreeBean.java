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
import java.util.ArrayList;
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
           List<Type> typeList = getListType(trees);
           addTypesToTreeNode(typeList, root);
        }
    }


    private List<Type> getListType(List<Tree<ObjectType>> treeList){
        List<Type> typeList = new ArrayList<Type>();

        for (Tree<ObjectType> tree : treeList) {
            typeList.add(getTypeObject(tree.getItem()));
        }
        return typeList;
    }

    private Type getTypeObject(ObjectType objectType){
        Type type = new Type();
        type.setName(objectType.getDisplayName());
        type.setId(objectType.getId());
        type.setCreatable(objectType.isCreatable());
        List<Type> children = new ArrayList<Type>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeObject(child));
        }
        type.setChildren(children);
        return type;
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

    private void addTypesToTreeNode(List<Type> types, TreeNode parent) {
        for (Type type : types) {
            TreeNode node = new DefaultTreeNode(type, parent);
            if (!type.getChildren().isEmpty()) {
                addTypesToTreeNode(type.getChildren(), node);
            }
        }
    }
}