package com.structurizr.api;

import com.structurizr.Workspace;
import com.structurizr.encryption.AesEncryptionStrategy;
import com.structurizr.encryption.EncryptedWorkspace;
import com.structurizr.io.json.EncryptedJsonReader;
import com.structurizr.io.json.JsonReader;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.SystemContextView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

public class WorkspaceApiClientIntegrationTests {

    private WorkspaceApiClient client;
    private final File workspaceArchiveLocation = new File(System.getProperty("java.io.tmpdir"), "structurizr");

    @BeforeEach
    void setUp() {
        client = new WorkspaceApiClient("81ace434-94a1-486f-a786-37bbeaa44e08", "a8673e21-7b6f-4f52-be65-adb7248be86b");
        client.setWorkspaceArchiveLocation(workspaceArchiveLocation);
        workspaceArchiveLocation.mkdirs();
        clearWorkspaceArchive();
        assertEquals(0, workspaceArchiveLocation.listFiles().length);
        client.setMergeFromRemote(false);
    }

    @AfterEach
    void tearDown() {
        clearWorkspaceArchive();
        workspaceArchiveLocation.delete();
    }

    private void clearWorkspaceArchive() {
        if (workspaceArchiveLocation.listFiles() != null) {
            for (File file : workspaceArchiveLocation.listFiles()) {
                file.delete();
            }
        }
    }

    private File getArchivedWorkspace() {
        return workspaceArchiveLocation.listFiles()[0];
    }

    @Test
    @Tag("IntegrationTest")
    void putAndGetWorkspace_WithoutEncryption() throws Exception {
        Workspace workspace = new Workspace("Structurizr client library tests - without encryption", "A test workspace for the Structurizr client library");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Person person = workspace.getModel().addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        SystemContextView systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        systemContextView.addAllElements();

        client.putWorkspace(20081, workspace);

        workspace = client.getWorkspace(20081);
        assertNotNull(workspace.getModel().getSoftwareSystemWithName("Software System"));
        assertNotNull(workspace.getModel().getPersonWithName("Person"));
        assertEquals(1, workspace.getModel().getRelationships().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());

        // and check the archive version is readable
        Workspace archivedWorkspace = new JsonReader().read(new FileReader(getArchivedWorkspace()));
        assertEquals(20081, archivedWorkspace.getId());
        assertEquals("Structurizr client library tests - without encryption", archivedWorkspace.getName());
        assertEquals(1, archivedWorkspace.getModel().getSoftwareSystems().size());

        assertEquals(1, workspaceArchiveLocation.listFiles().length);
    }

    @Test
    @Tag("IntegrationTest")
    void putAndGetWorkspace_WithEncryption() throws Exception {
        client.setEncryptionStrategy(new AesEncryptionStrategy("password"));
        Workspace workspace = new Workspace("Structurizr client library tests - with encryption", "A test workspace for the Structurizr client library");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        Person person = workspace.getModel().addPerson("Person", "Description");
        person.uses(softwareSystem, "Uses");
        SystemContextView systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        systemContextView.addAllElements();

        client.putWorkspace(20081, workspace);

        workspace = client.getWorkspace(20081);
        assertNotNull(workspace.getModel().getSoftwareSystemWithName("Software System"));
        assertNotNull(workspace.getModel().getPersonWithName("Person"));
        assertEquals(1, workspace.getModel().getRelationships().size());
        assertEquals(1, workspace.getViews().getSystemContextViews().size());

        // and check the archive version is readable
        EncryptedWorkspace archivedWorkspace = new EncryptedJsonReader().read(new FileReader(getArchivedWorkspace()));
        assertEquals(20081, archivedWorkspace.getId());
        assertEquals("Structurizr client library tests - with encryption", archivedWorkspace.getName());
        assertTrue(archivedWorkspace.getEncryptionStrategy() instanceof AesEncryptionStrategy);

        assertEquals(1, workspaceArchiveLocation.listFiles().length);
    }

    @Test
    @Tag("IntegrationTest")
    void lockWorkspace() throws Exception {
        client.unlockWorkspace(20081);
        assertTrue(client.lockWorkspace(20081));
    }


    @Test
    @Tag("IntegrationTest")
    void unlockWorkspace() throws Exception {
        client.lockWorkspace(20081);
        assertTrue(client.unlockWorkspace(20081));
    }

}