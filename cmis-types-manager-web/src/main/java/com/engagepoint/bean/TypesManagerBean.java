package com.engagepoint.bean;


import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.exception.CmisTypeDeleteException;
import com.engagepoint.pojo.TypeProxy;
import com.engagepoint.pojo.UserInfo;
import com.engagepoint.util.CustomStringUtils;
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
import java.util.Collections;
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
    private TypeProxy selectedType;
    private List<TypeProxy> types;

    @PostConstruct
    public void init() {
        types = getTypes();
        setTypeToHolder();
        initTree();
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

    public void onNodeSelect(NodeSelectEvent event) {
        selectedType = (TypeProxy) event.getTreeNode().getData();
        selectedTypeHolder.setType(selectedType);
    }

    public void deleteType() {
        try {
            service.deleteType(login.getUserInfo(), selectedType);
            MessageUtils.printInfo(TYPE_DELETED + selectedType.getDisplayName());
            setDefaultTypeToHolder();
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(ERROR_DELETE_TYPE, e);
        } catch (CmisTypeDeleteException e) {
//            TODO change concat method
            MessageUtils.printError(CustomStringUtils.concat(DELETE_MESSAGE_PREFIX, selectedType.getDisplayName(), DELETE_MESSAGE_SUFFIX));
            LOGGER.error(UNABLE_DELETE_TYPE, e);
        }
    }

    public void deleteTypeWithSubtypes() {
        if (typeHasSubtypes(selectedType)) {
            deleteType(login.getUserInfo(), selectedType);
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

    public List<TypeProxy> getTypes() {
        try {
            List<TypeProxy> typeProxies = service.getAllTypes(login.getUserInfo());
            return typeProxies;
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_SET_SELECTED_TYPE, e);
        }
        return Collections.EMPTY_LIST;
    }

    public SelectedTypeHolderBean getSelectedTypeHolder() {
        return selectedTypeHolder;
    }

    public void setSelectedTypeHolder(SelectedTypeHolderBean selectedTypeHolder) {
        this.selectedTypeHolder = selectedTypeHolder;
    }

    public String getDeleteMessage() {
        if (typeHasSubtypes(selectedType)) {
            return CustomStringUtils.concat("\"", selectedType.getDisplayName(), "\" type has children. Are you sure you want to delete?");
        } else {
            return CustomStringUtils.concat("Are you sure you want to delete \"", selectedType.getDisplayName(), "\" type ?");
        }
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    private void deleteType(UserInfo userInfo, TypeProxy selectedType) {
        try {
            if (!(selectedType.getTypeMutability().canDelete())) {
                MessageUtils.printError(CustomStringUtils.concat(DELETE_MESSAGE_PREFIX, selectedType.getDisplayName(), DELETE_MESSAGE_SUFFIX));
            } else {
                List<TypeProxy> selectedTypeChildren = selectedType.getChildren();
                for (TypeProxy selectedTypeChild : selectedTypeChildren) {
                    deleteType(userInfo, selectedTypeChild);
                }
                service.deleteType(userInfo, selectedType);
                MessageUtils.printInfo(TYPE_DELETED + selectedType.getDisplayName());
                setDefaultTypeToHolder();
            }
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(ERROR_DELETE_TYPE, e);
        } catch (CmisTypeDeleteException e) {
            MessageUtils.printError(CustomStringUtils.concat(DELETE_MESSAGE_PREFIX, selectedType.getDisplayName(), DELETE_MESSAGE_SUFFIX));
            LOGGER.error(UNABLE_DELETE_TYPE, e);
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

    private void addTypesToTree(List<TypeProxy> types, TreeNode parent) {
        TypeProxy selectType = selectedTypeHolder.getType();
        for (TypeProxy type : types) {
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

    private void setDefaultTypeToHolder() {
        selectedType = types.get(NumberUtils.INTEGER_ZERO);
        selectedTypeHolder.setType(selectedType);
    }
}