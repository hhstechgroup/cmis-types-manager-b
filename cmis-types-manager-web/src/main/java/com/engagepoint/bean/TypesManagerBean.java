package com.engagepoint.bean;


import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.exception.CmisTypeDeleteException;
import com.engagepoint.pojo.Type;
import com.engagepoint.util.MessageUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.primefaces.event.NodeSelectEvent;
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

import static com.engagepoint.constant.MessageConstants.*;
import static com.engagepoint.constant.NameConstants.TREE_DATA;

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
    private Service service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{selectedTypeHolderBean}")
    private SelectedTypeHolderBean selectedTypeHolder;
    private TreeNode root;
    private TreeNode selectedNode;
    private Type selectedType;
    private List<Type> types;

    @PostConstruct
    public void init() {
        refreshTypes();
        setTypeToHolder();
        initTree();
    }

    public void onNodeSelect(NodeSelectEvent event) {
        selectedType = (Type) event.getTreeNode().getData();
        selectedTypeHolder.setType(selectedType);
    }

    public void deleteType() {
        deleteTypeWithSubtypes(selectedType);
        refreshTypes();
        setDefaultTypeToHolder();
        initTree();
    }

    public Type getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(Type selectedType) {
        this.selectedType = selectedType;
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

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    public String getDeleteMessage() {
        if (hasTypeSubtypes(selectedType)) {
            return String.format(DELETE_WITH_SUBTYPES, selectedType.getDisplayName());
        } else {
            return String.format(DELETE_WITHOUT_SUBTYPES, selectedType.getDisplayName());
        }
    }

    private void deleteTypeWithSubtypes(Type selectedType) {
        try {
            if (selectedType.getTypeMutability().canDelete()) {
                List<Type> selectedTypeChildren = selectedType.getChildren();
                for (Type selectedTypeChild : selectedTypeChildren) {
                    deleteTypeWithSubtypes(selectedTypeChild);
                }
                service.deleteType(login.getUserInfo(), selectedType.getId());
                MessageUtils.printInfo(TYPE_DELETED + selectedType.getDisplayName());
            } else {
                MessageUtils.printError(String.format(CAN_NOT_DELETE, selectedType.getDisplayName()));
            }
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(ERROR_DELETE_TYPE, e);
        } catch (CmisTypeDeleteException e) {
            MessageUtils.printError(String.format(CAN_NOT_DELETE, selectedType.getDisplayName()));
            LOGGER.error(UNABLE_DELETE_TYPE, e);
        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    private void refreshTypes() {
        try {
            types = service.findAllTypes(login.getUserInfo());
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_SET_SELECTED_TYPE, e);
        }
    }

    private void initTree() {
        root = new DefaultTreeNode(TREE_DATA, null);
        addTypesToTree(types, root);
    }

    private void setTypeToHolder() {
        if (selectedTypeHolder.getType() == null) {
            setDefaultTypeToHolder();
        } else {
            selectedType = selectedTypeHolder.getType();
        }
    }

    private void addTypesToTree(List<Type> types, TreeNode parent) {
        Type selectType = selectedTypeHolder.getType();
        for (Type type : types) {
            TreeNode node = new DefaultTreeNode(type, parent);
            if (type.getId().equals(selectType.getId())) {
                expandNodes(node);
            }
            if (!type.getChildren().isEmpty()) {
                addTypesToTree(type.getChildren(), node);
            }
        }
    }

    private void expandNodes(TreeNode node) {
        TreeNode treeNode = node;
        treeNode.setSelected(true);
        while (treeNode.getParent() != null) {
            treeNode.getParent().setExpanded(true);
            treeNode = treeNode.getParent();
        }
    }

    private boolean hasTypeSubtypes(Type type) {
        return !type.getChildren().isEmpty();
    }

    private void setDefaultTypeToHolder() {
        selectedType = types.get(NumberUtils.INTEGER_ZERO);
        selectedTypeHolder.setType(selectedType);
    }
}