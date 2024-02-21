package com.structurizr.inspection.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.inspection.model.SoftwareSystemDocumentationInspection;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmbeddedViewMissingInspectionTests {

    @Test
    public void run_WithMissingView() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

        Section section = new Section();
        section.setFormat(Format.Markdown);
        section.setContent("""
                ## Context
                
                ![](embed:SystemContext)
                """);
        softwareSystem.getDocumentation().addSection(section);

        Decision decision = new Decision("1");
        decision.setTitle("Decision 1");
        decision.setStatus("Accepted");
        decision.setFormat(Format.AsciiDoc);
        decision.setContent("""
                ## Containers
                
                image::embed:Containers[]
                """);
        softwareSystem.getDocumentation().addDecision(decision);

        Violation violation = new EmbeddedViewMissingInspection(new DefaultInspector(workspace)).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("documentation.embeddedview", violation.getType());
        assertEquals("The following views are embedded into documentation for the software system \"Software System\" but do not exist in the workspace: SystemContext, Containers", violation.getMessage());

        workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");

        violation = new EmbeddedViewMissingInspection(new DefaultInspector(workspace)).run(softwareSystem);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("documentation.embeddedview", violation.getType());
        assertEquals("The following views are embedded into documentation for the software system \"Software System\" but do not exist in the workspace: Containers", violation.getMessage());
    }

    @Test
    public void run_WithoutMissingView() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

        Section section = new Section();
        section.setFormat(Format.Markdown);
        section.setContent("""
                ## Context
                
                ![](embed:SystemContext)
                """);
        softwareSystem.getDocumentation().addSection(section);

        Decision decision = new Decision("1");
        decision.setTitle("Decision 1");
        decision.setStatus("Accepted");
        decision.setFormat(Format.AsciiDoc);
        decision.setContent("""
                ## Containers
                
                image::embed:Containers[]
                """);
        softwareSystem.getDocumentation().addDecision(decision);

        workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "Description");
        workspace.getViews().createContainerView(softwareSystem, "Containers", "Description");

        Violation violation = new EmbeddedViewMissingInspection(new DefaultInspector(workspace)).run(softwareSystem);
        assertNull(violation);
    }

}
