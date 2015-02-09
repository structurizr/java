package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void test_getName_ReturnsTheInterfaceType_WhenANameIsNotGivenAndTheInterfaceTypeIsSet() {
        Component component = new Component();
        component.setInterfaceType("com.structurizr.component.SomeComponent");
        component.setImplementationType("com.structurizr.component.SomeComponentImpl");
        assertEquals("SomeComponent", component.getName());
    }

    @Test
    public void test_getName_ReturnsTheImplementationType_WhenOnlyTheImplementationTypeIsSet() {
        Component component = new Component();
        component.setImplementationType("com.structurizr.component.SomeController");
        assertEquals("SomeController", component.getName());
    }

    @Test
    public void test_getCanonicalName() {
        Component component = container.addComponent("Component", "Description");
        assertEquals("/System/Container/Component", component.getCanonicalName());
    }

}
