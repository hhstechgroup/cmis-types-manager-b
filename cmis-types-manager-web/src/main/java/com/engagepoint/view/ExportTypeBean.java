package com.engagepoint.view;

import com.engagepoint.services.CmisService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * User: arkadiy.sychov (arkadiy.sychov@engagepoint.com )
 * Date: 12/12/13
 * Time: 5:50 PM
 */

@ManagedBean
@ViewScoped
public class ExportTypeBean {
    @EJB
    private CmisService service;

    private boolean isXmlOrJson;
    private boolean isIncludeChilds;

    public boolean isXmlOrJson() {
        return isXmlOrJson;
    }

    public void setXmlOrJson(boolean xmlOrJson) {
        isXmlOrJson = xmlOrJson;
        System.out.println("isXmlOrJson = !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + isXmlOrJson());
    }

    public boolean isIncludeChilds() {
        return isIncludeChilds;
    }

    public void setIncludeChilds(boolean includeChilds) {
        isIncludeChilds = includeChilds;
        System.out.println("isIncludeChilds = !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + isIncludeChilds());
    }
}
