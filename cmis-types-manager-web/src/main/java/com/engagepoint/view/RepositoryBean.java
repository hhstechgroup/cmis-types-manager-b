package com.engagepoint.view;

import com.engagepoint.utils.MessageUtils;
import com.engagepoint.constants.Constants;
import com.engagepoint.exceptions.CmisException;
import com.engagepoint.services.CmisService;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: arkadiy.sychov (arkadiy.sychov@engagepoint.com )
 * Date: 11/29/13
 * Time: 4:33 PM
 */
@ManagedBean
@ViewScoped
public class RepositoryBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryBean.class);

    @EJB
    private CmisService service;
    private List<Repository> repositories;
    private String selectedRepoId;
    private List<SelectItem> repositoryList;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            repositories = service.getRepositories(loginBean.getUserInfo());
            repositoryList = new ArrayList<SelectItem>();
            for (Repository repo : repositories) {
                repositoryList.add(new SelectItem(repo.getId(), repo.getName()));
            }
            selectedRepoId = repositories.get(0).getId();
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(Constants.Messages.UNABLE_INIT_REPO, e);
        }
    }

    public void updateMainContent() {
        loginBean.getUserInfo().setRepositoryId(getSelectedRepoId());
        MessageUtils.printInfo(Constants.RepoManager.REPO_CHANGED);
    }

    public String getSelectedRepoId() {
        return selectedRepoId;
    }

    public void setSelectedRepoId(String selectedRepoId) {
        this.selectedRepoId = selectedRepoId;
    }

    public List<SelectItem> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<SelectItem> repositoryList) {
        this.repositoryList = repositoryList;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}

