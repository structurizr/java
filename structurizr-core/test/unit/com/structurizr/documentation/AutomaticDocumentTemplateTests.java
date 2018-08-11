package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AutomaticDocumentTemplateTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private AutomaticDocumentationTemplate template;

    @Before
    public void setUp() {
        softwareSystem = workspace.getModel().addSoftwareSystem("Name", "Description");
        template = new AutomaticDocumentationTemplate(workspace);
    }

    @Test
    public void test_construction_ThrowsAnException_WhenANullWorkspaceIsSpecified() {
        try {
            new AutomaticDocumentationTemplate(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A workspace must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addSections_ThrowsAnException_WhenTheDirectoryDoesNotExist() throws Exception {
        try {
            new AutomaticDocumentationTemplate(workspace).addSections(new File(".//test/unit/com/structurizr/documentation/foo"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo does not exist."));
        }
    }

    @Test
    public void test_addSections_ThrowsAnException_WhenTheDirectoryIsNotADirectory() throws Exception {
        try {
            new AutomaticDocumentationTemplate(workspace).addSections(new File(".//test/unit/com/structurizr/documentation/automatic/01-section-1.md"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("01-section-1.md is not a directory."));
        }
    }

    @Test
    public void test_addSections() throws IOException {
        Section section;
        File root = new File(".//test/unit/com/structurizr/documentation/automatic");

        List<Section> sections = template.addSections(softwareSystem, root);
        assertEquals(6, sections.size());

        assertSection(softwareSystem, "Section 1", Format.Markdown, "## Section 1", 1, sections.get(0));
        assertSection(softwareSystem, "Section 2", Format.Markdown, "## Section 2", 2, sections.get(1));
        assertSection(softwareSystem, "Section 3", Format.Markdown, "## Section 3", 3, sections.get(2));
        assertSection(softwareSystem, "Section 4", Format.AsciiDoc, "== Section 4", 4, sections.get(3));
        assertSection(softwareSystem, "Section 5", Format.AsciiDoc, "== Section 5", 5, sections.get(4));
        assertSection(softwareSystem, "Section 6", Format.AsciiDoc, "== Section 6", 6, sections.get(5));
    }

    private void assertSection(Element element, String title, Format format, String content, int order, Section section) {
        assertTrue(workspace.getDocumentation().getSections().contains(section));
        assertEquals(element, section.getElement());
        assertEquals(element.getId(), section.getElementId());
        assertEquals(title, section.getTitle());
        assertEquals(format, section.getFormat());
        assertEquals(content, section.getContent());
        assertEquals(order, section.getOrder());
    }

}
