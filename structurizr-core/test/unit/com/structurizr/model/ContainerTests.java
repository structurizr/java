package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @Test
    public void test_addComponent_ThrowsAnException_WhenAComponentWithTheSameNameAlreadyExists() {
        container.addComponent("Component 1", "");
        try {
            container.addComponent("Component 1", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component named 'Component 1' already exists for this container.", iae.getMessage());
        }
    }

    @Test
    public void test_getComponentWithName_ThrowsAnException_WhenANullNameIsSpecified() {
        try {
            container.getComponentWithName(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component name must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_getComponentWithName_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        try {
            container.getComponentWithName(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component name must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_getComponentOfType_ThrowsAnException_WhenANullTypeIsSpecified() {
        try {
            container.getComponentOfType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component type must be provided.", iae.getMessage());
        }
    }

    @Test
    public void test_getComponentOfType_ThrowsAnException_WhenAnEmptyTypeIsSpecified() {
        try {
            container.getComponentOfType(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component type must be provided.", iae.getMessage());
        }
    }

}