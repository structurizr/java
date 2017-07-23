package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ViewpointsAndPerspectivesDocumentationTemplateTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private ViewpointsAndPerspectivesDocumentationTemplate template;

    @Before
    public void setUp() {
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        template = new ViewpointsAndPerspectivesDocumentationTemplate(workspace);
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullWorkspaceIsSpecified() {
        try {
            new ViewpointsAndPerspectivesDocumentationTemplate(null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be specified.", e.getMessage());
        }
    }

    @Test
    public void test_addAllSectionsWithContentAsStrings() {
        Section section;

        section = template.addIntroductionSection(softwareSystem, Format.Markdown, "Section 1");
        assertSection(softwareSystem, "Introduction", Format.Markdown, "Section 1", 1, section);

        section = template.addGlossarySection(softwareSystem, Format.Markdown, "Section 2");
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 2", 2, section);

        section = template.addSystemStakeholdersAndRequirementsSection(softwareSystem, Format.Markdown, "Section 3");
        assertSection(softwareSystem, "System Stakeholders and Requirements", Format.Markdown, "Section 3", 3, section);

        section = template.addArchitecturalForcesSection(softwareSystem, Format.Markdown, "Section 4");
        assertSection(softwareSystem, "Architectural Forces", Format.Markdown, "Section 4", 4, section);

        section = template.addArchitecturalViewsSection(softwareSystem, Format.Markdown, "Section 5");
        assertSection(softwareSystem, "Architectural Views", Format.Markdown, "Section 5", 5, section);

        section = template.addSystemQualitiesSection(softwareSystem, Format.Markdown, "Section 6");
        assertSection(softwareSystem, "System Qualities", Format.Markdown, "Section 6", 6, section);

        section = template.addAppendicesSection(softwareSystem, Format.Markdown, "Section 7");
        assertSection(softwareSystem, "Appendices", Format.Markdown, "Section 7", 7, section);
    }

    @Test
    public void test_addAllSectionsWithContentFromFiles() throws IOException {
        Section section;
        File root = new File(".//test/unit/com/structurizr/documentation/viewpointsandperspectives");

        section = template.addIntroductionSection(softwareSystem, new File(root, "01-introduction.md"));
        assertSection(softwareSystem, "Introduction", Format.Markdown, "Section 1", 1, section);

        section = template.addGlossarySection(softwareSystem, new File(root, "02-glossary.md"));
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 2", 2, section);

        section = template.addSystemStakeholdersAndRequirementsSection(softwareSystem, new File(root, "03-system-stakeholders-and-requirements.md"));
        assertSection(softwareSystem, "System Stakeholders and Requirements", Format.Markdown, "Section 3", 3, section);

        section = template.addArchitecturalForcesSection(softwareSystem, new File(root, "04-architectural-forces.md"));
        assertSection(softwareSystem, "Architectural Forces", Format.Markdown, "Section 4", 4, section);

        section = template.addArchitecturalViewsSection(softwareSystem, new File(root, "05-architectural-views.md"));
        assertSection(softwareSystem, "Architectural Views", Format.Markdown, "Section 5", 5, section);

        section = template.addSystemQualitiesSection(softwareSystem, new File(root, "06-system-qualities.md"));
        assertSection(softwareSystem, "System Qualities", Format.Markdown, "Section 6", 6, section);

        section = template.addAppendicesSection(softwareSystem, new File(root, "07-appendices.adoc"));
        assertSection(softwareSystem, "Appendices", Format.AsciiDoc, "Section 7", 7, section);
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