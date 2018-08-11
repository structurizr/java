package com.structurizr.documentation;

import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class AdrToolsImporterTests {

    private Workspace workspace;
    private SoftwareSystem softwareSystem;
    private Documentation documentation;

    @Before
    public void setUp() {
        workspace = new Workspace("Name", "Description");
        softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "Description");
        documentation = workspace.getDocumentation();
    }

    @Test
    public void test_construction_ThrowsAnException_WhenAWorkspaceIsNotSpecified() {
        try {
            new AdrToolsImporter(null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A workspace must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenADirectoryIsNotSpecified() {
        try {
            new AdrToolsImporter(workspace, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The path to the architecture decision records must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenADirectoryIsSpecifiedButItDoesNotExist() {
        try {
            new AdrToolsImporter(workspace, new File("some-random-path"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("structurizr-adr-tools/some-random-path does not exist."));
        }
    }

    @Test
    public void test_construction_ThrowsAnException_WhenAPathIsSpecifiedButItIsNotADirectory() {
        try {
            new AdrToolsImporter(workspace, new File("build.gradle"));
            fail();
        } catch (IllegalArgumentException iae) {
            assertTrue(iae.getMessage().endsWith("structurizr-adr-tools/build.gradle is not a directory."));
        }
    }

    @Test
    public void test_importArchitectureDecisionRecords() throws Exception {
        AdrToolsImporter importer = new AdrToolsImporter(workspace, new File("test/unit/adrs"));
        importer.importArchitectureDecisionRecords();

        assertEquals(10, documentation.getDecisions().size());

        Decision decision1 = documentation.getDecisions().stream().filter(d -> d.getId().equals("1")).findFirst().get();
        assertEquals("1", decision1.getId());
        assertEquals("Record architecture decisions", decision1.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss ZZZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertEquals("12-Feb-2016 00:00:00 +0000", sdf.format(decision1.getDate()));
        assertEquals(DecisionStatus.Accepted, decision1.getStatus());
        assertEquals(Format.Markdown, decision1.getFormat());
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
    public void test_importArchitectureDecisionRecords_RewritesLinksBetweenADRsWhenTheyAreNotAssociatedWithAnElement() throws Exception {
        AdrToolsImporter importer = new AdrToolsImporter(workspace, new File("test/unit/adrs"));
        importer.importArchitectureDecisionRecords();

        Decision decision5 = documentation.getDecisions().stream().filter(d -> d.getId().equals("5")).findFirst().get();
        assertTrue(decision5.getContent().contains("Amended by [9. Help scripts](#/:9)"));
    }

    @Test
    public void test_importArchitectureDecisionRecords_RewritesLinksBetweenADRsWhenTheyAreAssociatedWithAnElement() throws Exception {
        AdrToolsImporter importer = new AdrToolsImporter(workspace, new File("test/unit/adrs"));
        importer.importArchitectureDecisionRecords(softwareSystem);

        Decision decision5 = documentation.getDecisions().stream().filter(d -> d.getId().equals("5")).findFirst().get();
        assertTrue(decision5.getContent().contains("Amended by [9. Help scripts](#%2FSoftware%20System:9)"));
    }

    @Test
    public void test_importArchitectureDecisionRecords_SupportsTheIncorrectSpellingOfSuperseded() throws Exception {
        AdrToolsImporter importer = new AdrToolsImporter(workspace, new File("test/unit/adrs"));
        importer.importArchitectureDecisionRecords();

        Decision decision4 = documentation.getDecisions().stream().filter(d -> d.getId().equals("4")).findFirst().get();
        assertEquals(DecisionStatus.Superseded, decision4.getStatus());
        System.out.println(decision4.getContent());
        assertTrue(decision4.getContent().contains("Superceded by [10. AsciiDoc format](#/:10)"));
    }

}