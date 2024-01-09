package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentationTests extends AbstractWorkspaceTestBase {

    private Documentation documentation;

    @BeforeEach
    public void setUp() {
        documentation = workspace.getDocumentation();
    }

    @Test
    void addSection_ThrowsAnException_WhenTheFormatIsNotSpecified() {
        try {
            Section section = new Section();
            section.setContent("Content");

            documentation.addSection(section);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A format must be specified.", iae.getMessage());
        }
    }

    @Test
    void addSection() {
        Section section = new Section();
        section.setContent("Content");
        section.setFormat(Format.Markdown);

        documentation.addSection(section);

        assertEquals(1, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Content", section.getContent());
        assertEquals(1, section.getOrder());
    }

    @Test
    void addSection_IncrementsTheSectionOrderNumber() {
        Section section1 = new Section(Format.Markdown, "Content 1");
        Section section2 = new Section(Format.Markdown, "Content 2");
        Section section3 = new Section(Format.Markdown, "Content 3");

        documentation.addSection(section1);
        documentation.addSection(section2);
        documentation.addSection(section3);

        assertEquals(1, section1.getOrder());
        assertEquals(2, section2.getOrder());
        assertEquals(3, section3.getOrder());
    }

    @Test
    void addDecision_ThrowsAnException_WhenTheTitleIsNotSpecified() {
        try {
            Decision decision = new Decision("1");

            documentation.addDecision(decision);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A title must be specified.", iae.getMessage());
        }
    }

    @Test
    void addDecision_ThrowsAnException_WhenTheContentIsNotSpecified() {
        try {
            Decision decision = new Decision("1");
            decision.setTitle("Title");

            documentation.addDecision(decision);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Content must be specified.", iae.getMessage());
        }
    }

    @Test
    void addDecision_ThrowsAnException_WhenTheStatusIsNotSpecified() {
        try {
            Decision decision = new Decision("1");
            decision.setTitle("Title");
            decision.setContent("Content");

            documentation.addDecision(decision);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A status must be specified.", iae.getMessage());
        }
    }

    @Test
    void addDecision_ThrowsAnException_WhenTheFormatIsNotSpecified() {
        try {
            Decision decision = new Decision("1");
            decision.setTitle("Title");
            decision.setContent("Content");
            decision.setStatus("Accepted");

            documentation.addDecision(decision);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A format must be specified.", iae.getMessage());
        }
    }

    @Test
    void addDecision_ThrowsAnException_WhenADecisionExistsWithTheSameId() {
        try {
            Decision decision = new Decision("1");
            decision.setTitle("Title");
            decision.setContent("Content");
            decision.setFormat(Format.Markdown);
            decision.setStatus("Accepted");

            documentation.addDecision(decision);
            documentation.addDecision(decision);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A decision with an ID of 1 already exists in this scope.", iae.getMessage());
        }
    }

}