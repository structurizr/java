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
    public void test_addSoftwareSystem() {
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
    public void test_addPerson() {
        assertTrue(model.getPeople().isEmpty());
        Person person = model.addPerson(Location.Internal, "Some internal user", "Some description");
        assertEquals(1, model.getPeople().size());

        assertEquals(Location.Internal, person.getLocation());
        assertEquals("Some internal user", person.getName());
        assertEquals("Some description", person.getDescription());
        assertTrue(person.getId() > 0);
        assertSame(person, model.getPeople().iterator().next());
    }

}
