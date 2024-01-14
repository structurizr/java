package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ContainerViewsForMultipleSoftwareSystemsInspectionTests {

    @Test
    public void run_MultipleSoftwareSystems() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createContainerView(a, "Containers-A", "Description");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        workspace.getViews().createContainerView(b, "Containers-B", "Description");

        Violation violation = new ContainerViewsForMultipleSoftwareSystemsInspection(workspace).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.workspace.scope", violation.getType());
        assertEquals("Container views exist for 2 software systems. It is recommended that a workspace includes container views for a single software system only.", violation.getMessage());
    }

    @Test
    public void run_SingleSoftwareSystem() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createContainerView(a, "Containers-A", "Description");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");

        Violation violation = new ContainerViewsForMultipleSoftwareSystemsInspection(workspace).run();
        assertNull(violation);
    }

}
