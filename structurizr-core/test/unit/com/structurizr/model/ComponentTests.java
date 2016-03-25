package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComponentTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System", "Description");
    private Container container = softwareSystem.addContainer("Container", "Description", "Some technology");

    @Test
    public void test_getName_ReturnsTheGivenName_WhenANameIsGiven() {
        Component component = new Component();
        component.setName("Some name");
        assertEquals("Some name", component.getName());
    }

    @Test
    public void test_getCanonicalName() {
        Component component = container.addComponent("Component", "Description");
        assertEquals("/System/Container/Component", component.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenNameContainsASlashCharacter() {
        Component component = container.addComponent("Name1/Name2", "Description");
        assertEquals("/System/Container/Name1Name2", component.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentContainer() {
        Component component = container.addComponent("Component", "Description");
        assertEquals(container, component.getParent());
    }

    @Test
    public void test_getContainer_ReturnsTheParentContainer() {
        Component component = container.addComponent("Name", "Description");
        assertEquals(container, component.getContainer());
    }

    @Test
    public void test_removeTags_DoesNotRemoveRequiredTags() {
        Component component = new Component();
        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));

        component.removeTag(Tags.COMPONENT);
        component.removeTag(Tags.ELEMENT);

        assertTrue(component.getTags().contains(Tags.ELEMENT));
        assertTrue(component.getTags().contains(Tags.COMPONENT));
    }

    @Test
    public void test_getPackage_ReturnsNull_WhenNoTypeHasBeenSet() {
        Component component = new Component();
        assertNull(component.getType());
        assertNull(component.getPackage());
    }

    @Test
    public void test_getPackage_ReturnsThePackageName_WhenATypeHasBeenSet() {
        Component component = new Component();
        component.setType(ComponentTests.class.getCanonicalName());
        assertEquals("com.structurizr.model", component.getPackage());
    }

}
