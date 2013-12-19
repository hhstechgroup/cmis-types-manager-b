package com.engagepoint.constants;

/**
 * User: vyacheslav.polulyakh (vyacheslav.polulyakh@engagepoint.com )
 * Date: 12/18/13
 * Time: 3:22 PM
 */

public class Constants {
    private Constants() {
    }

    public static final class Navigation {
        public static final String TO_LOGIN = "/login?faces-redirect=true";
        public static final String TO_CURRENT_PAGE = "";
        public static final String TO_MAIN_PAGE = "/dashboard/index?faces-redirect=true";
//        public static final String TO_CREATE_TYPE = "crud/create?faces-redirect=true";
//        public static final String TO_VIEW_TYPE = "crud/type?faces-redirect=true";
//        public static final String TO_UPDATE_TYPE = "crud/type?faces-redirect=true";

    }

    public static final class Strings {
        private Strings() {
        }

        public static final String EMPTY_STRING = "";
        public static final String SESSION_ID_DISPLAY_NAME = "sessionID";
        public static final String XML_PATTERN = "xml";
        public static final String DISPOSITION = "Content-Disposition";
        public static final String ATTACHMENT_FILE_NAME = "attachment; filename=\"";
    }

    public static final class Messages {
        private Messages() {
        }

        public static final String UNABLE_DELETE_TYPE = "Unable to delete type";
        public static final String ERROR_DELETE_TYPE = "Error while deleting type";
        public static final String TYPE_DELETED = "Deleted type ";
        public static final String UNABLE_INIT_TYPE_VIEW = "Unable to initialise type view";
        public static final String UNABLE_INIT_REPO = "Unable to initialization repositories";
        public static final String UNABLE_SET_SELECTED_TYPE = "Unable to set selected type";
        public static final String NOT_FOUND = "Not Found";
        public static final String REPO_NOT_EXISTS = "The repository on this URL doesn't exist!";
        public static final String UNEXPECTED_DOCUMENT = "Unexpected document! Received: something unknown";

        public static final String UNABLE_UPLOAD_FILE = "Unable to upload file";
        public static final String SUCCESS_IMPORT_TYPE = "Type imported successful!";
        public static final String NOT_SELECTED_FILE = "File is not selected";
        public static final String ERROR_IMPORT_TYPE = "Error while import type";
        public static final String ERROR_EXPORT_TYPE = "Error while exporting type";
        public static final String UNABLE_CREATE_TYPE = "Unable to create type";

        public static final String UNABLE_TO_INIT_VIEW = "Unable to initialise type view";

        public static final String TYPE_CREATED = " type created!";
        public static final String DELETE_MESSAGE_PREFFIX = "The type <";
        public static final String DELETE_MESSAGE_SUFFIX = "> cannot be deleted";
    }

    public static final class TypesManager {
        private TypesManager() {
        }

        public static final String TREE_DATA = "Root";
        public static final String MUTABILITY_DELETE_DISPLAY_NAME = "Delete ";
        public static final String MUTABILITY_UPDATE_DISPLAY_NAME = "Update ";
        public static final String MUTABILITY_CREATE_DISPLAY_NAME = "Create ";

        public static final String CMIS_SECONDARY = "cmis:secondary";

    }

    public static final class Integers {
        private Integers() {
        }

        public static final int ZERO = 0;
    }

    public static final class RepoManager {
        private RepoManager() {
        }

        public static final String REPO_CHANGED = "Repository changed successfully";
    }

    public static final class TopMenuBar {
        private TopMenuBar() {
        }

        public static final String DASHBOARD_MID = "dashboard";
        public static final String DASHBOARD_ROOT_VIEW = "/dashboard/";
        public static final String DASHBOARD_ADDRESS = "index.xhtml";
        public static final String DASHBOARD_LABEL = "Types Management";

        public static final String CONFIGURATION_MID = "configuration";
        public static final String CONFIGURATION_ROOT_VIEW = "/configuration/";
        public static final String CONFIGURATION_ADDRESS = "index.xhtml";
        public static final String CONFIGURATION_LABEL = "Configuration";

        public static final String INFO_MID = "info";
        public static final String INFO_ROOT_VIEW = "/about/";
        public static final String INFO_ADDRESS = "index.xhtml";
        public static final String INFO_LABEL = "About";

        public static final String STYLE_CLASS_ACTIVE = "active";
    }
}
