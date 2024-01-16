package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Format;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SoftwareSystemDecisionInspectionTests {

    @Test
    public void run_WithoutDecision() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name");

        Violation violation = new SoftwareSystemDecisionsInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.softwaresystem.decisions", violation.getType());
        assertEquals("The software system named \"Software System\" has containers, but is missing decisions.", violation.getMessage());
    }

    @Test
    public void run_WithDecision() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name", "Description");

        Decision decision = new Decision("1");
        decision.setFormat(Format.Markdown);
        decision.setTitle("Decision 1");
        decision.setContent("Content");
        decision.setStatus("Accepted");
        softwareSystem.getDocumentation().addDecision(decision);

        Violation violation = new SoftwareSystemDecisionsInspection(workspace).run(softwareSystem);
        assertNull(violation);
    }

}
