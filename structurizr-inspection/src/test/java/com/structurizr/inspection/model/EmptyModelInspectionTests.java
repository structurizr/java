package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
EmptyModelInspectionTests {

    @Test
    public void run_WhenThereAreNoElements() {
        Workspace workspace = new Workspace("Name", "Description");

        Violation violation = new EmptyModelInspection(new DefaultInspector(workspace)).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.empty", violation.getType());
        assertEquals("Add some elements to the model.", violation.getMessage());
    }

    @Test
    public void run_WhenThereAreElements() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Name");

        Violation violation = new EmptyModelInspection(new DefaultInspector(workspace)).run();
        assertNull(violation);
    }

}
