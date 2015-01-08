package com.structurizr.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SoftwareSystemTests {

    private Model model = new Model("Name", "Description");
    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Name", "Description");

    @Test
    public void test_getType_ReturnsSoftwareSystem() {
        assertEquals(ElementType.SoftwareSystem, softwareSystem.getType());
    }

    @Test
    public void test_addContainer_AddsAContainer_WhenAContainerWithTheSameNameDoesNotExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertEquals("Web Application", container.getName());
        assertEquals("Description", container.getDescription());
        assertEquals("Spring MVC", container.getTechnology());
        assertTrue(container.getId() > 0);
        assertEquals(1, softwareSystem.getContainers().size());
        assertSame(container, softwareSystem.getContainers().iterator().next());
    }

    @Test
    public void test_addContainer_DoesNotAddAContainer_WhenAContainerWithTheSameNameAlreadyExists() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertEquals(1, softwareSystem.getContainers().size());

        container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertEquals(1, softwareSystem.getContainers().size());
        assertNull(container);
    }

    @Test
    public void test_getContainerWithName_ReturnsNull_WhenAContainerWithTheSpecifiedNameDoesNotExist() {
        assertNull(softwareSystem.getContainerWithName("Web Application"));
    }

    @Test
    public void test_GetContainerWithName_ReturnsAContainer_WhenAContainerWithTheSpecifiedNameDoesExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertSame(container, softwareSystem.getContainerWithName("Web Application"));
    }

    @Test
    public void test_getContainerWithId_ReturnsNull_WhenAContainerWithTheSpecifiedIdDoesNotExist() {
        assertNull(softwareSystem.getContainerWithId(100));
    }

    @Test
    public void test_GetContainerWithId_ReturnsAContainer_WhenAContainerWithTheSpecifiedIdDoesExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertSame(container, softwareSystem.getContainerWithId(container.getId()));
    }

}
