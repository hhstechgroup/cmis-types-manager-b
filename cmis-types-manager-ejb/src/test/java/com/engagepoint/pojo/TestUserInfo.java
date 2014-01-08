package com.engagepoint.pojo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * User: victor.klymenko
 * Date: 1/6/14
 * Time: 10:35 AM
 */
public class TestUserInfo {
    private UserInfo userInfo = new UserInfo();

    @Before
    public void before() {
        userInfo.setUrl("url");
        userInfo.setPassword("password");
        userInfo.setUsername("userName");
    }

    @Test
    public void testReset() {
        Assert.assertNotNull(userInfo.getUrl());
        Assert.assertNotNull(userInfo.getUsername());
        Assert.assertNotNull(userInfo.getPassword());
        userInfo.reset();
        Assert.assertTrue(userInfo.getPassword().isEmpty());
        Assert.assertTrue(userInfo.getUsername().isEmpty());
        Assert.assertTrue(userInfo.getUrl().isEmpty());
    }

    @Test
    public void testGetAtomPubParameters(){
        Map<String, String> parameters = userInfo.getAtomPubParameters();
        Assert.assertNotNull(parameters);
        Assert.assertEquals("userName", parameters.get("org.apache.chemistry.opencmis.user"));
        Assert.assertEquals("url", parameters.get("org.apache.chemistry.opencmis.binding.atompub.url"));
        Assert.assertEquals("password", parameters.get("org.apache.chemistry.opencmis.password"));
    }
}
