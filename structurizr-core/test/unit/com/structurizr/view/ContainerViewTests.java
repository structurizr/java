package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContainerViewTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private ContainerView view;

    @Before
    public void setUp() {
        softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        view = new ContainerView(softwareSystem, "containers", "Description");
    }

    @Test
    public void test_construction() {
        assertEquals("The System - Containers", view.getName());
        assertEquals("Description", view.getDescription());
        assertEquals(0, view.getElements().size());
        assertSame(softwareSystem, view.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), view.getSoftwareSystemId());
        assertSame(model, view.getModel());
    }

    @Test
    public void test_addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystems() {
        assertEquals(0, view.getElements().size());
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
        assertEquals(0, view.getElements().size());
        view.addAllPeople();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllPeople_AddsAllPeople_WhenThereAreSomePeopleInTheModel() {
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");

        view.addAllPeople();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
    }

    @Test
    public void test_addAllElements_DoesNothing_WhenThereAreNoSoftwareSystemsOrPeople() {
        assertEquals(0, view.getElements().size());
        view.addAllElements();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllElements_AddsAllSoftwareSystemsAndPeopleAndContainers_WhenThereAreSomeSoftwareSystemsAndPeopleAndContainersInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");
        Container webApplication = softwareSystem.addContainer("Web Application", "Does something", "Apache Tomcat");
        Container database = softwareSystem.addContainer("Database", "Does something", "MySQL");

        view.addAllElements();

        assertEquals(6, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    public void test_addAllContainers_DoesNothing_WhenThereAreNoContainers() {
        assertEquals(0, view.getElements().size());
        view.addAllContainers();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllContainers_AddsAllContainers_WhenThereAreSomeContainers() {
        Container webApplication = softwareSystem.addContainer("Web Application", "Does something", "Apache Tomcat");
        Container database = softwareSystem.addContainer("Database", "Does something", "MySQL");

        view.addAllContainers();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    public void test_addNearestNeightbours_DoesNothing_WhenANullElementIsSpecified() {
        view.addNearestNeighbours(null);

        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addNearestNeighbours_DoesNothing_WhenThereAreNoNeighbours() {
        view.addNearestNeighbours(softwareSystem);

        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addNearestNeighbours_AddsNearestNeighbours_WhenThereAreSomeNearestNeighbours() {
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

        view.addNearestNeighbours(webApplication);

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(database)));

        view = new ContainerView(softwareSystem, "containers", "Description");
        view.addNearestNeighbours(softwareSystemA);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));

        view = new ContainerView(softwareSystem, "containers", "Description");
        view.addNearestNeighbours(webApplication);

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(database)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    public void test_remove_RemovesContainer() {
        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "", "");

        view.addAllContainers();
        assertEquals(2, view.getElements().size());

        view.remove(webApplication);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    public void test_remove_ElementsWithTag() {
        final String TAG = "myTag";
        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "", "");
        database.addTags(TAG);

        view.addAllContainers();
        assertEquals(2, view.getElements().size());

        view.removeElementsWithTag(TAG);
        assertEquals(1, view.getElements().size());
        assertEquals(webApplication, view.getElements().iterator().next().getElement());
    }

    @Test
    public void test_remove_RelationshipWithTag() {
        final String TAG = "myTag";
        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "", "");
        webApplication.uses(database, "").addTags(TAG);

        view.addAllContainers();
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        view.removeRelationshipsWithTag(TAG);
        assertEquals(2, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    public void test_addDependentSoftwareSystem() {
        assertEquals(0, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        view.addDependentSoftwareSystems();

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem(Location.External, "SoftwareSystem 2", "");

        view.addDependentSoftwareSystems();
        assertEquals(0, view.getElements().size());
        assertEquals(0, view.getRelationships().size());

        softwareSystem2.uses(softwareSystem, "");

        view.addDependentSoftwareSystems();
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_addDependentSoftwareSystem2() {
        Container container1a = softwareSystem.addContainer("Container 1A", "", "");

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem(Location.External, "SoftwareSystem 2", "");
        Container container2a = softwareSystem2.addContainer("Container 2-A", "", "");

        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());
        container2a.uses(container1a, "");

        view.addDependentSoftwareSystems();
        view.addAllContainers();

        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());
    }

    @Test
    public void test_addDefaultElements() {
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        Person user1 = model.addPerson("User 1");
        Person user2 = model.addPerson("User 2");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1", "", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2", "", "");

        user1.uses(container1, "Uses");
        user2.uses(container2, "Uses");
        container1.uses(container2, "Uses");

        view = new ContainerView(softwareSystem1, "containers", "Description");
        view.addDefaultElements();

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(user1)));
        assertFalse(view.getElements().contains(new ElementView(user2)));
        assertFalse(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));
        assertTrue(view.getElements().contains(new ElementView(container1)));
        assertFalse(view.getElements().contains(new ElementView(container2)));
    }

    @Test
    public void test_addSoftwareSystem_ThrowsAnException_WhenTheSoftwareSystemIsTheScopeOfTheView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");

        view = new ContainerView(softwareSystem, "containers", "Description");
        try {
            view.add(softwareSystem);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("The software system in scope cannot be added to a container view.", e.getMessage());
        }
    }

}