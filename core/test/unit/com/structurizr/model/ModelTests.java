package com.structurizr.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTests {

    private Model model = new Model("Name", "Description");

    @Test
    public void test_modelConstruction() {
        model = new Model("Model name", "Model description");
        assertEquals("Model name", model.getName());
        assertEquals("Model description", model.getDescription());
    }

    @Test
    public void test_addSoftwareSystem_AddsTheSoftwareSystem_WhenASoftwareSystemDoesNotExistWithTheSameName() {
        assertTrue(model.getSoftwareSystems().isEmpty());
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        assertEquals(Location.External, softwareSystem.getLocation());
        assertEquals("System A", softwareSystem.getName());
        assertEquals("Some description", softwareSystem.getDescription());
        assertTrue(softwareSystem.getId() > 0);
        assertSame(softwareSystem, model.getSoftwareSystems().iterator().next());
    }

    @Test
    public void test_addSoftwareSystem_DoesNotAddTheSoftwareSystem_WhenASoftwareSystemExistsWithTheSameName() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Some description");
        assertEquals(1, model.getSoftwareSystems().size());

        softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertEquals(1, model.getSoftwareSystems().size());
        assertNull(softwareSystem);
    }

    @Test
    public void test_addPerson_AddsThePerson_WhenAPersonDoesNotExistWithTheSameName() {
        assertTrue(model.getPeople().isEmpty());
        Person person = model.addPerson(Location.Internal, "Some internal user", "Some description");
        assertEquals(1, model.getPeople().size());

        assertEquals(Location.Internal, person.getLocation());
        assertEquals("Some internal user", person.getName());
        assertEquals("Some description", person.getDescription());
        assertTrue(person.getId() > 0);
        assertSame(person, model.getPeople().iterator().next());
    }

    @Test
    public void test_addPerson_DoesNotAddThePerson_WhenAPersonExistsWithTheSameName() {
        Person person = model.addPerson(Location.Internal, "Admin User", "Description");
        assertEquals(1, model.getPeople().size());

        person = model.addPerson(Location.External, "Admin User", "Description");
        assertEquals(1, model.getPeople().size());
        assertNull(person);
    }

    @Test
    public void test_getElement_ReturnsNull_WhenAnElementWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getElement(100));
    }

    @Test
    public void test_getElement_ReturnsAnElement_WhenAnElementWithTheSpecifiedIdDoesExist() {
        Person person = model.addPerson(Location.Internal, "Name", "Description");
        assertSame(person, model.getElement(person.getId()));
    }

    @Test
    public void test_contains_ReturnsFalse_WhenTheSpecifiedElementIsNotInTheModel() {
        Model newModel = new Model("Name", "Description");
        SoftwareSystem softwareSystem = newModel.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertFalse(model.contains(softwareSystem));
    }

    @Test
    public void test_contains_ReturnsTrue_WhenTheSpecifiedElementIsInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Unspecified, "Name", "Description");
        assertTrue(model.contains(softwareSystem));
    }

    @Test
    public void test_getSoftwareSystemWithName_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getSoftwareSystemWithName("System X"));
    }

    @Test
    public void test_getSoftwareSystemWithName_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedNameExists() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithName("System A"));
    }

    @Test
    public void test_getSoftwareSystemWithId_ReturnsNull_WhenASoftwareSystemWithTheSpecifiedIdDoesNotExist() {
        assertNull(model.getSoftwareSystemWithId(100));
    }

    @Test
    public void test_getSoftwareSystemWithId_ReturnsASoftwareSystem_WhenASoftwareSystemWithTheSpecifiedIdDoesExist() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "System A", "Description");
        assertSame(softwareSystem, model.getSoftwareSystemWithId(softwareSystem.getId()));
    }

    @Test
    public void test_getPersonWithName_ReturnsNull_WhenAPersonWithTheSpecifiedNameDoesNotExist() {
        assertNull(model.getPersonWithName("Admin User"));
    }

    @Test
    public void test_getPersonWithName_ReturnsAPerson_WhenAPersonWithTheSpecifiedNameExists() {
        Person person = model.addPerson(Location.External, "Admin User", "Description");
        assertSame(person, model.getPersonWithName("Admin User"));
    }

}
