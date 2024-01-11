package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MultipleSoftwareSystemsDetailedInspectionTests {

    @Test
    public void run_MultipleSoftwareSystemsWithContainers() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").addContainer("Container");
        workspace.getModel().addSoftwareSystem("B").addContainer("Container");

        Recommendation recommendation = new MultipleSoftwareSystemsDetailedInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.scope", recommendation.getType());
        assertEquals("This workspace describes the internal details of 2 software systems. It is recommended that a workspace contains the model, views, and documentation for a single software system only.", recommendation.getDescription());
    }

    @Test
    public void run_MultipleSoftwareSystemsWithDocumentation() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("A").getDocumentation().addSection(new Section(Format.Markdown, "# Section 1"));
        workspace.getModel().addSoftwareSystem("B").getDocumentation().addSection(new Section(Format.Markdown, "# Section 1"));

        Recommendation recommendation = new MultipleSoftwareSystemsDetailedInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.scope", recommendation.getType());
        assertEquals("This workspace describes the internal details of 2 software systems. It is recommended that a workspace contains the model, views, and documentation for a single software system only.", recommendation.getDescription());
    }

    @Test
    public void run_MultipleSoftwareSystemsWithDecisions() {
        Workspace workspace = new Workspace("Name", "Description");
        Decision decision = new Decision("1");
        decision.setTitle("Decision 1");
        decision.setFormat(Format.Markdown);
        decision.setContent("Content");
        decision.setStatus("Accepted");
        workspace.getModel().addSoftwareSystem("A").getDocumentation().addDecision(decision);
        workspace.getModel().addSoftwareSystem("B").getDocumentation().addDecision(decision);

        Recommendation recommendation = new MultipleSoftwareSystemsDetailedInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.scope", recommendation.getType());
        assertEquals("This workspace describes the internal details of 2 software systems. It is recommended that a workspace contains the model, views, and documentation for a single software system only.", recommendation.getDescription());
    }

    @Test
    public void run_SingleSoftwareSystem() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        a.addContainer("Container");

        a.getDocumentation().addSection(new Section(Format.Markdown, "# Section 1"));

        Decision decision = new Decision("1");
        decision.setTitle("Decision 1");
        decision.setFormat(Format.Markdown);
        decision.setContent("Content");
        decision.setStatus("Accepted");

        a.getDocumentation().addDecision(decision);

        Recommendation recommendation = new MultipleSoftwareSystemsDetailedInspection(workspace).run();
        assertNull(recommendation);
    }

}
