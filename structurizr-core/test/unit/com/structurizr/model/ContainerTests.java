package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    public void test_technologyProperty() {
        assertEquals("Some technology", container.getTechnology());

        container.setTechnology("Some other technology");
        assertEquals("Some other technology", container.getTechnology());
    }

    @Test
    public void test_getCanonicalName() {
        assertEquals("Container://System.Container", container.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        container = softwareSystem.addContainer("Name1/.Name2", "Description", "Some technology");

        assertEquals("Container://System.Name1Name2", container.getCanonicalName());
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
    public void test_addComponent_AddsAComponentWithTheSpecifiedNameAndDescription() {
        Component component = container.addComponent("Name", "Description");
        assertTrue(container.getComponents().contains(component));
        assertEquals("Name", component.getName());
        assertEquals("Description", component.getDescription());
        assertNull(component.getTechnology());
        assertNull(component.getType());
        assertEquals(0, component.getCode().size());
        assertSame(container, component.getParent());
    }

    @Test
    public void test_addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnology() {
        Component component = container.addComponent("Name", "Description", "Technology");
        assertTrue(container.getComponents().contains(component));
        assertEquals("Name", component.getName());
        assertEquals("Description", component.getDescription());
        assertEquals("Technology", component.getTechnology());
        assertNull(component.getType());
        assertEquals(0, component.getCode().size());
        assertSame(container, component.getParent());
    }

    @Test
    public void test_addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnologyAndStringType() {
        Component component = container.addComponent("Name", "SomeType", "Description", "Technology");
        assertTrue(container.getComponents().contains(component));
        assertEquals("Name", component.getName());
        assertEquals("Description", component.getDescription());
        assertEquals("Technology", component.getTechnology());
        assertEquals("SomeType", component.getType().getType());
        assertEquals(1, component.getCode().size());
        assertSame(container, component.getParent());
    }

    @Test
    public void test_addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnologyAndClassType() {
        Component component = container.addComponent("Name", this.getClass(), "Description", "Technology");
        assertTrue(container.getComponents().contains(component));
        assertEquals("Name", component.getName());
        assertEquals("Description", component.getDescription());
        assertEquals("Technology", component.getTechnology());
        assertEquals("com.structurizr.model.ContainerTests", component.getType().getType());
        assertEquals(1, component.getCode().size());
        assertSame(container, component.getParent());
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

    @Test
    public void test_getComponentOfType_ReturnsNull_WhenNoComponentWithTheSpecifiedTypeExists() {
        assertNull(container.getComponentOfType("SomeType"));
    }

    @Test
    public void test_getComponentOfType_ReturnsAComponent_WhenAComponentWithTheSpecifiedTypeExists() {
        container.addComponent("Name", "SomeType", "Description", "Technology");
        Component component = container.getComponentOfType("SomeType");

        assertNotNull(component);
        assertEquals("SomeType", component.getType().getType());
    }

}