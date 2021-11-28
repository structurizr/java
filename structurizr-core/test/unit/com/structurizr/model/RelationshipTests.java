package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RelationshipTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem1, softwareSystem2;

    @Before
    public void setUp() {
        softwareSystem1 = model.addSoftwareSystem(Location.Internal, "Name1", "Description");
        softwareSystem2 = model.addSoftwareSystem(Location.Internal, "Name2", "Description");
    }

    @Test
    public void test_getDescription_NeverReturnsNull() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, null);
        assertEquals("", relationship.getDescription());
    }

    @Test
    public void test_getTags_WhenThereAreNoTags() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        assertEquals("Relationship", relationship.getTags());
    }

    @Test
    public void test_getTags_ReturnsTheListOfTags_WhenThereAreSomeTags() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        relationship.addTags("tag1", "tag2", "tag3");
        assertEquals("Relationship,tag1,tag2,tag3", relationship.getTags());
    }

    @Test
    public void test_setTags_ClearsTheTags_WhenPassedNull() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        relationship.addTags("Tag 1", "Tag 2");
        assertEquals("Relationship,Tag 1,Tag 2", relationship.getTags());
        relationship.setTags(null);
        assertEquals("Relationship", relationship.getTags());
    }

    @Test
    public void test_addTags_DoesNotDoAnything_WhenPassedNull() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        relationship.addTags((String)null);
        assertEquals("Relationship", relationship.getTags());

        relationship.addTags(null, null, null);
        assertEquals("Relationship", relationship.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTags() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        relationship.addTags(null, "tag1", null, "tag2");
        assertEquals("Relationship,tag1,tag2", relationship.getTags());
    }

    @Test
    public void test_getInteractionStyle_ReturnsNull_WhenNotExplicitlySet() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
        assertNull(relationship.getInteractionStyle());
    }

    @Test
    public void test_getTags_IncludesTheInteractionStyleWhenSpecified() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology");
        assertFalse(relationship.getTags().contains(Tags.SYNCHRONOUS));
        assertFalse(relationship.getTags().contains(Tags.ASYNCHRONOUS));

        relationship.setInteractionStyle(InteractionStyle.Synchronous);
        assertTrue(relationship.getTags().contains(Tags.SYNCHRONOUS));
        assertFalse(relationship.getTags().contains(Tags.ASYNCHRONOUS));

        relationship.setInteractionStyle(InteractionStyle.Asynchronous);
        assertFalse(relationship.getTags().contains(Tags.SYNCHRONOUS));
        assertTrue(relationship.getTags().contains(Tags.ASYNCHRONOUS));
    }

    @Test
    public void test_setUrl() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology");
        relationship.setUrl("https://structurizr.com");
        assertEquals("https://structurizr.com", relationship.getUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setUrl_ThrowsAnIllegalArgumentException_WhenAnInvalidUrlIsSpecified() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology");
        relationship.setUrl("htt://blah");
    }

    @Test
    public void test_setUrl_ResetsTheUrl_WhenANullUrlIsSpecified() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology");
        relationship.setUrl("https://structurizr.com");
        relationship.setUrl(null);
        assertNull(relationship.getUrl());
    }

    @Test
    public void test_setUrl_ResetsTheUrl_WhenAnEmptyUrlIsSpecified() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology");
        relationship.setUrl("https://structurizr.com");
        relationship.setUrl(" ");
        assertNull(relationship.getUrl());
    }

    @Test
    public void test_interactionStyle_CanBeSetToNull() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses 1", "Technology", null);

        assertNull(relationship.getInteractionStyle());
        assertFalse(relationship.getTagsAsSet().contains(Tags.ASYNCHRONOUS));
        assertFalse(relationship.getTagsAsSet().contains(Tags.SYNCHRONOUS));
    }

}