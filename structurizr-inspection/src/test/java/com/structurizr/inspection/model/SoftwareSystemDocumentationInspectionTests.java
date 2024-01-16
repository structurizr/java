package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
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

        Violation violation = new SoftwareSystemDocumentationInspection(workspace).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.softwaresystem.documentation", violation.getType());
        assertEquals("The software system named \"Software System\" has containers, but is missing documentation.", violation.getMessage());
    }

    @Test
    public void run_WithDocumentation() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Name", "Description");
        softwareSystem.getDocumentation().addSection(new Section(Format.Markdown, "# Section 1"));

        Violation violation = new SoftwareSystemDocumentationInspection(workspace).run(softwareSystem);
        assertNull(violation);
    }

}
