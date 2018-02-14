package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StructurizrClientIntegrationTests {

    private StructurizrClient structurizrClient;

    @Before
    public void setUp() {
        structurizrClient = new StructurizrClient("81ace434-94a1-486f-a786-37bbeaa44e08", "a8673e21-7b6f-4f52-be65-adb7248be86b");
        structurizrClient.setWorkspaceArchiveLocation(null);
        structurizrClient.setMergeFromRemote(false);
    }

    @Test
    public void test_putAndGetWorkspace_WithoutEncryption() throws Exception {
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