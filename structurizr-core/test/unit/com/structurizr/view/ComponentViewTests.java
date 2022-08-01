package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentViewTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private Container webApplication;
    private ComponentView view;

    @BeforeEach
    public void setUp() {
        softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        webApplication = softwareSystem.addContainer("Web Application", "Does something", "Apache Tomcat");
        view = new ComponentView(webApplication, "Key", "Some description");
    }

    @Test
    void test_construction() {
        assertEquals("The System - Web Application - Components", view.getName());
        assertEquals("Some description", view.getDescription());
        assertEquals(0, view.getElements().size());
        assertSame(softwareSystem, view.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), view.getSoftwareSystemId());
        assertEquals(webApplication.getId(), view.getContainerId());
        assertSame(model, view.getModel());
    }

    @Test
    void test_addAllSoftwareSystems_DoesNothing_WhenThereAreNoOtherSoftwareSystems() {
        assertEquals(0, view.getElements().size());
        view.addAllSoftwareSystems();
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addAllSoftwareSystems_AddsAllSoftwareSystems_WhenThereAreSomeSoftwareSystemsInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");

        view.addAllSoftwareSystems();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addAllPeople_DoesNothing_WhenThereAreNoPeople() {
        assertEquals(0, view.getElements().size());
        view.addAllPeople();
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addAllPeople_AddsAllPeople_WhenThereAreSomePeopleInTheModel() {
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");

        view.addAllPeople();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
    }

    @Test
    void test_addAllElements_DoesNothing_WhenThereAreNoSoftwareSystemsOrPeople() {
        assertEquals(0, view.getElements().size());
        view.addAllElements();
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addAllElements_AddsAllSoftwareSystemsAndPeopleAndContainersAndComponents_WhenThereAreSomeSoftwareSystemsAndPeopleAndContainersAndComponentsInTheModel() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem(Location.External, "System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem(Location.External, "System B", "Description");
        Person userA = model.addPerson(Location.External, "User A", "Description");
        Person userB = model.addPerson(Location.External, "User B", "Description");
        Container database = softwareSystem.addContainer("Database", "Does something", "MySQL");
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        Component componentB = webApplication.addComponent("Component B", "Does something", "Java");

        view.addAllElements();

        assertEquals(7, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(userB)));
        assertTrue(view.getElements().contains(new ElementView(database)));
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(componentB)));
    }

    @Test
    void test_addAllContainers_DoesNothing_WhenThereAreNoContainers() {
        assertEquals(0, view.getElements().size());
        view.addAllContainers();
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addAllContainers_AddsAllContainers_WhenThereAreSomeContainers() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        Container fileSystem = softwareSystem.addContainer("File System", "Stores something else", "");

        view.addAllContainers();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
        assertTrue(view.getElements().contains(new ElementView(fileSystem)));
    }

    @Test
    void test_addAllComponents_DoesNothing_WhenThereAreNoComponents() {
        assertEquals(0, view.getElements().size());
        view.addAllComponents();
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addAllComponents_AddsAllComponents_WhenThereAreSomeComponents() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        Component componentB = webApplication.addComponent("Component B", "Does something", "Java");

        view.addAllComponents();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(componentB)));
    }

    @Test
    void test_add_ThrowsAnException_WhenANullContainerIsSpecified() {
        assertEquals(0, view.getElements().size());

        try {
            view.add((Container) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_add_AddsTheContainer_WhenTheContainerIsNoInTheViewAlready() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");

        assertEquals(0, view.getElements().size());
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    void test_add_DoesNothing_WhenTheSpecifiedContainerIsAlreadyInTheView() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));

        view.add(database);
        assertEquals(1, view.getElements().size());
    }

    @Test
    void test_remove_ThrowsAndException_WhenANullContainerIsPassed() {
        try {
            view.remove((Container) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_remove_RemovesTheContainer_WhenTheContainerIsInTheView() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));

        view.remove(database);
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_remove_DoesNothing_WhenTheContainerIsNotInTheView() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        Container fileSystem = softwareSystem.addContainer("File System", "Stores something else", "");

        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));

        view.remove(fileSystem);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    void test_add_DoesNothing_WhenANullComponentIsSpecified() {
        assertEquals(0, view.getElements().size());
        view.add((Component) null);
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_add_AddsTheComponent_WhenTheComponentIsNotInTheViewAlready() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");

        assertEquals(0, view.getElements().size());
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
    }

    @Test
    void test_add_DoesNothing_WhenTheSpecifiedComponentIsAlreadyInTheView() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));

        view.add(componentA);
        assertEquals(1, view.getElements().size());
    }

    @Test
    void test_add_ThrowsAnException_WhenTheSpecifiedComponentIsInADifferentContainer() {
        try {
            SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "Description");

            final Container containerA1 = softwareSystemA.addContainer("Container A1", "Description", "Tec");

            final Container containerA2 = softwareSystemA.addContainer("Container A2", "Description", "Tec");
            final Component componentA2_1 = containerA2.addComponent("Component A2-1", "Description");

            view = new ComponentView(containerA1, "components", "Description");
            view.add(componentA2_1);
        } catch (Exception e) {
            assertEquals("Only components belonging to Container A1 can be added to this view.", e.getMessage());
        }
    }

    @Test
    void test_add_ThrowsAnException_WhenTheContainerOfTheViewIsAdded() {
        try {
            view.add(webApplication);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("The container in scope cannot be added to a component view.", e.getMessage());
        }
    }

    @Test
    void test_add_DoesNothing_WhenTheContainerOfTheViewIsAddedViaDependency() {
        final SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "Some other system", "external system that uses our web application");

        final Relationship relationshipFromExternalSystem = softwareSystem.uses(webApplication, "");

        assertEquals(0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count(), "the container itself is not added to the view");
        view.add(relationshipFromExternalSystem);
        assertEquals(0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count(), "the container itself is not added to the view");
    }

    @Test
    void test_remove_DoesNothing_WhenANullComponentIsPassed() {
        try {
            view.remove((Component) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("An element must be specified.", iae.getMessage());
        }
    }

    @Test
    void test_remove_RemovesTheComponent_WhenTheComponentIsInTheView() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));

        view.remove(componentA);
        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_remove_RemovesTheComponentAndRelationships_WhenTheComponentIsInTheViewAndHasArelationshipToAnotherElement() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        Component componentB = webApplication.addComponent("Component B", "Does something", "Java");
        componentA.uses(componentB, "uses");

        view.add(componentA);
        view.add(componentB);
        assertEquals(2, view.getElements().size());
        assertEquals(1, view.getRelationships().size());

        view.remove(componentB);
        assertEquals(1, view.getElements().size());
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_remove_DoesNothing_WhenTheComponentIsNotInTheView() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        Component componentB = webApplication.addComponent("Component B", "Does something", "Java");

        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));

        view.remove(componentB);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
    }

    @Test
    void test_addNearestNeightbours_DoesNothing_WhenANullElementIsSpecified() {
        view.addNearestNeighbours(null);

        assertEquals(0, view.getElements().size());
    }

    @Test
    void test_addNearestNeighbours_DoesNothing_WhenThereAreNoNeighbours() {
        Component component = webApplication.addComponent("Component", "", "");
        view.add(component);
        assertEquals(1, view.getElements().size());

        view.addNearestNeighbours(component);
        assertEquals(1, view.getElements().size());
    }

    @Test
    void test_addNearestNeighbours_AddsNearestNeighbours_WhenThereAreSomeNearestNeighbours() {
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

        view = new ComponentView(webApplication, "components", "Description");
        view.addNearestNeighbours(softwareSystemA);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(controller)));

        view = new ComponentView(webApplication, "components", "Description");
        view.addNearestNeighbours(controller);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(controller)));
        assertTrue(view.getElements().contains(new ElementView(service)));

        view = new ComponentView(webApplication, "components", "Description");
        view.addNearestNeighbours(service);

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(controller)));
        assertTrue(view.getElements().contains(new ElementView(service)));
        assertTrue(view.getElements().contains(new ElementView(repository)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addExternalDependencies_AddsOrphanedElements_WhenThereAreNoDirectRelationshipsWithAComponent() {
        SoftwareSystem source = model.addSoftwareSystem("Source", "");
        SoftwareSystem destination = model.addSoftwareSystem("Destination", "");

        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa = aa.addComponent("AAA", "", "");

        source.uses(aa, "");
        aa.uses(destination, "");

        view = new ComponentView(aa, "components", "Description");
        view.addAllComponents();
        view.addExternalDependencies();

        // check that the view includes the desired elements
        Set<Element> elementsInView = view.getElements().stream().map(ElementView::getElement).collect(Collectors.toSet());
        assertTrue(elementsInView.contains(aaa));

        // but there are no relationships (because component AAA isn't directly related to anything)
        assertEquals(0, view.getRelationships().size());
    }

    @Test
    void test_addExternalDependencies_AddsTheContainer_WhenAComponentHasARelationshipToAContainerInTheSameSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        Container containerB = softwareSystemA.addContainer("Container B", "", "");

        componentA.uses(containerB, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(containerB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheContainer_WhenAComponentHasARelationshipFromAContainerInTheSameSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        Container containerB = softwareSystemA.addContainer("Container B", "", "");

        containerB.uses(componentA, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(containerB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentContainer_WhenAComponentHasARelationshipToAComponentInADifferentContainerInTheSameSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        Container containerB = softwareSystemA.addContainer("Container B", "", "");
        Component componentB = containerB.addComponent("Component B", "", "");

        componentA.uses(componentB, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(containerB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentContainer_WhenAComponentHasARelationshipFromAComponentInADifferentContainerInTheSameSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        Container containerB = softwareSystemA.addContainer("Container B", "", "");
        Component componentB = containerB.addComponent("Component B", "", "");

        componentB.uses(componentA, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(containerB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentSoftwareSystem_WhenAComponentHasARelationshipToAContainerInAnotherSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B", "");
        Container containerB = softwareSystemB.addContainer("Container B", "", "");

        componentA.uses(containerB, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentSoftwareSystem_WhenAComponentHasARelationshipFromAContainerInAnotherSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B", "");
        Container containerB = softwareSystemB.addContainer("Container B", "", "");

        containerB.uses(componentA, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentSoftwareSystem_WhenAComponentHasARelationshipToAComponentInAnotherSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B", "");
        Container containerB = softwareSystemB.addContainer("Container B", "", "");
        Component componentB = containerB.addComponent("Component B", "", "");

        componentA.uses(componentB, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addExternalDependencies_AddsTheParentSoftwareSystem_WhenAComponentHasARelationshipFromAComponentInAnotherSoftwareSystem() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("Software System A", "");
        Container containerA = softwareSystemA.addContainer("Container A", "", "");
        Component componentA = containerA.addComponent("Component A", "", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("Software System B", "");
        Container containerB = softwareSystemB.addContainer("Container B", "", "");
        Component componentB = containerB.addComponent("Component B", "", "");

        componentB.uses(componentA, "uses");

        view = new ComponentView(containerA, "key", "description");
        view.addAllComponents();
        view.addExternalDependencies();
        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    void test_addDefaultElements() {
        model.setImpliedRelationshipsStrategy(new CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy());

        CustomElement element = model.addCustomElement("Custom");
        Person user1 = model.addPerson("User 1");
        Person user2 = model.addPerson("User 2");
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1", "", "");
        Component component1 = container1.addComponent("Component 1", "", "");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2", "", "");
        Component component2 = container2.addComponent("Component 2", "", "");

        user1.uses(component1, "Uses");
        user2.uses(component2, "Uses");
        component1.uses(component2, "Uses");

        view = new ComponentView(container1, "components", "Description");
        view.addDefaultElements();

        assertEquals(3, view.getElements().size());
        assertFalse(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user1)));
        assertFalse(view.getElements().contains(new ElementView(user2)));
        assertFalse(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));
        assertFalse(view.getElements().contains(new ElementView(container1)));
        assertFalse(view.getElements().contains(new ElementView(container2)));
        assertTrue(view.getElements().contains(new ElementView(component1)));
        assertFalse(view.getElements().contains(new ElementView(component2)));

        element.uses(component1, "Uses");
        view.addDefaultElements();

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(element)));
        assertTrue(view.getElements().contains(new ElementView(user1)));
        assertFalse(view.getElements().contains(new ElementView(user2)));
        assertFalse(view.getElements().contains(new ElementView(softwareSystem1)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem2)));
        assertFalse(view.getElements().contains(new ElementView(container1)));
        assertFalse(view.getElements().contains(new ElementView(container2)));
        assertTrue(view.getElements().contains(new ElementView(component1)));
        assertFalse(view.getElements().contains(new ElementView(component2)));
    }

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenTheSoftwareSystemIsTheScopeOfTheView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        view = new ComponentView(container, "components", "Description");
        try {
            view.add(softwareSystem);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("The software system in scope cannot be added to a component view.", e.getMessage());
        }
    }

    @Test
    void test_addContainer_ThrowsAnException_WhenTheContainerIsTheScopeOfTheView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");

        view = new ComponentView(container, "components", "Description");
        try {
            view.add(container);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("The container in scope cannot be added to a component view.", e.getMessage());
        }
    }

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenAChildContainerIsAlreadyAdded() {
        try {
            SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
            Container container1 = softwareSystem1.addContainer("Container 1");
            Component component1 = container1.addComponent("Component 1");

            SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
            Container container2 = softwareSystem2.addContainer("Container 2");
            Component component2 = container2.addComponent("Component 2");

            ComponentView view = views.createComponentView(container1, "key", "Description");

            view.add(container2);
            view.add(softwareSystem2);

            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A child of Software System 2 is already in this view.", e.getMessage());
        }
    }

    @Test
    void test_addSoftwareSystem_ThrowsAnException_WhenAChildComponentIsAlreadyAdded() {
        try {
            SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
            Container container1 = softwareSystem1.addContainer("Container 1");
            Component component1 = container1.addComponent("Component 1");

            SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
            Container container2 = softwareSystem2.addContainer("Container 2");
            Component component2 = container2.addComponent("Component 2");

            ComponentView view = views.createComponentView(container1, "key", "Description");

            view.add(component2);
            view.add(softwareSystem2);

            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A child of Software System 2 is already in this view.", e.getMessage());
        }
    }

    @Test
    void test_addContainer_ThrowsAnException_WhenAChildComponentIsAlreadyAdded() {
        try {
            SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
            Container container1 = softwareSystem1.addContainer("Container 1");
            Component component1 = container1.addComponent("Component 1");

            SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
            Container container2 = softwareSystem2.addContainer("Container 2");
            Component component2 = container2.addComponent("Component 2");

            ComponentView view = views.createComponentView(container1, "key", "Description");

            view.add(component2);
            view.add(container2);

            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A child of Container 2 is already in this view.", e.getMessage());
        }
    }

    @Test
    void test_addContainer_ThrowsAnException_WhenTheParentIsAlreadyAdded() {
        try {
            SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
            Container container1 = softwareSystem1.addContainer("Container 1");
            Component component1 = container1.addComponent("Component 1");

            SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
            Container container2 = softwareSystem2.addContainer("Container 2");
            Component component2 = container2.addComponent("Component 2");

            ComponentView view = views.createComponentView(container1, "key", "Description");

            view.add(softwareSystem2);
            view.add(container2);

            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A parent of Container 2 is already in this view.", e.getMessage());
        }
    }

    @Test
    void test_addComponent_ThrowsAnException_WhenTheParentIsAlreadyAdded() {
        try {
            SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1");
            Container container1 = softwareSystem1.addContainer("Container 1");
            Component component1 = container1.addComponent("Component 1");

            SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2");
            Container container2 = softwareSystem2.addContainer("Container 2");
            Component component2 = container2.addComponent("Component 2");

            ComponentView view = views.createComponentView(container1, "key", "Description");

            view.add(softwareSystem2);
            view.add(component2);

            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A parent of Component 2 is already in this view.", e.getMessage());
        }
    }

}