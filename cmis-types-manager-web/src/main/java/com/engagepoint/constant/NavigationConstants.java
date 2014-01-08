package com.engagepoint.constant;

import org.apache.commons.lang.StringUtils;

/**
 * User: Qnex
 * Date: 01.01.14
 * Time: 21:49
 */
public final class NavigationConstants {
    public static final String TO_LOGIN = "/login?faces-redirect=true";
    public static final String TO_CURRENT_PAGE = StringUtils.EMPTY;
    public static final String TO_MAIN_PAGE = "/dashboard/index?faces-redirect=true";
    public static final String REDIRECT_TRUE = "?faces-redirect=true";
    public static final String DASHBOARD_ROOT_VIEW = "/dashboard/";
    public static final String MAIN_ADDRESS = "index.xhtml";
    public static final String LOGIN_PAGE = "/login.xhtml";
    public static final String CONFIGURATION_ROOT_VIEW = "/configuration/";
    public static final String INFO_ROOT_VIEW = "/about/";

    private NavigationConstants() {
    }
}