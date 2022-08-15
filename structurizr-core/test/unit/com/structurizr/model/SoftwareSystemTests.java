package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class SoftwareSystemTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Name", "Description");

    @Test
    void test_addContainer_ThrowsAnException_WhenANullNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            softwareSystem.addContainer(null, "", "");
        });
    }

    @Test
    void test_addContainer_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        assertThrows(IllegalArgumentException.class, () -> {
            softwareSystem.addContainer(" ", "", "");
        });
    }

    @Test
    void test_addContainer_AddsAContainer_WhenAContainerWithTheSameNameDoesNotExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertEquals("Web Application", container.getName());
        assertEquals("Description", container.getDescription());
        assertEquals("Spring MVC", container.getTechnology());
        assertEquals("2", container.getId());
        assertEquals(1, softwareSystem.getContainers().size());
        assertSame(container, softwareSystem.getContainers().iterator().next());
    }

    @Test
    void test_addContainer_ThrowsAnException_WhenAContainerWithTheSameNameAlreadyExists() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertEquals(1, softwareSystem.getContainers().size());

        try {
            softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
            fail();
        } catch (Exception e) {
            assertEquals("A container named 'Web Application' already exists for this software system.", e.getMessage());
        }
    }

    @Test
    void test_getContainerWithName_ReturnsNull_WhenAContainerWithTheSpecifiedNameDoesNotExist() {
        assertNull(softwareSystem.getContainerWithName("Web Application"));
    }

    @Test
    void test_GetContainerWithName_ReturnsAContainer_WhenAContainerWithTheSpecifiedNameDoesExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertSame(container, softwareSystem.getContainerWithName("Web Application"));
    }

    @Test
    void test_getContainerWithId_ReturnsNull_WhenAContainerWithTheSpecifiedIdDoesNotExist() {
        assertNull(softwareSystem.getContainerWithId("100"));
    }

    @Test
    void test_GetContainerWithId_ReturnsAContainer_WhenAContainerWithTheSpecifiedIdDoesExist() {
        Container container = softwareSystem.addContainer("Web Application", "Description", "Spring MVC");
        assertSame(container, softwareSystem.getContainerWithId(container.getId()));
    }

    @Test
    void test_uses_AddsAUnidirectionalRelationshipBetweenTwoSoftwareSystems() {
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
    void test_uses_AddsAUnidirectionalRelationshipBetweenTwoSoftwareSystems_WhenADifferentRelationshipAlreadyExists() {
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
    void test_uses_DoesNotAddAUnidirectionalRelationshipBetweenTwoSoftwareSystems_WhenTheSameRelationshipAlreadyExists() {
        SoftwareSystem systemA = model.addSoftwareSystem(Location.Internal, "System A", "Description");
        SoftwareSystem systemB = model.addSoftwareSystem(Location.Internal, "System B", "Description");
        systemA.uses(systemB, "Gets data using the REST API");
        systemA.uses(systemB, "Gets data using the REST API");

        assertEquals(1, systemA.getRelationships().size());
    }

    @Test
    void test_delivers_AddsAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.delivers(person, "E-mails results to");

        assertEquals(1, system.getRelationships().size());
        assertEquals(0, person.getRelationships().size());
        Relationship relationship = system.getRelationships().iterator().next();
        assertEquals(system, relationship.getSource());
        assertEquals(person, relationship.getDestination());
        assertEquals("E-mails results to", relationship.getDescription());
    }

    @Test
    void test_delivers_AddsAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson_WhenADifferentRelationshipAlreadyExists() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.delivers(person, "E-mails results to");
        system.delivers(person, "Text messages results to");

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
    void test_delivers_DoesNotAddAUnidirectionalRelationshipBetweenASoftwareSystemAndAPerson_WhenTheSameRelationshipAlreadyExists() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        Person person = model.addPerson(Location.Internal, "User", "Description");
        system.delivers(person, "E-mails results to");
        system.delivers(person, "E-mails results to");

        assertEquals(1, system.getRelationships().size());
    }

    @Test
    void test_getTags_IncludesSoftwareSystemByDefault() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        assertEquals("Element,Software System", system.getTags());
    }

    @Test
    void test_getCanonicalName() {
        SoftwareSystem system = model.addSoftwareSystem(Location.Internal, "System", "Description");
        assertEquals("SoftwareSystem://System", system.getCanonicalName());
    }

    @Test
    void test_getCanonicalName_WhenNameContainsSlashAndDotCharacters() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name1/.Name2", "Description");
        assertEquals("SoftwareSystem://Name1Name2", softwareSystem.getCanonicalName());
    }

    @Test
    void test_getParent_ReturnsNull() {
        assertNull(softwareSystem.getParent());
    }

    @Test
    void test_removeTags_DoesNotRemoveRequiredTags() {
        assertTrue(softwareSystem.getTags().contains(Tags.ELEMENT));
        assertTrue(softwareSystem.getTags().contains(Tags.SOFTWARE_SYSTEM));

        softwareSystem.removeTag(Tags.SOFTWARE_SYSTEM);
        softwareSystem.removeTag(Tags.ELEMENT);

        assertTrue(softwareSystem.getTags().contains(Tags.ELEMENT));
        assertTrue(softwareSystem.getTags().contains(Tags.SOFTWARE_SYSTEM));
    }

    @Test
    void test_getContainerWithName_ThrowsAnException_WhenANullNameIsSpecified() {
        try {
            softwareSystem.getContainerWithName(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container name must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_getContainerWithName_ThrowsAnException_WhenAnEmptyNameIsSpecified() {
        try {
            softwareSystem.getContainerWithName(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container name must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_getContainerWithId_ThrowsAnException_WhenANullIdIsSpecified() {
        try {
            softwareSystem.getContainerWithId(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container ID must be provided.", iae.getMessage());
        }
    }

    @Test
    void test_getContainerWithId_ThrowsAnException_WhenAnEmptyIdIsSpecified() {
        try {
            softwareSystem.getContainerWithId(" ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container ID must be provided.", iae.getMessage());
        }
    }

}