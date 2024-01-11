package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
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

        Recommendation recommendation = new SoftwareSystemDecisionsInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.softwaresystem.decisions", recommendation.getType());
        assertEquals("The software system named \"Software System\" has containers, but is missing decisions.", recommendation.getDescription());
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

        Recommendation recommendation = new SoftwareSystemDecisionsInspection(workspace).run(softwareSystem);
        assertNull(recommendation);
    }

}
