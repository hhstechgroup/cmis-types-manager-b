package com.engagepoint.view;



import com.engagepoint.filters.LoginFilter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

/**
 * Created by michael.vakulik on 12/18/13.
 */
@ManagedBean
@RequestScoped
public class BackNavigationBean implements Serializable {

    public String getHistory(){
        return LoginFilter.history.toString();
    }

    public String goToLastPage() {
        System.out.println(LoginFilter.history.poll()+"dfffffffffffffffffffffffffffffffffffff");
        return LoginFilter.history.poll();
    }
}
