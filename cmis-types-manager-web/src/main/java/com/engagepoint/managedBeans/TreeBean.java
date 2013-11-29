package com.engagepoint.managedBeans;

import com.engagepoint.CmisConnectException;
import com.engagepoint.CmisService;
import com.engagepoint.LoginInfo;
import com.engagepoint.pojos.CmisType;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
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
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {
    @EJB
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    private TreeNode root;
    private CmisType selectedType;

    private List<CmisType> getListType(List<Tree<ObjectType>> treeList) {
        List<CmisType> cmisTypeList = new ArrayList<CmisType>();

        for (Tree<ObjectType> tree : treeList) {
            cmisTypeList.add(getTypeObject(tree.getItem()));
        }
        return cmisTypeList;
    }

    private CmisType getTypeObject(ObjectType objectType) {
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
        this.root = new DefaultTreeNode("Root", null);
        LoginInfo loginInfo = login.getLoginInfo();
        List<Tree<ObjectType>> trees = null;
        try {
            trees = service.getTreeTypes(loginInfo);
        } catch (CmisConnectException e) {
            FacesContext.getCurrentInstance().addMessage("exceptions", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
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

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}