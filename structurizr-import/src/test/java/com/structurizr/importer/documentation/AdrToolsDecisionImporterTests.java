package com.structurizr.importer.documentation;

import com.structurizr.Workspace;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Documentation;
import com.structurizr.documentation.Format;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;


public class AdrToolsDecisionImporterTests {

    private static final File DECISIONS_FOLDER = new File("./src/test/resources/decisions/adrtools");

    private AdrToolsDecisionImporter decisionImporter;
    private Workspace workspace;
    private Documentation documentation;

    @BeforeEach
    public void setUp() {
        decisionImporter = new AdrToolsDecisionImporter();
        workspace = new Workspace("Name", "Description");
        documentation = workspace.getDocumentation();
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenANullDocumentableIsSpecified() {
        try {
            decisionImporter.importDocumentation(null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A workspace, software system, container, or component must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenADirectoryIsNotSpecified()  {
        try {
            decisionImporter.importDocumentation(workspace, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A path must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenADirectoryIsSpecifiedButItDoesNotExist() {
        try {
            File directory = new File("foo");
            assertFalse(directory.exists());
            decisionImporter.importDocumentation(workspace, directory);
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("foo does not exist."));
        }
    }

    @Test
    public void test_importDocumentation_ThrowsAnException_WhenAPathIsSpecifiedButItIsNotADirectory() {
        try {
            decisionImporter.importDocumentation(workspace, new File("build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("/build.gradle is not a directory."));
        }
    }

    @Test
    public void test_importDecisions() {
        decisionImporter.importDocumentation(workspace, DECISIONS_FOLDER);

        assertEquals(9, documentation.getDecisions().size());

        Decision decision1 = documentation.getDecisions().stream().filter(d -> d.getId().equals("1")).findFirst().get();
        assertEquals("1", decision1.getId());
        assertEquals("Record architecture decisions", decision1.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss ZZZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("12-Feb-2016 00:00:00 +0000", sdf.format(decision1.getDate()));
        assertEquals("Accepted", decision1.getStatus());
        Assertions.assertEquals(Format.Markdown, decision1.getFormat());
        assertEquals("# 1. Record architecture decisions\n" +
                        "\n" +
                        "Date: 2016-02-12\n" +
                        "\n" +
                        "## Status\n" +
                        "\n" +
                        "Accepted\n" +
                        "\n" +
                        "## Context\n" +
                        "\n" +
                        "We need to record the architectural decisions made on this project.\n" +
                        "\n" +
                        "## Decision\n" +
                        "\n" +
                        "We will use Architecture Decision Records, as described by Michael Nygard in this article: http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions\n" +
                        "\n" +
                        "## Consequences\n" +
                        "\n" +
                        "See Michael Nygard's article, linked above.\n",
                decision1.getContent());
    }

    @Test
    public void test_importDocumentation_CapturesLinksBetweenDecisions() {
        decisionImporter.importDocumentation(workspace, DECISIONS_FOLDER);

        Decision decision5 = documentation.getDecisions().stream().filter(d -> d.getId().equals("5")).findFirst().get();
        assertEquals(1, decision5.getLinks().size());
        Decision.Link link = decision5.getLinks().iterator().next();
        assertEquals("9", link.getId());
        assertEquals("Amended by", link.getDescription());
    }

    @Test
    public void test_importDocumentation_RewritesLinksBetweenDecisions() {
        decisionImporter.importDocumentation(workspace, DECISIONS_FOLDER);

        Decision decision5 = documentation.getDecisions().stream().filter(d -> d.getId().equals("5")).findFirst().get();
        assertTrue(decision5.getContent().contains("Amended by [9. Help scripts](#9)"));
    }

}