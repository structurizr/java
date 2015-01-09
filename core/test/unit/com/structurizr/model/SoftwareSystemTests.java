package com.structurizr.model;

import org.junit.Test;

import java.util.Iterator;

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

    @Test
    public void test_uses_AddsAUnidirectionalRelationshipBetweenTwoSoftwareSystems() {
        SoftwareSystem systemA = model.addSoftwareSystem(Location.Internal, "System A", "Description");
        SoftwareSystem systemB = model.addSoftwareSystem(Location.Internal, "System B", "Description");
        systemA.uses(systemB, "Gets some data from");

        assertEquals(1, systemA.getRelationships().size());
        assertEquals(0, systemB.getRelationships().size());
        Relationship relationship = systemA.getRelationships().iterator().next();
        assertEquals(systemA, relationship.getSource());
        assertEquals(systemB, relationship.getDestination());
        assertEquals("Gets some data from", relationship.getDescription());
    }

    @Test
    public void test_uses_AddsAUnidirectionalRelationshipBetweenTwoSoftwareSystems_WhenADifferentRelationshipAlreadyExists() {
        SoftwareSystem systemA = model.addSoftwareSystem(Location.Internal, "System A", "Description");
        SoftwareSystem systemB = model.addSoftwareSystem(Location.Internal, "System B", "Description");
        systemA.uses(systemB, "Gets data using the REST API");
        systemA.uses(systemB, "Subscribes to updates using the Streaming API");

        Iterator<Relationship> it = systemA.getRelationships().iterator();
        assertEquals(2, systemA.getRelationships().size());
        Relationship relationship = it.next();
        assertEquals(systemA, relationship.getSource());
        assertEquals(systemB, relationship.getDestination());
        assertEquals("Gets data using the REST API", relationship.getDescription());

        relationship = it.next();
        assertEquals(systemA, relationship.getSource());
        assertEquals(systemB, relationship.getDestination());
        assertEquals("Subscribes to updates using the Streaming API", relationship.getDescription());
    }

    @Test
    public void test_uses_DoesNotAddAUnidirectionalRelationshipBetweenTwoSoftwareSystems_WhenTheSameRelationshipAlreadyExists() {
        SoftwareSystem systemA = model.addSoftwareSystem(Location.Internal, "System A", "Description");
        SoftwareSystem systemB = model.addSoftwareSystem(Location.Internal, "System B", "Description");
        systemA.uses(systemB, "Gets data using the REST API");
        systemA.uses(systemB, "Gets data using the REST API");

        assertEquals(1, systemA.getRelationships().size());
    }

    @Test
    public void test_sendsSomethingTo_AddsAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.sendsSomethingTo(person, "E-mails results to");

        assertEquals(1, system.getRelationships().size());
        assertEquals(0, person.getRelationships().size());
        Relationship relationship = system.getRelationships().iterator().next();
        assertEquals(system, relationship.getSource());
        assertEquals(person, relationship.getDestination());
        assertEquals("E-mails results to", relationship.getDescription());
    }

    @Test
    public void test_sendsSomethingTo_AddsAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson_WhenADifferentRelationshipAlreadyExists() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.sendsSomethingTo(person, "E-mails results to");
        system.sendsSomethingTo(person, "Text messages results to");

        Iterator<Relationship> it = system.getRelationships().iterator();
        assertEquals(2, system.getRelationships().size());
        assertEquals(0, person.getRelationships().size());
        Relationship relationship = it.next();
        assertEquals(system, relationship.getSource());
        assertEquals(person, relationship.getDestination());
        assertEquals("E-mails results to", relationship.getDescription());

        relationship = it.next();
        assertEquals(system, relationship.getSource());
        assertEquals(person, relationship.getDestination());
        assertEquals("Text messages results to", relationship.getDescription());
    }

    @Test
    public void test_sendsSomethingTo_DoesNotAddAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson_WhenTheSameRelationshipAlreadyExists() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.sendsSomethingTo(person, "E-mails results to");
        system.sendsSomethingTo(person, "E-mails results to");

        assertEquals(1, system.getRelationships().size());
    }

}
