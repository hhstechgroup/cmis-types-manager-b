package com.engagepoint.constants;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/18/13
 * Time: 3:22 PM
 */

public class Constants {
    public static final class Navigation{
        public static final String TO_LOGIN = "/login?faces-redirect=true";
        public static final String TO_CURRENT_PAGE = "";
        public static final String TO_MAIN_PAGE = "/dashboard/index?faces-redirect=true";
        public static final String TO_CREATE_TYPE = "crud/newCreate?faces-redirect=true";
//        public static final String TO_VIEW_TYPE = "crud/type?faces-redirect=true";
//        public static final String TO_UPDATE_TYPE = "crud/type?faces-redirect=true";

    }

    public static final class Strings{
        public static final String EMPTY_STRING = "";
    }

    public static final class Messages{
        public static final String UNABLE_DELETE_TYPE = "Unable to delete type";
        public static final String ERROR_DELETE_TYPE = "Error while deleting type";
        public static final String TYPE_DELETED = "Deleted type ";
        public static final String UNABLE_INIT_TYPE_VIEW = "Unable to initialise type view";
        public static final String UNABLE_INIT_REPO = "Unable to initialization repositories";
    }

    public static final class TypesManager{
        public static final String TREE_DATA = "Root";
        public static final String MUTABILITY_DELETE_DISPLAY_NAME = "Delete ";
        public static final String MUTABILITY_UPDATE_DISPLAY_NAME = "Update ";
        public static final String MUTABILITY_CREATE_DISPLAY_NAME = "Create ";
        public static final int FIRST_TYPE_ID = 0;
    }

    public static final class RepoManager{
        public static final String REPO_CHANGED = "Repository changed successfully";
    }

    public static final class TopMenuBar{
        public static final String DASHBOARD_MID =  "dashboard";
        public static final String DASHBOARD_ROOT_VIEW = "/dashboard/";
        public static final String DASHBOARD_ADDRESS = "index.xhtml";
        public static final String DASHBOARD_LABEL = "Types Management";

        public static final String CONFIGURATION_MID = "configuration";
        public static final String CONFIGURATION_ROOT_VIEW = "/configuration/";
        public static final String CONFIGURATION_ADDRESS = "index.xhtml";
        public static final String CONFIGURATION_LABEL =  "Configuration";

        public static final String INFO_MID = "info";
        public static final String INFO_ROOT_VIEW = "/about/";
        public static final String INFO_ADDRESS = "index.xhtml";
        public static final String INFO_LABEL =  "About";

        public static final String STYLE_CLASS = "active";
    }
}
