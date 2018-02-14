package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContainerTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    public void test_getCanonicalName() {
        assertEquals("/System/Container", container.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsASlashCharacter() {
        container = softwareSystem.addContainer("Name1/Name2", "Description", "Some technology");

        assertEquals("/System/Name1Name2", container.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentSoftwareSystem() {
        assertEquals(softwareSystem, container.getParent());
    }

    @Test
    public void test_getSoftwareSystem_ReturnsTheParentSoftwareSystem() {
        assertEquals(softwareSystem, container.getSoftwareSystem());
    }

    @Test
    public void test_removeTags_DoesNotRemoveRequiredTags() {
        assertTrue(container.getTags().contains(Tags.ELEMENT));
        assertTrue(container.getTags().contains(Tags.CONTAINER));

        container.removeTag(Tags.CONTAINER);
        container.removeTag(Tags.ELEMENT);

        assertTrue(container.getTags().contains(Tags.ELEMENT));
        assertTrue(container.getTags().contains(Tags.CONTAINER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addComponent_ThrowsAnException_WhenANullNameIsSpecified() {
        container.addComponent(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_addComponent_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        container.addComponent(" ", "");
    }

}
