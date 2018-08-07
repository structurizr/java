package com.structurizr;

import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class WorkspaceTests {

    private Workspace workspace = new Workspace("Name", "Description");

    @Test
    public void test_isEmpty_ReturnsTrue_WhenThereAreNoElementsViewsOrDocumentation() {
        workspace = new Workspace("Name", "Description");
        assertTrue(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereAreElements() {
        workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("Name", "Description");
        assertFalse(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereAreViews() {
        workspace = new Workspace("Name", "Description");
        workspace.getViews().createSystemLandscapeView("key", "Description");
        assertFalse(workspace.isEmpty());
    }

    @Test
    public void test_isEmpty_ReturnsFalse_WhenThereIsDocumentation() throws Exception {
        workspace = new Workspace("Name", "Description");
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addImage(new File("../docs/images/structurizr-logo.png"));
        assertFalse(workspace.isEmpty());
    }

    @Test
    public void test_countAndLogWarnings() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1", null);
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2", " ");
        Container container1 = softwareSystem1.addContainer("Name", "Description", null);
        Container container2 = softwareSystem2.addContainer("Name", "Description", " ");
        container1.uses(container2, null, null);
        container2.uses(container1, " ", " ");

        Component component1A = container1.addComponent("A", null, null);
        Component component1B = container1.addComponent("B", "", "");
        component1A.uses(component1B, null);
        component1B.uses(component1A, "");

        assertEquals(10, workspace.countAndLogWarnings());
    }

    @Test
    public void test_hydrate_DoesNotCrash() {
        Workspace workspace = new Workspace("Name", "Description");
        assertNotNull(workspace.getViews());
        assertNotNull(workspace.getDocumentation());

        // check that the hydrate method doesn't crash (it includes some method calls via reflection)
        workspace.hydrate();
    }

}