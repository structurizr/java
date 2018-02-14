package com.structurizr.api;

import com.structurizr.Workspace;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StructurizrClientTests {

    private StructurizrClient structurizrClient;

    @Test
    public void test_setUrl_RemovesTheTrailingSlash_WhenATrailingSlashIsAdded() {
        structurizrClient = new StructurizrClient("https://api.structurizr.com/", "key", "secret");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());

        structurizrClient.setUrl("https://api.structurizr.com/");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());
    }

    @Test
    public void test_setUrl() {
        structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());

        structurizrClient.setUrl("https://api.structurizr.com");
        assertEquals("https://api.structurizr.com", structurizrClient.getUrl());
    }

    @Test(expected = StructurizrClientException.class)
    public void test_putWorkspace_ThrowsAnException_WhenANullWorkspaceIsSpecified() throws Exception {
        structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        structurizrClient.putWorkspace(1234, null);
    }

    @Test(expected = StructurizrClientException.class)
    public void test_putWorkspace_ThrowsAnException_WhenTheWorkspaceIdIsNotSet() throws Exception {
        structurizrClient = new StructurizrClient("https://api.structurizr.com", "key", "secret");
        Workspace workspace = new Workspace("Name", "Description");
        structurizrClient.putWorkspace(0, workspace);
    }

    @Test
    public void test_constructionWithAPropertiesFile_ThrowsAnException_WhenNoPropertiesAreFound() {
        try {
            structurizrClient = new StructurizrClient();
            fail();
        } catch (Exception e) {
            assertEquals("Could not find a structurizr.properties file on the classpath.", e.getMessage());
        }
    }

}
