package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RelationshipTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem1, softwareSystem2;

    @Before
    public void setUp() {
        softwareSystem1 = model.addSoftwareSystem(Location.Internal, "Name1", "Description");
        softwareSystem2 = model.addSoftwareSystem(Location.Internal, "Name2", "Description");
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
    public void test_setTags_DoesNotDoAnything_WhenPassedNull() {
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "uses");
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

}