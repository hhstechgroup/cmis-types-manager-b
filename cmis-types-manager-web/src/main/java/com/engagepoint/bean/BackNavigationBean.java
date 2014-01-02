package com.engagepoint.bean;



import com.engagepoint.filter.LoginFilter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * Created by michael.vakulik on 12/18/13.
 */
@ManagedBean
@RequestScoped
public class BackNavigationBean implements Serializable {
//  TODO Change this crap
    public String getHistory(){
        return LoginFilter.history.toString();
    }

    public String goToLastPage() {
        return LoginFilter.history.getFirst();
    }

}
