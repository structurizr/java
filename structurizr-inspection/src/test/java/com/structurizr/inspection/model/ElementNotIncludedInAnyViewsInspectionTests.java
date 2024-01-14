package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.inspection.Severity;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ElementNotIncludedInAnyViewsInspectionTests {

    @Test
    public void run_NotInViews() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");

        Violation violation = new ElementNotIncludedInAnyViewsInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.element.noview", violation.getType());
        assertEquals("The software system named \"Name\" is not included on any views - add it to a view.", violation.getMessage());
    }

    @Test
    public void run_InViews() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        workspace.getViews().createSystemLandscapeView("key", "Description").addAllElements();

        Violation violation = new ElementNotIncludedInAnyViewsInspection(workspace).run(softwareSystem);
        assertNull(violation);
    }

}
