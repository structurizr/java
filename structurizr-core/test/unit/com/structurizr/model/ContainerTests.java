package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    void technologyProperty() {
        assertEquals("Some technology", container.getTechnology());

        container.setTechnology("Some other technology");
        assertEquals("Some other technology", container.getTechnology());
    }

    @Test
    void getCanonicalName() {
        assertEquals("Container://System.Container", container.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        container = softwareSystem.addContainer("Name1/.Name2", "Description", "Some technology");

        assertEquals("Container://System.Name1Name2", container.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentSoftwareSystem() {
        assertEquals(softwareSystem, container.getParent());
    }

    @Test
    void getSoftwareSystem_ReturnsTheParentSoftwareSystem() {
        assertEquals(softwareSystem, container.getSoftwareSystem());
    }

    @Test
    void removeTags_DoesNotRemoveRequiredTags() {
        assertTrue(container.getTags().contains(Tags.ELEMENT));
        assertTrue(container.getTags().contains(Tags.CONTAINER));

        container.removeTag(Tags.CONTAINER);
        container.removeTag(Tags.ELEMENT);

        assertTrue(container.getTags().contains(Tags.ELEMENT));
        assertTrue(container.getTags().contains(Tags.CONTAINER));
    }

    @Test
    void addComponent_ThrowsAnException_WhenANullNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.addComponent(null, "");
        });
    }

    @Test
    void addComponent_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.addComponent(" ", "");
        });
    }

    @Test
    void addComponent_ThrowsAnException_WhenAComponentWithTheSameNameAlreadyExists() {
        container.addComponent("Component 1", "");
        try {
            container.addComponent("Component 1", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component named 'Component 1' already exists for this container.", iae.getMessage());
        }
    }

    @Test
    void addComponent_AddsAComponentWithTheSpecifiedNameAndDescription() {
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
    void addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnology() {
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
    void addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnologyAndStringType() {
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
    void addComponent_AddsAComponentWithTheSpecifiedNameAndDescriptionAndTechnologyAndClassType() {
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
    void getComponentWithName_ThrowsAnException_WhenANullNameIsSpecified() {
        try {
            container.getComponentWithName(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component name must be provided.", iae.getMessage());
        }
    }

    @Test
    void getComponentWithName_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        try {
            container.getComponentWithName(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component name must be provided.", iae.getMessage());
        }
    }

    @Test
    void getComponentOfType_ThrowsAnException_WhenANullTypeIsSpecified() {
        try {
            container.getComponentOfType(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component type must be provided.", iae.getMessage());
        }
    }

    @Test
    void getComponentOfType_ThrowsAnException_WhenAnEmptyTypeIsSpecified() {
        try {
            container.getComponentOfType(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A component type must be provided.", iae.getMessage());
        }
    }

    @Test
    void getComponentOfType_ReturnsNull_WhenNoComponentWithTheSpecifiedTypeExists() {
        assertNull(container.getComponentOfType("SomeType"));
    }

    @Test
    void getComponentOfType_ReturnsAComponent_WhenAComponentWithTheSpecifiedTypeExists() {
        container.addComponent("Name", "SomeType", "Description", "Technology");
        Component component = container.getComponentOfType("SomeType");

        assertNotNull(component);
        assertEquals("SomeType", component.getType().getType());
    }

}