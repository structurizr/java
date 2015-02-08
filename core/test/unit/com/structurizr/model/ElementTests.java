package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElementTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_getTags_WhenThereAreNoTags() {
        Element element = model.addSoftwareSystem(Location.Internal, "Name", "Description");
        assertEquals("Software System", element.getTags());
    }

    @Test
    public void test_getTags_ReturnsTheListOfTags_WhenThereAreSomeTags() {
        Element element = model.addSoftwareSystem(Location.Internal, "Name", "Description");
        element.addTags("tag1", "tag2", "tag3");
        assertEquals("Software System,tag1,tag2,tag3", element.getTags());
    }

    @Test
    public void test_setTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem(Location.Internal, "Name", "Description");
        element.setTags(null);
        assertEquals("Software System", element.getTags());
    }

    @Test
    public void test_addTags_DoesNotDoAnything_WhenPassedNull() {
        Element element = model.addSoftwareSystem(Location.Internal, "Name", "Description");
        element.addTags(null);
        assertEquals("Software System", element.getTags());

        element.addTags(null, null, null);
        assertEquals("Software System", element.getTags());
    }

    @Test
    public void test_addTags_AddsTags_WhenPassedSomeTags() {
        Element element = model.addSoftwareSystem(Location.Internal, "Name", "Description");
        element.addTags(null, "tag1", null, "tag2");
        assertEquals("Software System,tag1,tag2", element.getTags());
    }

    @Test
    public void test_equals_ReturnsTrue_WhenTheSameObjectIsPassed() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "SystemA", "");
        assertTrue(softwareSystem.equals(softwareSystem));
    }

    @Test
    public void test_equals_ReturnsTrue_WhenTheAnObjectWithTheSameCanonicalNameIsPassed() {
        SoftwareSystem softwareSystem1 = new Workspace("", "").getModel().addSoftwareSystem(Location.Internal, "SystemA", "");
        SoftwareSystem softwareSystem2 = new Workspace("", "").getModel().addSoftwareSystem(Location.Internal, "SystemA", "");
        assertTrue(softwareSystem1.equals(softwareSystem2));
        assertTrue(softwareSystem2.equals(softwareSystem1));
    }

}