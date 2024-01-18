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


public class MadrDecisionImporterTests {

    private static final File DECISIONS_FOLDER = new File("./src/test/resources/decisions/madr");

    private MadrDecisionImporter decisionImporter;
    private Workspace workspace;
    private Documentation documentation;

    @BeforeEach
    public void setUp() {
        decisionImporter = new MadrDecisionImporter();
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

        assertEquals(19, documentation.getDecisions().size());

        Decision decision0 = documentation.getDecisions().stream().filter(d -> d.getId().equals("0")).findFirst().get();
        assertEquals("0", decision0.getId());
        assertEquals("Use Markdown Any Decision Records", decision0.getTitle());
        assertEquals("11-Jan-2024", sdf.format(decision0.getDate()));
        assertEquals("accepted", decision0.getStatus());
        Assertions.assertEquals(Format.Markdown, decision0.getFormat());
        assertEquals("""
# Use Markdown Any Decision Records

## Context and Problem Statement

We want to record any decisions made in this project independent whether decisions concern the architecture ("architectural decision record"), the code, or other fields.
Which format and structure should these records follow?

## Considered Options

* [MADR](https://adr.github.io/madr/) 3.0.0 – The Markdown Any Decision Records
* [Michael Nygard's template](http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions) – The first incarnation of the term "ADR"
* [Sustainable Architectural Decisions](https://www.infoq.com/articles/sustainable-architectural-design-decisions) – The Y-Statements
* Other templates listed at <https://github.com/joelparkerhenderson/architecture_decision_record>
* Formless – No conventions for file format and structure

## Decision Outcome

Chosen option: "MADR 3.0.0", because

* Implicit assumptions should be made explicit.
  Design documentation is important to enable people understanding the decisions later on.
  See also [A rational design process: How and why to fake it](https://doi.org/10.1109/TSE.1986.6312940).
* MADR allows for structured capturing of any decision.
* The MADR format is lean and fits our development style.
* The MADR structure is comprehensible and facilitates usage & maintenance.
* The MADR project is vivid.
""", decision0.getContent());

        Decision decision3 = documentation.getDecisions().stream().filter(d -> d.getId().equals("3")).findFirst().get();
        assertEquals("on hold", decision3.getStatus());

        Decision decision8 = documentation.getDecisions().stream().filter(d -> d.getId().equals("8")).findFirst().get();
        assertEquals("""
# Add Status Field

## Context and Problem Statement

Technical Story: <https://github.com/adr/madr/issues/2>

ADRs have a status. Should this be tracked? And if it should, how should we track it?

## Considered Options

* Use YAML front matter
* Use badge
* Use text line
* Use separate heading
* Use table
* Do not add status

## Decision Outcome

Chosen option: "Use YAML front matter", because comes out best (see below).

## Pros and Cons of the Options

### Use YAML front matter

Example:

```markdown
---
parent: Decisions
nav_order: 3
status: on hold
---
# Write own MADR tooling
```

* Good, because YAML front matter is supported by most Markdown parsers

### Use badge

#### Examples

* ![Example "Use Angular" with "status: accepted"](0008-example-badge.png)
* [![Example "status: superseded"](https://img.shields.io/badge/status-superseeded_by_ADR_0001-orange.svg?style=flat-square)](https://github.com/adr/madr/blob/main/docs/decisions/0001-use-CC0-as-license.md)

---

* Good, because plain markdown
* Good, because looks good
* Bad, because hard to read in markdown source
* Bad, because relies on the online service <https://shields.io> or [local badges have to be generated](https://github.com/badges/shields#using-the-badge-library)
* Bad, because at local usages, many badges have to be generated (superseeded-by-ADR-0006, for each ADR number)
* Bad, because not easy to write

### Use text line

Example: `Status: Accepted`

* Good, because plain markdown
* Good, because easy to read
* Good, because easy to write
* Good, because looks OK in both markdown-source (MD) and in rendered versions (HTML, PDF)
* Good, because no dependencies on external tools
* Good, because single line indicates the current state
* Bad, because "Status" line needs to be maintained
* Bad, because uses space at the beginning. When users read MADR, they should directly dive into the context and problem and not into the status

### Use separate heading

Example: ![example for separate heading](0008-example-separate-heading.png)

* Good, because plain markdown
* Good, because easy to write
* Bad, because it uses much space: At least three lines: heading, status, separating empty line

### Use table

Example: ![example for table](0008-example-table.png)

* Good, because history can be included
* Good, because multiple entries can be made
* Good, because already implemented in `adr-tools` fork
* Bad, because not covered by the [CommonMark specification 0.28 (2017-08-01)](http://spec.commonmark.org/0.28/)
* Bad, because hard to read
* Bad, because outdated entries cannot be easily identified
* Bad, because needs more markdown training

### Do not add status

* Good, because MADR is kept lean
* Bad, because users demand state field
* Bad, because not in line with other ADR templates

## More Information

See [ADR-0013](#13) for more reasoning on using YAML front matter.
                """, decision8.getContent());

        Decision decision18 = documentation.getDecisions().stream().filter(d -> d.getId().equals("18")).findFirst().get();
        assertEquals("Use \"Confirmation\" as Heading", decision18.getTitle());
    }

    @Test
    public void test_importDocumentation_CapturesLinksBetweenDecisions() {
        decisionImporter.importDocumentation(workspace, DECISIONS_FOLDER);

        Decision decision8 = documentation.getDecisions().stream().filter(d -> d.getId().equals("8")).findFirst().get();
        assertEquals(1, decision8.getLinks().size());
        Decision.Link link = decision8.getLinks().iterator().next();
        assertEquals("13", link.getId());
        assertEquals("Links to", link.getDescription());
    }

}