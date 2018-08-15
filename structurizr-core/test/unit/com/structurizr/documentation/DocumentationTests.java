package com.structurizr.documentation;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

public class DocumentationTests extends AbstractWorkspaceTestBase {

    private Documentation documentation;

    @Before
    public void setUp() {
        documentation = workspace.getDocumentation();
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenTheRelatedElementIsNotPresentInTheAssociatedModel() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
            new Workspace("", "").getDocumentation().addSection(softwareSystem, "Title", Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The element named Software System does not exist in the model associated with this documentation.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenTheTitleIsNotSpecified() {
        try {
            documentation.addSection(null, null, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A title must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenTheContentIsNotSpecified() {
        try {
            documentation.addSection(null, "Title", Format.Markdown, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Content must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenTheFormatIsNotSpecified() {
        try {
            documentation.addSection(null, "Title", null, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A format must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenASectionExistsWithTheSameTitle() {
        try {
            documentation.addSection(null, "Title", Format.Markdown, "Content");
            documentation.addSection(null, "Title", Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A section with a title of Title already exists for this workspace.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection_ThrowsAnException_WhenASectionExistsWithTheSameTitleForAnElement() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
            documentation.addSection(softwareSystem, "Title", Format.Markdown, "Content");
            documentation.addSection(softwareSystem, "Title", Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A section with a title of Title already exists for the element named Software System.", iae.getMessage());
        }
    }

    @Test
    public void test_addSection() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Section section = documentation.addSection(softwareSystem, "Title", Format.Markdown, "Content");

        assertEquals(1, documentation.getSections().size());
        assertTrue(documentation.getSections().contains(section));
        assertSame(softwareSystem, section.getElement());
        assertEquals("Title", section.getTitle());
        assertEquals(Format.Markdown, section.getFormat());
        assertEquals("Content", section.getContent());
        assertEquals(1, section.getOrder());
    }

    @Test
    public void test_addSection_IncrementsTheSectionOrderNumber() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Section section1 = documentation.addSection(softwareSystem, "Section 1", Format.Markdown, "Content");
        Section section2 = documentation.addSection(softwareSystem, "Section 2", Format.Markdown, "Content");
        Section section3 = documentation.addSection(softwareSystem, "Section 3", Format.Markdown, "Content");

        assertEquals(1, section1.getOrder());
        assertEquals(2, section2.getOrder());
        assertEquals(3, section3.getOrder());
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenTheIdIsNotSpecified() {
        try {
            documentation.addDecision(null, new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An ID must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenTheTitleIsNotSpecified() {
        try {
            documentation.addDecision("1", new Date(), null, DecisionStatus.Accepted, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A title must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenTheContentIsNotSpecified() {
        try {
            documentation.addDecision("1", new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Content must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenTheStatusIsNotSpecified() {
        try {
            documentation.addDecision("1", new Date(), "Title", null, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A status must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenTheFormatIsNotSpecified() {
        try {
            documentation.addDecision("1", new Date(), "Title", DecisionStatus.Accepted, null, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A format must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenADecisionExistsWithTheSameId() {
        try {
            documentation.addDecision("1", new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, "Content");
            documentation.addDecision("1", new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A decision with an ID of 1 already exists for this workspace.", iae.getMessage());
        }
    }

    @Test
    public void test_addDecision_ThrowsAnException_WhenADecisionExistsWithTheSameIdForAnElement() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
            documentation.addDecision(softwareSystem, "1", new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, "Content");
            documentation.addDecision(softwareSystem, "1", new Date(), "Title", DecisionStatus.Accepted, Format.Markdown, "Content");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A decision with an ID of 1 already exists for the element named Software System.", iae.getMessage());
        }
    }

    @Test
    public void test_hydrate() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");

        Section section = new Section();
        section.setElementId(softwareSystem.getId());
        section.setTitle("Title");
        documentation.setSections(Collections.singleton(section));

        Decision decision = new Decision();
        decision.setElementId(softwareSystem.getId());
        documentation.setDecisions(Collections.singleton(decision));

        documentation.hydrate(model);

        assertSame(softwareSystem, section.getElement());
        assertSame(softwareSystem, decision.getElement());
    }

}