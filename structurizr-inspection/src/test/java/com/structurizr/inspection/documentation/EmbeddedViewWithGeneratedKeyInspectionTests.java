package com.structurizr.inspection.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmbeddedViewWithGeneratedKeyInspectionTests {

    @Test
    public void run_WithGeneratedKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

        Section section = new Section();
        section.setFormat(Format.Markdown);
        section.setContent("""
                ## Context
                
                ![](embed:SystemContext-001)
                """);
        softwareSystem.getDocumentation().addSection(section);

        section = new Section();
        section.setFormat(Format.AsciiDoc);
        section.setContent("""
                ## Containers
                
                image::embed:Container-001[]
                """);
        softwareSystem.getDocumentation().addSection(section);

        workspace.getViews().createSystemContextView(softwareSystem, "", "Description");
        workspace.getViews().createContainerView(softwareSystem, "", "Description");

        Violation violation = new EmbeddedViewWithGeneratedKeyInspection(new DefaultInspector(workspace)).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("documentation.embeddedview", violation.getType());
        assertEquals("The following views are embedded into documentation for the software system \"Software System\" via an automatically generated view key: SystemContext-001, Container-001", violation.getMessage());
    }

    @Test
    public void run_WithoutGeneratedKey() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        
        Section section = new Section();
        section.setFormat(Format.Markdown);
        section.setContent("""
                ## Context
                
                ![](embed:SystemContext)
                """);
        softwareSystem.getDocumentation().addSection(section);

        section = new Section();
        section.setFormat(Format.AsciiDoc);
        section.setContent("""
                ## Containers
                
                image::embed:Containers[]
                """);
        softwareSystem.getDocumentation().addSection(section);

        workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        workspace.getViews().createContainerView(softwareSystem, "Containers", "Description");

        Violation violation = new EmbeddedViewWithGeneratedKeyInspection(new DefaultInspector(workspace)).run(softwareSystem);
        assertNull(violation);
    }

}
