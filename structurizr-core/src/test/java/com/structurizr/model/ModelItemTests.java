package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ModelItemTests extends AbstractWorkspaceTestBase {

    @Test
    void construction() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals("Name", element.getName());
        assertEquals("Description", element.getDescription());
    }

    @Test
    void getTags_WhenThereAreNoTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    void hasTag_ChecksRequiredTags() {
        SoftwareSystem system = model.addSoftwareSystem("Name", "Description");
        assertTrue(system.hasTag("Software System"), "hasTag returns true for Software System");
        assertTrue(system.hasTag("Element"), "hasTag returns true for Element");
    }

    @Test
    void getTags_ReturnsTheListOfTags_WhenThereAreSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags("tag1", "tag2", "tag3");
        assertEquals("Element,Software System,tag1,tag2,tag3", element.getTags());
    }

    @Test
    void setTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setTags(null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    void addTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags((String) null);
        assertEquals("Element,Software System", element.getTags());

        element.addTags(null, null, null);
        assertEquals("Element,Software System", element.getTags());
    }

    @Test
    void addTags_AddsTags_WhenPassedSomeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    void addTags_AddsTags_WhenPassedSomeTagsAndThereAreDuplicateTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags(null, "tag1", null, "tag2", "tag2");
        assertEquals("Element,Software System,tag1,tag2", element.getTags());
    }

    @Test
    void removeTags() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addTags("tag1", "tag2");
        assertTrue(element.removeTag("tag1"), "Remove an existing tag returns true");
        assertFalse(element.hasTag("tag1"), "Tag has been removed");

        assertFalse(element.removeTag("no-such-tag"), "Remove a non-existing tag returns false");
        assertFalse(element.removeTag("Element"), "Remove a required tag returns false");
    }

    @Test
    void getProperties_ReturnsAnEmptyList_WhenNoPropertiesHaveBeenAdded() {
        Element element = model.addSoftwareSystem("Name", "Description");
        assertEquals(0, element.getProperties().size());
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(null, "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheNameIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty(" ", "value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property name must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsNull() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_ThrowsAnException_WhenTheValueIsEmpty() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addProperty("name", " ");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("A property value must be specified.", e.getMessage());
        }
    }

    @Test
    void addProperty_AddsTheProperty_WhenANameAndValueAreSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.addProperty("AWS region", "us-east-1");
        assertEquals("us-east-1", element.getProperties().get("AWS region"));
    }

    @Test
    void setProperties_DoesNothing_WhenNullIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        element.setProperties(null);
        assertEquals(0, element.getProperties().size());
    }

    @Test
    void setProperties_SetsTheProperties_WhenANonEmptyMapIsSpecified() {
        Element element = model.addSoftwareSystem("Name", "Description");
        Map<String, String> properties = new HashMap<>();
        properties.put("name", "value");
        element.setProperties(properties);
        assertEquals(1, element.getProperties().size());
        assertEquals("value", element.getProperties().get("name"));
    }

    @Test
    void addPerspective_ThrowsAnException_WhenANameIsNotSpecified() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addPerspective(null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    void addPerspective_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addPerspective(" ", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    void addPerspective_ThrowsAnException_WhenADescriptionIsNotSpecified() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addPerspective("Security", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A description must be specified.", iae.getMessage());
        }
    }

    @Test
    void addPerspective_ThrowsAnException_WhenAnEmptyDescriptionIsSpecified() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addPerspective("Security", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A description must be specified.", iae.getMessage());
        }
    }

    @Test
    void addPerspective_AddsAPerspective() {
        Element element = model.addSoftwareSystem("Name", "Description");

        Perspective securityPerspective = element.addPerspective("Security", "Data is encrypted at rest.");
        assertEquals("Security", securityPerspective.getName());
        assertEquals("Data is encrypted at rest.", securityPerspective.getDescription());
        assertEquals("", securityPerspective.getValue());
        assertTrue(element.getPerspectives().contains(securityPerspective));

        Perspective technicalDebtPerspective = element.addPerspective("Technical Debt", "High tech debt due to feature X being delivered rapidly.", "High");
        assertEquals("Technical Debt", technicalDebtPerspective.getName());
        assertEquals("High tech debt due to feature X being delivered rapidly.", technicalDebtPerspective.getDescription());
        assertEquals("High", technicalDebtPerspective.getValue());
        assertTrue(element.getPerspectives().contains(technicalDebtPerspective));
    }

    @Test
    void addPerspective_ThrowsAnException_WhenTheNamedPerspectiveAlreadyExists() {
        try {
            Element element = model.addSoftwareSystem("Name", "Description");
            element.addPerspective("Security", "Data is encrypted at rest.");
            element.addPerspective("Security", "Data is encrypted at rest.");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A perspective named \"Security\" already exists.", iae.getMessage());
        }
    }

    @Test
    void setUrl_AcceptsAUrl() {
        Element element = model.addSoftwareSystem("Name");
        element.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", element.getUrl());
    }

    @Test
    void setUrl_AcceptsAnIntraWorkspaceUrl() {
        Element element = model.addSoftwareSystem("Name");
        element.setUrl("{workspace}/diagrams#key");
        assertEquals("{workspace}/diagrams#key", element.getUrl());
    }

    @Test
    void setUrl_AcceptsAnInterWorkspaceUrl() {
        Element element = model.addSoftwareSystem("Name");

        element.setUrl("{workspace:123456}");
        assertEquals("{workspace:123456}", element.getUrl());

        element.setUrl("{workspace:123456}/diagrams#key");
        assertEquals("{workspace:123456}/diagrams#key", element.getUrl());

        element.setUrl("{workspace:123456}/documentation");
        assertEquals("{workspace:123456}/documentation", element.getUrl());
    }

}