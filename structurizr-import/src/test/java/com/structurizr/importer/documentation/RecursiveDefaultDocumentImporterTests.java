package com.structurizr.importer.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecursiveDefaultDocumentImporterTests {

    private Workspace workspace;
    private RecursiveDefaultDocumentationImporter documentationImporter;

    @BeforeEach
    public void setUp() {
        documentationImporter = new RecursiveDefaultDocumentationImporter();
        workspace = new Workspace("Name", "Description");
    }

    @Test
    public void test_importDocumentation_WithRecursiveSetToTrue() {
        File directory = new File("./src/test/resources/docs/docs");

        documentationImporter.importDocumentation(workspace, directory);
        Collection<Section> sections = workspace.getDocumentation().getSections();
        assertEquals(7, sections.size());

        assertSection(Format.Markdown, "## Section 1", 1, "01-section-1.md", sections.stream().filter(s -> s.getOrder() == 1).findFirst().get());
        assertSection(Format.Markdown, "## Section 2", 2, "02-section-2.markdown", sections.stream().filter(s -> s.getOrder() == 2).findFirst().get());
        assertSection(Format.Markdown, "## Section 3", 3, "03-section-3.text", sections.stream().filter(s -> s.getOrder() == 3).findFirst().get());
        assertSection(Format.AsciiDoc, "== Section 4", 4, "04-section-4.adoc", sections.stream().filter(s -> s.getOrder() == 4).findFirst().get());
        assertSection(Format.AsciiDoc, "== Section 5", 5, "05-section-5.asciidoc", sections.stream().filter(s -> s.getOrder() == 5).findFirst().get());
        assertSection(Format.AsciiDoc, "== Section 6", 6, "06-section-6.asc", sections.stream().filter(s -> s.getOrder() == 6).findFirst().get());
        assertSection(Format.Markdown, "## Section 7", 7, "07-subdirectory/01-section-1.md", sections.stream().filter(s -> s.getOrder() == 7).findFirst().get());
    }

    private void assertSection(Format format, String content, int order, String filename, Section section) {
        assertTrue(workspace.getDocumentation().getSections().contains(section));
        assertEquals(format, section.getFormat());
        assertEquals(content, section.getContent());
        assertEquals(order, section.getOrder());
        assertEquals(filename, section.getFilename());
    }

}