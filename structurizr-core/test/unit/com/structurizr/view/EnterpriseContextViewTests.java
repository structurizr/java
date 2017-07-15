package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.Enterprise;
import com.structurizr.model.Location;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EnterpriseContextViewTests extends AbstractWorkspaceTestBase {

    private EnterpriseContextView view;

    @Before
    public void setUp() {
        view = new EnterpriseContextView(model, "context", "Description");
    }

    @Test
    public void test_construction() {
        assertEquals("Enterprise Context", view.getName());
        assertEquals(0, view.getElements().size());
        assertSame(model, view.getModel());
    }

    @Test
    public void test_getName_WhenNoEnterpriseIsSpecified() {
        assertEquals("Enterprise Context", view.getName());
    }

    @Test
    public void test_getName_WhenAnEnterpriseIsSpecified() {
        model.setEnterprise(new Enterprise("Widgets Limited"));
        assertEquals("Enterprise Context for Widgets Limited", view.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getName_WhenAnEmptyEnterpriseNameIsSpecified() {
        model.setEnterprise(new Enterprise(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getName_WhenANullEnterpriseNameIsSpecified() {
        model.setEnterprise(new Enterprise(null));
    }

    @Test
    public void test_addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystems() {
        view.addAllSoftwareSystems();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllSoftwareSystems_AddsAllSoftwareSystems_WhenThereAreSomeSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");

        view.addAllSoftwareSystems();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    public void test_addAllPeople_DoesNothing_WhenThereAreNoPeople() {
        view.addAllPeople();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllPeople_AddsAllPeople_WhenThereAreSomePeopleInTheModel() {
        Person userA = model.addPerson("User A", "Description");
        Person userB = model.addPerson("User B", "Description");

        view.addAllPeople();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
    }

    @Test
    public void test_addAllElements_DoesNothing_WhenThereAreNoSoftwareSystemsOrPeople() {
        view.addAllElements();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllElements_AddsAllSoftwareSystemsAndPeople_WhenThereAreSomeSoftwareSystemsAndPeopleInTheModel() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Person person = model.addPerson("Person", "Description");

        view.addAllElements();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(person)));
    }

}