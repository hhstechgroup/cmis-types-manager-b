package com.engagepoint.services;

import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.HashMap;
import java.util.Map;

/**
 * User: AlexDenisenko
 * Date: 28.12.13
 * Time: 10:02
 */
public class UserInfo {
    private static final String CMIS_SPECIFICATION = "/services/";
    private String username;
    private String password;
    private String url;
    private String repositoryId;


    public UserInfo() {
        username = "";
        password = "";
        url = "";
        repositoryId = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRepositoryId() {
        return repositoryId;
    }


    public void setRepository(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void reset() {
        username = "";
        password = "";
        url = "";
    }

    public Map<String, String> getAtomPubParameters() {
        return new HashMap<String, String>() {
            {
                put(SessionParameter.USER, username);
                put(SessionParameter.PASSWORD, password);
                put(SessionParameter.ATOMPUB_URL, url);
                put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
                put(SessionParameter.REPOSITORY_ID, repositoryId);
            }
        };
    }

    public Map<String, String> getSoapParameters() {
        return new HashMap<String, String>() {
            {
                put(SessionParameter.USER, username);
                put(SessionParameter.PASSWORD, password);
                put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
                put(SessionParameter.WEBSERVICES_ACL_SERVICE, url + CMIS_SPECIFICATION + "ACLService?wsdl");
                put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, url + CMIS_SPECIFICATION +"DiscoveryService?wsdl");
                put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, url + CMIS_SPECIFICATION +"MultiFilingService?wsdl");
                put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, url + CMIS_SPECIFICATION + "NavigationService?wsdl");
                put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, url + CMIS_SPECIFICATION + "ObjectService?wsdl");
                put(SessionParameter.WEBSERVICES_POLICY_SERVICE, url + CMIS_SPECIFICATION + "PolicyService?wsdl");
                put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, url + CMIS_SPECIFICATION + "RelationshipService?wsdl");
                put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url + CMIS_SPECIFICATION + "RepositoryService?wsdl");
                put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, url + CMIS_SPECIFICATION + "VersioningService?wsdl");
                put(SessionParameter.REPOSITORY_ID, repositoryId);

            }
        };
    }

}
