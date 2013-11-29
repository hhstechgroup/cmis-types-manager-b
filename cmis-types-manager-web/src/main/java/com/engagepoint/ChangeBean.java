package com.engagepoint;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * User: ${Michael Vakulik}
 * Date: ${DATE}
 * Time: ${TIME}
 */
@ManagedBean(name = "changeBean")
@RequestScoped
public class ChangeBean {

    public String goTo() {
        return "changeType";
    }
    public String goToback() {
        return "index";
    }
}
