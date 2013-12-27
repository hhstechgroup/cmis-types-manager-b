package com.engagepoint.bean;


import com.engagepoint.util.CustomStringUtils;
import com.engagepoint.util.MessageUtils;
import com.engagepoint.constant.Constants;
import com.engagepoint.exception.CmisException;
import com.engagepoint.exception.CmisTypeDeleteException;
import com.engagepoint.service.CmisService;
import com.engagepoint.service.TypeProxy;
import com.engagepoint.service.UserInfo;
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
    private CmisService service;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean login;
    @ManagedProperty(value = "#{sessionStateBean}")
    private SessionStateBean sessionStateBean;

    private UserInfo userInfo;
    private TreeNode root;
    private TreeNode selectedNode;
    private TypeProxy selectedType;
    private List<TypeProxy> typeProxies;


    @PostConstruct
    public void init() {
        userInfo = login.getUserInfo();
        initTree();
    }

    private void initTree() {
            root = new DefaultTreeNode(Constants.TypesManager.TREE_DATA, null);
            setSelectedType();
            addTypesToTree(typeProxies, root);
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
        sessionStateBean.setTypeProxy(selectedType);
    }

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public void deleteType() {
        try {
            service.deleteType(userInfo, selectedType);
            MessageUtils.printInfo(Constants.Messages.TYPE_DELETED + selectedType.getDisplayName());
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.ERROR_DELETE_TYPE, e);
        } catch (CmisTypeDeleteException e) {
            MessageUtils.printError(CustomStringUtils.concatenate(Constants.Messages.DELETE_MESSAGE_PREFFIX, selectedType.getDisplayName(), Constants.Messages.DELETE_MESSAGE_SUFFIX));
            LOGGER.error(Constants.Messages.UNABLE_DELETE_TYPE, e);
        }
    }

    private void deleteType(UserInfo userInfo, TypeProxy selectedType) {

        try {
            if (!(selectedType.getTypeMutability().canDelete())){
                MessageUtils.printError(CustomStringUtils.concatenate(Constants.Messages.DELETE_MESSAGE_PREFFIX, selectedType.getDisplayName(), Constants.Messages.DELETE_MESSAGE_SUFFIX));

            } else {
                List<TypeProxy> selectedTypeChildren = selectedType.getChildren();
                for (TypeProxy selectedTypeChild : selectedTypeChildren) {
                    deleteType(userInfo, selectedTypeChild);
                }
                service.deleteType(userInfo, selectedType);
                MessageUtils.printInfo(Constants.Messages.TYPE_DELETED + selectedType.getDisplayName());
            }


        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.ERROR_DELETE_TYPE, e);
        } catch (CmisTypeDeleteException e) {
            MessageUtils.printError(CustomStringUtils.concatenate(Constants.Messages.DELETE_MESSAGE_PREFFIX, selectedType.getDisplayName(), Constants.Messages.DELETE_MESSAGE_SUFFIX));
            LOGGER.error(Constants.Messages.UNABLE_DELETE_TYPE, e);
        }
    }

    public void deleteTypeWithSubtypes() {
        if (typeHasSubtypes(selectedType)) {
            deleteType(userInfo, selectedType);
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

    public void setSelectedType() {
        try {
            typeProxies = service.getTypeInfo(userInfo);
            selectedType = typeProxies.get(Constants.Integers.ZERO);
            sessionStateBean.setTypeProxy(selectedType);
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_SET_SELECTED_TYPE, e);
        }
    }

    public SessionStateBean getSessionStateBean() {
        return sessionStateBean;
    }

    public void setSessionStateBean(SessionStateBean sessionStateBean) {
        this.sessionStateBean = sessionStateBean;
    }

    private void addTypesToTree(List<TypeProxy> types, TreeNode parent) {
        for (TypeProxy type : types) {
            TreeNode node = new DefaultTreeNode(type, parent);
            if (!type.getChildren().isEmpty()) {
                addTypesToTree(type.getChildren(), node);
            }
        }
    }

    public String getDeleteMessage() {
        if (typeHasSubtypes(selectedType)) {
            return CustomStringUtils.concatenate("\"", selectedType.getDisplayName(), "\" type has children. Are you sure you want to delete?");
        } else {
            return CustomStringUtils.concatenate("Are you sure you want to delete \"", selectedType.getDisplayName(), "\" type ?");
        }
    }
}