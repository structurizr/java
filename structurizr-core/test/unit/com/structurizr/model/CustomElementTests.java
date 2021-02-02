package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomElementTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_basicProperties() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertEquals("Name", element.getName());
        assertEquals("Type", element.getMetadata());
        assertEquals("Description", element.getDescription());
    }

    @Test
    public void test_getCanonicalName() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertEquals("Custom://Name", element.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        element.setName("Name1/.Name2");
        assertEquals("Custom://Name1Name2", element.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsNull() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertNull(element.getParent());
    }

    @Test
    public void test_removeTags_DoesNotRemoveRequiredTags() {
        CustomElement element = model.addCustomElement("Name", "Type", "Description");
        assertTrue(element.getTags().contains(Tags.ELEMENT));

        element.removeTag(Tags.ELEMENT);

        assertTrue(element.getTags().contains(Tags.ELEMENT));
    }

    @Test
    public void test_uses_AddsARelationshipWhenTheDescriptionIsSpecified() {
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
    public void test_uses_AddsARelationshipWhenTheDescriptionAndTechnologyAreSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses", "Technology");
        assertEquals(1, element1.getRelationships().size());

        Relationship relationship = element1.getRelationships().iterator().next();
        assertSame(element1, relationship.getSource());
        assertSame(element2, relationship.getDestination());
        assertEquals("Uses", relationship.getDescription());
        assertEquals("Technology", relationship.getTechnology());
        assertEquals(null, relationship.getInteractionStyle());
    }

    @Test
    public void test_uses_AddsARelationshipWhenTheDescriptionAndTechnologyAndInteractionStyleAreSpecified() {
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
    public void test_uses_AddsARelationshipWhenTheDescriptionAndTechnologyAndInteractionStyleAndTagsAreSpecified() {
        CustomElement element1 = model.addCustomElement("Box 1");
        CustomElement element2 = model.addCustomElement("Box 2");

        element1.uses(element2, "Uses", "Technology", InteractionStyle.Asynchronous, new String[] { "Tag 1", "Tag 2" });
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