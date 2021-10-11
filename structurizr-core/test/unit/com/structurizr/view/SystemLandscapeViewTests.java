package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SystemLandscapeViewTests extends AbstractWorkspaceTestBase {

    private SystemLandscapeView view;

    @Before
    public void setUp() {
        view = new SystemLandscapeView(model, "context", "Description");
    }

    @Test
    public void test_construction() {
        assertEquals("System Landscape", view.getName());
        assertEquals(0, view.getElements().size());
        assertSame(model, view.getModel());
    }

    @Test
    public void test_getName_WhenNoEnterpriseIsSpecified() {
        assertEquals("System Landscape", view.getName());
    }

    @Test
    public void test_getName_WhenAnEnterpriseIsSpecified() {
        model.setEnterprise(new Enterprise("Widgets Limited"));
        assertEquals("System Landscape for Widgets Limited", view.getName());
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

    @Test
    public void test_isEnterpriseBoundaryVisible() {
        assertTrue(view.isEnterpriseBoundaryVisible()); // default is true

        view.setEnterpriseBoundaryVisible(false);
        assertFalse(view.isEnterpriseBoundaryVisible());
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
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
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
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
        view.addNearestNeighbours(softwareSystem);

        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addNearestNeighbours_AddsNearestNeighbours_WhenThereAreSomeNearestNeighbours() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("The System", "Description");
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("System B", "Description");
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

        view = views.createSystemLandscapeView("context", "Description");
        view.addNearestNeighbours(softwareSystemA);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
    }

    @Test
    public void test_addDefaultElements() {
        CustomElement element = model.addCustomElement("Custom");
        Person user = model.addPerson("User");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");

        view.addDefaultElements();

        assertEquals(3, view.getElements().size());
        assertFalse(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));

        element.uses(softwareSystem1, "Uses");
        view.addDefaultElements();

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));
    }

}