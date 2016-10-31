package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void test_putAndGetWorkspace_WithoutEncryption() throws Exception {
        // this isn't really a unit test...
        structurizrClient = new StructurizrClient("81ace434-94a1-486f-a786-37bbeaa44e08", "a8673e21-7b6f-4f52-be65-adb7248be86b");
        Workspace workspace = new Workspace("Structurizr client library tests", "A test workspace for the Structurizr client library");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Person person = workspace.getModel().addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        SystemContextView systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        systemContextView.addAllElements();

        structurizrClient.putWorkspace(20081, workspace);

        workspace = structurizrClient.getWorkspace(20081);
        assertTrue(workspace.getModel().contains(softwareSystem));
        assertTrue(workspace.getModel().contains(person));
        assertEquals(1, workspace.getModel().getRelationships().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
    }

    @Test
    public void test_putAndGetWorkspace_WithEncryption() throws Exception {
        // this isn't really a unit test...
        structurizrClient = new StructurizrClient("81ace434-94a1-486f-a786-37bbeaa44e08", "a8673e21-7b6f-4f52-be65-adb7248be86b");
        structurizrClient.setEncryptionStrategy(new AesEncryptionStrategy("password"));
        Workspace workspace = new Workspace("Structurizr client library tests", "A test workspace for the Structurizr client library");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Person person = workspace.getModel().addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        SystemContextView systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        systemContextView.addAllElements();

        structurizrClient.putWorkspace(20081, workspace);

        workspace = structurizrClient.getWorkspace(20081);
        assertTrue(workspace.getModel().contains(softwareSystem));
        assertTrue(workspace.getModel().contains(person));
        assertEquals(1, workspace.getModel().getRelationships().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());
    }

}
