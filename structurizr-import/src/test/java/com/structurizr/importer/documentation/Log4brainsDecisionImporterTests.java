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


public class Log4brainsDecisionImporterTests {

    private static final File DECISIONS_FOLDER = new File("./src/test/resources/decisions/log4brains");

    private Log4brainsDecisionImporter decisionImporter;
    private Workspace workspace;
    private Documentation documentation;

    @BeforeEach
    public void setUp() {
        decisionImporter = new Log4brainsDecisionImporter();
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        assertEquals(4, documentation.getDecisions().size());

        // I think these first two decisions are found in the wrong order, which is a consequence of Log4brains not having decision IDs

        Decision decision1 = documentation.getDecisions().stream().filter(d -> d.getId().equals("1")).findFirst().get();
        assertEquals("1", decision1.getId());
        assertEquals("Use Log4brains to manage the ADRs", decision1.getTitle());
        assertEquals("10-Jan-2024", sdf.format(decision1.getDate()));
        assertEquals("accepted", decision1.getStatus());
        Assertions.assertEquals(Format.Markdown, decision1.getFormat());
        assertEquals("""
# Use Log4brains to manage the ADRs

- Status: accepted
- Date: 2024-01-10
- Tags: dev-tools, doc

## Context and Problem Statement

We want to record architectural decisions made in this project.
Which tool(s) should we use to manage these records?

## Considered Options

- [Log4brains](https://github.com/thomvaill/log4brains): architecture knowledge base (command-line + static site generator)
- [ADR Tools](https://github.com/npryce/adr-tools): command-line to create ADRs
- [ADR Tools Python](https://bitbucket.org/tinkerer_/adr-tools-python/src/master/): command-line to create ADRs
- [adr-viewer](https://github.com/mrwilson/adr-viewer): static site generator
- [adr-log](https://adr.github.io/adr-log/): command-line to create a TOC of ADRs

## Decision Outcome

Chosen option: "Log4brains", because it includes the features of all the other tools, and even more.
""",
                decision1.getContent());

        Decision decision2 = documentation.getDecisions().stream().filter(d -> d.getId().equals("2")).findFirst().get();
        assertEquals("2", decision2.getId());
        assertEquals("Use Markdown Architectural Decision Records", decision2.getTitle());
        assertEquals("10-Jan-2024", sdf.format(decision2.getDate()));
        assertEquals("accepted", decision2.getStatus());
        Assertions.assertEquals(Format.Markdown, decision2.getFormat());
        assertEquals("""
# Use Markdown Architectural Decision Records

- Status: accepted
- Date: 2024-01-10
- Tags: doc

## Context and Problem Statement

We want to record architectural decisions made in this project.
Which format and structure should these records follow?

## Considered Options

- [MADR](https://adr.github.io/madr/) 2.1.2 with Log4brains patch
- [MADR](https://adr.github.io/madr/) 2.1.2 – The original Markdown Architectural Decision Records
- [Michael Nygard's template](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions) – The first incarnation of the term "ADR"
- [Sustainable Architectural Decisions](https://www.infoq.com/articles/sustainable-architectural-design-decisions) – The Y-Statements
- Other templates listed at <https://github.com/joelparkerhenderson/architecture_decision_record>
- Formless – No conventions for file format and structure

## Decision Outcome

Chosen option: "MADR 2.1.2 with Log4brains patch", because

- Implicit assumptions should be made explicit.
  Design documentation is important to enable people understanding the decisions later on.
  See also [A rational design process: How and why to fake it](https://doi.org/10.1109/TSE.1986.6312940).
- The MADR format is lean and fits our development style.
- The MADR structure is comprehensible and facilitates usage & maintenance.
- The MADR project is vivid.
- Version 2.1.2 is the latest one available when starting to document ADRs.
- The Log4brains patch adds more features, like tags.

The "Log4brains patch" performs the following modifications to the original template:

- Change the ADR filenames format (`NNN-adr-name` becomes `YYYYMMDD-adr-name`), to avoid conflicts during Git merges.
- Add a `draft` status, to enable collaborative writing.
- Add a `Tags` field.

## Links

- Relates to [Use Log4brains to manage the ADRs](#1)
""",
                decision2.getContent());

        Decision decision3 = documentation.getDecisions().stream().filter(d -> d.getId().equals("3")).findFirst().get();
        assertEquals("3", decision3.getId());
        assertEquals("Decision 3", decision3.getTitle());
        assertEquals("13-Jan-2024", sdf.format(decision3.getDate()));
        assertEquals("superseded", decision3.getStatus());
        Assertions.assertEquals(Format.Markdown, decision3.getFormat());
        assertEquals("""
# Decision 3

- Status: superseded by [20240111-decision-4](#4)

## Context and Problem Statement

Describe the context and problem statement, e.g., in free form using two to three sentences. You may want to articulate the problem in form of a question.
""",
                decision3.getContent());

        Decision decision4 = documentation.getDecisions().stream().filter(d -> d.getId().equals("4")).findFirst().get();
        assertEquals("4", decision4.getId());
        assertEquals("Decision 4", decision4.getTitle());
        assertEquals("14-Jan-2024", sdf.format(decision4.getDate()));
        assertEquals("accepted", decision4.getStatus());
        Assertions.assertEquals(Format.Markdown, decision4.getFormat());
        assertEquals("""
# Decision 4

- Status: accepted

## Context and Problem Statement

Describe the context and problem statement, e.g., in free form using two to three sentences. You may want to articulate the problem in form of a question.

## Links

- Supersedes [20240111-decision-3](#3)
""",
                decision4.getContent());
    }

    @Test
    public void test_importDocumentation_CapturesLinksBetweenDecisions() {
        decisionImporter.importDocumentation(workspace, DECISIONS_FOLDER);

        Decision decision2 = documentation.getDecisions().stream().filter(d -> d.getId().equals("2")).findFirst().get();
        assertEquals(1, decision2.getLinks().size());
        Decision.Link link = decision2.getLinks().iterator().next();
        assertEquals("1", link.getId());
        assertEquals("Relates to", link.getDescription());

        Decision decision3 = documentation.getDecisions().stream().filter(d -> d.getId().equals("3")).findFirst().get();
        assertEquals(1, decision3.getLinks().size());
        link = decision3.getLinks().iterator().next();
        assertEquals("4", link.getId());
        assertEquals("superseded by", link.getDescription());

        Decision decision4 = documentation.getDecisions().stream().filter(d -> d.getId().equals("4")).findFirst().get();
        assertEquals(1, decision4.getLinks().size());
        link = decision4.getLinks().iterator().next();
        assertEquals("3", link.getId());
        assertEquals("Supersedes", link.getDescription());
    }

}