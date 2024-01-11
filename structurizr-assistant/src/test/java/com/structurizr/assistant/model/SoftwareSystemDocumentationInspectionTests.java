package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SoftwareSystemDocumentationInspectionTests {

    @Test
    public void run_WithoutDocumentation() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name");

        Recommendation recommendation = new SoftwareSystemDocumentationInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.softwaresystem.documentation", recommendation.getType());
        assertEquals("The software system named \"Software System\" has containers, but is missing documentation.", recommendation.getDescription());
    }

    @Test
    public void run_WithDocumentation() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name", "Description");
        softwareSystem.getDocumentation().addSection(new Section(Format.Markdown, "# Section 1"));

        Recommendation recommendation = new SoftwareSystemDocumentationInspection(workspace).run(softwareSystem);
        assertNull(recommendation);
    }

}
