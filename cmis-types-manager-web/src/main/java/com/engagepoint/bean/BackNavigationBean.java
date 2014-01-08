package com.engagepoint.bean;



import com.engagepoint.filter.LoginFilter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * User: michael.vakulik
 * Date: 12/18/13
 * Time: 17:24
 */

@ManagedBean
@RequestScoped
public class BackNavigationBean implements Serializable {
    public String getHistory(){
        return LoginFilter.HISTORY.toString();
    }

    public String goToLastPage() {
        return LoginFilter.HISTORY.getFirst();
    }

}
