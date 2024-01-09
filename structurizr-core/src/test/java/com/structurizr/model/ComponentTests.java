package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem("System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    void getName_ReturnsTheGivenName_WhenANameIsGiven() {
        Component component = new Component();
        component.setName("Some name");
        assertEquals("Some name", component.getName());
    }

    @Test
    void getCanonicalName() {
        Component component = container.addComponent("Component", "Description");
        assertEquals("Component://System.Container.Component", component.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        Component component = container.addComponent("Name1/.Name2", "Description");
        assertEquals("Component://System.Container.Name1Name2", component.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentContainer() {
        Component component = container.addComponent("Component", "Description");
        assertEquals(container, component.getParent());
    }

    @Test
    void getContainer_ReturnsTheParentContainer() {
        Component component = container.addComponent("Name", "Description");
        assertEquals(container, component.getContainer());
    }

    @Test
    void removeTags_DoesNotRemoveRequiredTags() {
        Component component = new Component();
        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));

        component.removeTag(Tags.COMPONENT);
        component.removeTag(Tags.ELEMENT);

        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));
    }

    @Test
    void technologyProperty() {
        Component component = new Component();
        assertNull(component.getTechnology());

        component.setTechnology("Spring Bean");
        assertEquals("Spring Bean", component.getTechnology());
    }

}