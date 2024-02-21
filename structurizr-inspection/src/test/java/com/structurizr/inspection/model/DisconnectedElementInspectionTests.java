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
DisconnectedElementInspectionTests {

    @Test
    public void run_WithDisconnectedElement() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");

        Violation violation = new DisconnectedElementInspection(new DefaultInspector(workspace)).run(a);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.element.disconnected", violation.getType());
        assertEquals("The software system \"A\" is disconnected - add a relationship to/from it, or consider removing it from the model.", violation.getMessage());
    }

    @Test
    public void run_WithoutDisconnectedElement() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        a.uses(b, "Uses");

        Violation violation = new DisconnectedElementInspection(new DefaultInspector(workspace)).run(a);
        assertNull(violation);

        violation = new DisconnectedElementInspection(new DefaultInspector(workspace)).run(b);
        assertNull(violation);
    }

}
