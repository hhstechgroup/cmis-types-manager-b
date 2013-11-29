package com.engagepoint;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class TreeBean implements Serializable {
    @EJB
    private CmisService service;
    @Inject
    private LoginBean login;
    private TreeNode root;
    private CmisType selectedType;


    private List<CmisType> getListType(List<Tree<ObjectType>> treeList){
        List<CmisType> cmisTypeList = new ArrayList<CmisType>();

        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getTypeObject(tree.getItem()));
        }
        return cmisTypeList;
    }

    private CmisType getTypeObject(ObjectType objectType){
        CmisType cmisType = new CmisType();
        cmisType.setName(objectType.getDisplayName());
        cmisType.setId(objectType.getId());
        cmisType.setCreatable(objectType.isCreatable());
        cmisType.setFileable(objectType.isFileable());
        List<CmisType> children = new ArrayList<CmisType>();
        for (ObjectType child : objectType.getChildren()) {
            children.add(getTypeObject(child));
        }
        cmisType.setChildren(children);
        return cmisType;
    }


    public TreeNode getRoot() {
        root = new DefaultTreeNode("Root", null);
        LoginInfo loginInfo = login.getLoginInfo();
        List<Tree<ObjectType>> trees = null;

        try {
            trees = service.getTreeTypes(loginInfo);
        } catch (CmisConnectException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if (trees != null) {
            List<CmisType> cmisTypeList = getListType(trees);
            addTypesToTreeNode(cmisTypeList, root);
        }
        return root;
    }

    public CmisType getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(CmisType selectedDocument) {
        this.selectedType = selectedDocument;
    }

    private void addTypesToTreeNode(List<CmisType> cmisTypes, TreeNode parent) {
        for (CmisType cmisType : cmisTypes) {
            TreeNode node = new DefaultTreeNode(cmisType, parent);
            if (!cmisType.getChildren().isEmpty()) {
                addTypesToTreeNode(cmisType.getChildren(), node);
            }
        }
    }
}