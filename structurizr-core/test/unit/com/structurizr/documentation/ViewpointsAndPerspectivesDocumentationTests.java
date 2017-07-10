package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewpointsAndPerspectivesDocumentationTests {

    private SoftwareSystem softwareSystem;
    private ViewpointsAndPerspectivesDocumentation documentation;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");

        documentation = new ViewpointsAndPerspectivesDocumentation(workspace);
    }

    @Test
    public void test_construction() {
        assertTrue(documentation.getSections().isEmpty());
        assertTrue(documentation.getImages().isEmpty());
    }

    @Test
    public void test_addAllSectionsWithContentAsStrings() {
        Section section;

        section = documentation.addIntroductionSection(softwareSystem, Format.Markdown, "Section 1");
        assertSection(softwareSystem, "Introduction", Format.Markdown, "Section 1", 1, section, documentation);

        section = documentation.addGlossarySection(softwareSystem, Format.Markdown, "Section 2");
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 2", 2, section, documentation);

        section = documentation.addSystemStakeholdersAndRequirementsSection(softwareSystem, Format.Markdown, "Section 3");
        assertSection(softwareSystem, "System Stakeholders and Requirements", Format.Markdown, "Section 3", 3, section, documentation);

        section = documentation.addArchitecturalForcesSection(softwareSystem, Format.Markdown, "Section 4");
        assertSection(softwareSystem, "Architectural Forces", Format.Markdown, "Section 4", 4, section, documentation);

        section = documentation.addArchitecturalViewsSection(softwareSystem, Format.Markdown, "Section 5");
        assertSection(softwareSystem, "Architectural Views", Format.Markdown, "Section 5", 5, section, documentation);

        section = documentation.addSystemQualitiesSection(softwareSystem, Format.Markdown, "Section 6");
        assertSection(softwareSystem, "System Qualities", Format.Markdown, "Section 6", 6, section, documentation);

        section = documentation.addAppendicesSection(softwareSystem, Format.Markdown, "Section 7");
        assertSection(softwareSystem, "Appendices", Format.Markdown, "Section 7", 7, section, documentation);
    }

    @Test
    public void test_addAllSectionsWithContentFromFiles() throws IOException {
        Section section;
        File root = new File(".//test/unit/com/structurizr/documentation/viewpointsandperspectives");

        section = documentation.addIntroductionSection(softwareSystem, Format.Markdown, new File(root, "01-introduction.md"));
        assertSection(softwareSystem, "Introduction", Format.Markdown, "Section 1", 1, section, documentation);

        section = documentation.addGlossarySection(softwareSystem, Format.Markdown, new File(root, "02-glossary.md"));
        assertSection(softwareSystem, "Glossary", Format.Markdown, "Section 2", 2, section, documentation);

        section = documentation.addSystemStakeholdersAndRequirementsSection(softwareSystem, Format.Markdown, new File(root, "03-system-stakeholders-and-requirements.md"));
        assertSection(softwareSystem, "System Stakeholders and Requirements", Format.Markdown, "Section 3", 3, section, documentation);

        section = documentation.addArchitecturalForcesSection(softwareSystem, Format.Markdown, new File(root, "04-architectural-forces.md"));
        assertSection(softwareSystem, "Architectural Forces", Format.Markdown, "Section 4", 4, section, documentation);

        section = documentation.addArchitecturalViewsSection(softwareSystem, Format.Markdown, new File(root, "05-architectural-views.md"));
        assertSection(softwareSystem, "Architectural Views", Format.Markdown, "Section 5", 5, section, documentation);

        section = documentation.addSystemQualitiesSection(softwareSystem, Format.Markdown, new File(root, "06-system-qualities.md"));
        assertSection(softwareSystem, "System Qualities", Format.Markdown, "Section 6", 6, section, documentation);

        section = documentation.addAppendicesSection(softwareSystem, Format.Markdown, new File(root, "07-appendices.md"));
        assertSection(softwareSystem, "Appendices", Format.Markdown, "Section 7", 7, section, documentation);
    }

    private void assertSection(Element element, String type, Format format, String content, int order, Section section, Documentation documentation) {
        assertTrue(documentation.getSections().contains(section));
        assertEquals(element, section.getElement());
        assertEquals(element.getId(), section.getElementId());
        assertEquals(type, section.getType());
        assertEquals(format, section.getFormat());
        assertEquals(content, section.getContent());
        assertEquals(order, section.getOrder());
    }

}