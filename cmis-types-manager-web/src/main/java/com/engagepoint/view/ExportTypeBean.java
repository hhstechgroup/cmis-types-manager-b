package com.engagepoint.view;

import com.engagepoint.components.MultiPageMessagesSupport;
import com.engagepoint.services.CmisService;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
        MultiPageMessagesSupport.printInfo("If xml - true, if json - false" + isXmlOrJson);
       // System.out.println("isXmlOrJson = !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + isXmlOrJson());
    }

    public boolean isIncludeChilds() {
        return isIncludeChilds;
    }

    public void setIncludeChilds(boolean includeChilds) {
        isIncludeChilds = includeChilds;
        Message.printError("If includeChild - true, if not includeChild - false" + isIncludeChilds);

        // System.out.println("isIncludeChilds = !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + isIncludeChilds());
    }

    public void ajaxTest(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("  " + isIncludeChilds));
    }
}
