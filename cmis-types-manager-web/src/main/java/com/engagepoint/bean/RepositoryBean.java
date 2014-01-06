package com.engagepoint.bean;

import com.engagepoint.ejb.Service;
import com.engagepoint.exception.CmisException;
import com.engagepoint.util.MessageUtils;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.engagepoint.constant.MessageConstants.REPO_CHANGED;
import static com.engagepoint.constant.MessageConstants.UNABLE_INIT_REPO;

/**
 * User: arkadiy.sychov (arkadiy.sychov@engagepoint.com )
 * Date: 11/29/13
 * Time: 4:33 PM
 */
@ManagedBean
@SessionScoped
public class RepositoryBean implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryBean.class);
    @EJB
    private Service service;
    private String selectedRepoId;
    private List<SelectItem> selectItems;
    private Map<String, Repository> repositories;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            repositories = getRepositoryMapFrom(service.getRepositories(loginBean.getUserInfo()));
            selectItems = new ArrayList<SelectItem>();
            for (Repository repo : repositories.values()) {
                selectItems.add(new SelectItem(repo.getId(), repo.getName()));
            }
            selectedRepoId = repositories.values().iterator().next().getId();
        } catch (CmisException e) {
            MessageUtils.printError(e.getMessage());
            LOGGER.error(UNABLE_INIT_REPO, e);
        }
    }

    public void changeRepository() {
        loginBean.getUserInfo().setRepository(selectedRepoId);
        MessageUtils.printInfo(REPO_CHANGED);
    }

    public String getSelectedRepoId() {
        return selectedRepoId;
    }

    public void setSelectedRepoId(String selectedRepoId) {
        this.selectedRepoId = selectedRepoId;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    public RepositoryInfo getInfo() {
        return repositories.get(selectedRepoId);
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    private Map<String, Repository> getRepositoryMapFrom(List<Repository> list) {
        Map<String, Repository> map = new LinkedHashMap<String, Repository>();
        for (Repository el : list) {
            map.put(el.getId(), el);
        }
        return map;
    }

}

