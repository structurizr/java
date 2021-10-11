package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SystemContextViewTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private SystemContextView view;

    @Before
    public void setUp() {
        softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        view = new SystemContextView(softwareSystem, "context", "Description");
    }

    @Test
    public void test_construction() {
        assertEquals("The System - System Context", view.getName());
        assertEquals(1, view.getElements().size());
        assertSame(view.getElements().iterator().next().getElement(), softwareSystem);
        assertSame(softwareSystem, view.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), view.getSoftwareSystemId());
        assertSame(model, view.getModel());
    }

    @Test
    public void test_addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystems() {
        assertEquals(1, view.getElements().size());
        view.addAllSoftwareSystems();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addAllSoftwareSystems_AddsAllSoftwareSystems_WhenThereAreSomeSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");

        view.addAllSoftwareSystems();

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    public void test_addAllPeople_DoesNothing_WhenThereAreNoPeople() {
        assertEquals(1, view.getElements().size());
        view.addAllPeople();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addAllPeople_AddsAllPeople_WhenThereAreSomePeopleInTheModel() {
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");

        view.addAllPeople();

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
    }

    @Test
    public void test_addAllElements_DoesNothing_WhenThereAreNoSoftwareSystemsOrPeople() {
        assertEquals(1, view.getElements().size());
        view.addAllElements();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addAllElements_AddsAllSoftwareSystemsAndPeople_WhenThereAreSomeSoftwareSystemsAndPeopleInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");

        view.addAllElements();

        assertEquals(5, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
    }

    @Test
    public void test_addNearestNeighbours_ThrowsAnException_WhenANullElementIsSpecified() {
        try {
            view.addNearestNeighbours(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addNearestNeighbours_ThrowsAnException_WhenAnElementThatIsNotAPersonOrSoftwareSystemIsSpecified() {
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        try {
            view.addNearestNeighbours(container);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A person or software system must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addNearestNeighbours_DoesNothing_WhenThereAreNoNeighbours() {
        view.addNearestNeighbours(softwareSystem);

        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addNearestNeighbours_AddsNearestNeighbours_WhenThereAreSomeNearestNeighbours() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("B", "Description");
        Person userA = model.addPerson("User A", "Description");
        Person userB = model.addPerson("User B", "Description");

        // userA -> systemA -> system -> systemB -> userB
        userA.uses(softwareSystemA, "");
        softwareSystemA.uses(softwareSystem, "");
        softwareSystem.uses(softwareSystemB, "");
        softwareSystemB.delivers(userB, "");

        // userA -> systemA -> web application -> systemB -> userB
        // web application -> database
        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "", "");
        softwareSystemA.uses(webApplication, "");
        webApplication.uses(softwareSystemB, "");
        webApplication.uses(database, "");

        // userA -> systemA -> controller -> service -> repository -> database
        Component controller = webApplication.addComponent("Controller", "");
        Component service = webApplication.addComponent("Service", "");
        Component repository = webApplication.addComponent("Repository", "");
        softwareSystemA.uses(controller, "");
        controller.uses(service, "");
        service.uses(repository, "");
        repository.uses(database, "");

        // userA -> systemA -> controller -> service -> systemB -> userB
        service.uses(softwareSystemB, "");

        view.addNearestNeighbours(softwareSystem);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));

        view = new SystemContextView(softwareSystem, "context", "Description");
        view.addNearestNeighbours(softwareSystemA);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
    }

    @Test
    public void test_removeSoftwareSystem_ThrowsAnException_WhenPassedNull() {
        try {
            view.remove((SoftwareSystem)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_removeSoftwareSystem_DoesNothing_WhenTheSoftwareSystemIsNotInTheView() {
        SoftwareSystem anotherSoftwareSystem = model.addSoftwareSystem("Another software system", "");
        assertEquals(1, view.getElements().size());

        view.remove(anotherSoftwareSystem);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_removeSoftwareSystem_DoesNotRemoveTheSoftwareSystemInFocus() {
        try {
            view.remove(softwareSystem);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The element named 'The System' cannot be removed from this view.", iae.getMessage());
        }
    }

    @Test
    public void test_removeSoftwareSystem_RemovesTheSoftwareSystemAndRelationshipsFromTheView() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software system 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software system 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");
        softwareSystem2.uses(softwareSystem1, "uses");
        view = views.createSystemContextView(softwareSystem1, "key", "description");
        view.add(softwareSystem2);
        assertEquals(2, view.getElements().size());
        assertEquals(2, view.getRelationships().size());

        view.remove(softwareSystem2);
        assertEquals(1, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    public void test_removePerson_ThrowsAnException_WhenPassedNull() {
        try {
            view.remove((Person)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_removePerson_DoesNothing_WhenThePersonIsNotInTheView() {
        Person person = model.addPerson("Person", "");
        assertEquals(1, view.getElements().size());

        view.remove(person);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_removePerson_RemovesThePersonAndRelationshipsFromTheView() {
        Person person = model.addPerson("Person", "");
        person.uses(softwareSystem, "uses");
        softwareSystem.delivers(person, "delivers something to");
        view = views.createSystemContextView(softwareSystem, "key", "description");
        view.add(person);
        assertEquals(2, view.getElements().size());
        assertEquals(2, view.getRelationships().size());

        view.remove(person);
        assertEquals(1, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    public void test_addSoftwareSystemWithoutRelationships_DoesNotAddRelationships() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software system 1", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software system 2", "");
        softwareSystem1.uses(softwareSystem2, "uses");
        view = views.createSystemContextView(softwareSystem1, "key", "description");
        view.add(softwareSystem2, false);

        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    public void test_addPersonWithoutRelationships_DoesNotAddRelationships() {
        Person user = model.addPerson("User", "");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software system 2", "");
        user.uses(softwareSystem, "uses");
        view = views.createSystemContextView(softwareSystem, "key", "description");
        view.add(user, false);

        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    public void test_isEnterpriseBoundaryVisible() {
        assertTrue(view.isEnterpriseBoundaryVisible()); // default is true

        view.setEnterpriseBoundaryVisible(false);
        assertFalse(view.isEnterpriseBoundaryVisible());
    }

    @Test
    public void test_addDefaultElements() {
        CustomElement element = model.addCustomElement("Custom");
        Person user1 = model.addPerson("User 1");
        Person user2 = model.addPerson("User 2");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");

        user1.uses(softwareSystem1, "");
        softwareSystem1.uses(softwareSystem2, "");
        user2.uses(softwareSystem2, "");

        view = views.createSystemContextView(softwareSystem1, "key", "description");
        view.addDefaultElements();

        assertEquals(3, view.getElements().size());
        assertFalse(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user1)));
        assertFalse(view.getElements().contains(new ElementView(user2)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));

        element.uses(softwareSystem1, "Uses");
        view.addDefaultElements();

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user1)));
        assertFalse(view.getElements().contains(new ElementView(user2)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));
    }

}