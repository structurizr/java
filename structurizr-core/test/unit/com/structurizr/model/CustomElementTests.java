package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomElementTests extends AbstractWorkspaceTestBase {

    @Test
    void basicProperties() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertEquals("Name", element.getName());
        assertEquals("Type", element.getMetadata());
        assertEquals("Description", element.getDescription());
    }

    @Test
    void getCanonicalName() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertEquals("Custom://Name", element.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        element.setName("Name1/.Name2");
        assertEquals("Custom://Name1Name2", element.getCanonicalName());
    }

    @Test
    void getParent_ReturnsNull() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertNull(element.getParent());
    }

    @Test
    void removeTags_DoesNotRemoveRequiredTags() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertTrue(element.getTags().contains(Tags.ELEMENT));

        element.removeTag(Tags.ELEMENT);

        assertTrue(element.getTags().contains(Tags.ELEMENT));
    }

    @Test
    void uses_AddsARelationshipWhenTheDescriptionIsSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses");
        assertEquals(1, element1.getRelationships().size());

        Relationship relationship = element1.getRelationships().iterator().next();
        assertSame(element1, relationship.getSource());
        assertSame(element2, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertNull(relationship.getTechnology());
        assertNull(relationship.getInteractionStyle());
    }

    @Test
    void uses_AddsARelationshipWhenTheDescriptionAndTechnologyAreSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses", "Technology");
        assertEquals(1, element1.getRelationships().size());

        Relationship relationship = element1.getRelationships().iterator().next();
        assertSame(element1, relationship.getSource());
        assertSame(element2, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
        assertNull(relationship.getInteractionStyle());
    }

    @Test
    void uses_AddsARelationshipWhenTheDescriptionAndTechnologyAndInteractionStyleAreSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses", "Technology", InteractionStyle.Asynchronous);
        assertEquals(1, element1.getRelationships().size());

        Relationship relationship = element1.getRelationships().iterator().next();
        assertSame(element1, relationship.getSource());
        assertSame(element2, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());
    }

    @Test
    void uses_AddsARelationshipWhenTheDescriptionAndTechnologyAndInteractionStyleAndTagsAreSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses", "Technology", InteractionStyle.Asynchronous, new String[]{"Tag 1", "Tag 2"});
        assertEquals(1, element1.getRelationships().size());

        Relationship relationship = element1.getRelationships().iterator().next();
        assertSame(element1, relationship.getSource());
        assertSame(element2, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
        assertEquals(InteractionStyle.Asynchronous, relationship.getInteractionStyle());
        assertEquals("Relationship,Asynchronous,Tag 1,Tag 2", relationship.getTags());
    }

}