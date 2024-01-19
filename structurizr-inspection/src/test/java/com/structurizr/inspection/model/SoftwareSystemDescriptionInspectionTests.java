package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
SoftwareSystemDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");

        Violation violation = new SoftwareSystemDescriptionInspection(new DefaultInspector(workspace)).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.softwaresystem.description", violation.getType());
        assertEquals("Add a description to the software system named \"Name\".", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        Violation violation = new SoftwareSystemDescriptionInspection(new DefaultInspector(workspace)).run(softwareSystem);
        assertNull(violation);
    }

}
