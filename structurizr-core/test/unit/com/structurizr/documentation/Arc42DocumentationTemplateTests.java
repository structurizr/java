package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class Arc42DocumentationTemplateTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private Arc42DocumentationTemplate template;

    @Before
    public void setUp() {
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        template = new Arc42DocumentationTemplate(workspace);
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullWorkspaceIsSpecified() {
        try {
            new Arc42DocumentationTemplate(null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_construction() {
        assertTrue(workspace.getDocumentation().getSections().isEmpty());
        assertTrue(workspace.getDocumentation().getImages().isEmpty());
    }

    @Test
    public void test_addAllSectionsWithContentAsStrings() {
        Section section;

        section = template.addIntroductionAndGoalsSection(softwareSystem, Format.Markdown, "Section 1");
        assertSection(softwareSystem, "Introduction and Goals", Format.Markdown, "Section 1", 1, section);

        section = template.addConstraintsSection(softwareSystem, Format.Markdown, "Section 2");
        assertSection(softwareSystem, "Constraints", Format.Markdown, "Section 2", 2, section);

        section = template.addContextAndScopeSection(softwareSystem, Format.Markdown, "Section 3");
        assertSection(softwareSystem, "Context and Scope", Format.Markdown, "Section 3", 3, section);

        section = template.addSolutionStrategySection(softwareSystem, Format.Markdown, "Section 4");
        assertSection(softwareSystem, "Solution Strategy", Format.Markdown, "Section 4", 4, section);

        section = template.addBuildingBlockViewSection(softwareSystem, Format.Markdown, "Section 5");
        assertSection(softwareSystem, "Building Block View", Format.Markdown, "Section 5", 5, section);

        section = template.addRuntimeViewSection(softwareSystem, Format.Markdown, "Section 6");
        assertSection(softwareSystem, "Runtime View", Format.Markdown, "Section 6", 6, section);

        section = template.addDeploymentViewSection(softwareSystem, Format.Markdown, "Section 7");
        assertSection(softwareSystem, "Deployment View", Format.Markdown, "Section 7", 7, section);

        section = template.addCrosscuttingConceptsSection(softwareSystem, Format.Markdown, "Section 8");
        assertSection(softwareSystem, "Crosscutting Concepts", Format.Markdown, "Section 8", 8, section);

        section = template.addArchitecturalDecisionsSection(softwareSystem, Format.Markdown, "Section 9");
        assertSection(softwareSystem, "Architectural Decisions", Format.Markdown, "Section 9", 9, section);

        section = template.addQualityRequirementsSection(softwareSystem, Format.Markdown, "Section 10");
        assertSection(softwareSystem, "Quality Requirements", Format.Markdown, "Section 10", 10, section);

        section = template.addRisksAndTechnicalDebtSection(softwareSystem, Format.Markdown, "Section 11");
        assertSection(softwareSystem, "Risks and Technical Debt", Format.Markdown, "Section 11", 11, section);

        section = template.addGlossarySection(softwareSystem, Format.Markdown, "Section 12");
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 12", 12, section);
    }

    @Test
    public void test_addAllSectionsWithContentFromFiles() throws IOException {
        Section section;
        File root = new File(".//test/unit/com/structurizr/documentation/arc42");

        section = template.addIntroductionAndGoalsSection(softwareSystem, Format.Markdown, new File(root, "introduction-and-goals.md"));
        assertSection(softwareSystem, "Introduction and Goals", Format.Markdown, "Section 1", 1, section);

        section = template.addConstraintsSection(softwareSystem, Format.Markdown, new File(root, "constraints.md"));
        assertSection(softwareSystem, "Constraints", Format.Markdown, "Section 2", 2, section);

        section = template.addContextAndScopeSection(softwareSystem, Format.Markdown, new File(root, "context-and-scope.md"));
        assertSection(softwareSystem, "Context and Scope", Format.Markdown, "Section 3", 3, section);

        section = template.addSolutionStrategySection(softwareSystem, Format.Markdown, new File(root, "solution-strategy.md"));
        assertSection(softwareSystem, "Solution Strategy", Format.Markdown, "Section 4", 4, section);

        section = template.addBuildingBlockViewSection(softwareSystem, Format.Markdown, new File(root, "building-block-view.md"));
        assertSection(softwareSystem, "Building Block View", Format.Markdown, "Section 5", 5, section);

        section = template.addRuntimeViewSection(softwareSystem, Format.Markdown, new File(root, "runtime-view.md"));
        assertSection(softwareSystem, "Runtime View", Format.Markdown, "Section 6", 6, section);

        section = template.addDeploymentViewSection(softwareSystem, Format.Markdown, new File(root, "deployment-view.md"));
        assertSection(softwareSystem, "Deployment View", Format.Markdown, "Section 7", 7, section);

        section = template.addCrosscuttingConceptsSection(softwareSystem, Format.Markdown, new File(root, "crosscutting-concepts.md"));
        assertSection(softwareSystem, "Crosscutting Concepts", Format.Markdown, "Section 8", 8, section);

        section = template.addArchitecturalDecisionsSection(softwareSystem, Format.Markdown, new File(root, "architectural-decisions.md"));
        assertSection(softwareSystem, "Architectural Decisions", Format.Markdown, "Section 9", 9, section);

        section = template.addQualityRequirementsSection(softwareSystem, Format.Markdown, new File(root, "quality-requirements.md"));
        assertSection(softwareSystem, "Quality Requirements", Format.Markdown, "Section 10", 10, section);

        section = template.addRisksAndTechnicalDebtSection(softwareSystem, Format.Markdown, new File(root, "risks-and-technical-debt.md"));
        assertSection(softwareSystem, "Risks and Technical Debt", Format.Markdown, "Section 11", 11, section);

        section = template.addGlossarySection(softwareSystem, Format.Markdown, new File(root, "glossary.md"));
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 12", 12, section);
    }

    private void assertSection(Element element, String type, Format format, String content, int order, Section section) {
        assertTrue(workspace.getDocumentation().getSections().contains(section));
        assertEquals(element, section.getElement());
        assertEquals(element.getId(), section.getElementId());
        assertEquals(type, section.getType());
        assertEquals(format, section.getFormat());
        assertEquals(content, section.getContent());
        assertEquals(order, section.getOrder());
    }

}