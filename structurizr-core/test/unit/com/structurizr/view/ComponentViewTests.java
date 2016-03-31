package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class ComponentViewTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem;
    private Container webApplication;
    private ComponentView view;

    @Before
    public void setUp() {
        softwareSystem = model.addSoftwareSystem(Location.Internal, "The System", "Description");
        webApplication = softwareSystem.addContainer("Web Application", "Does something", "Apache Tomcat");
        view = new ComponentView(webApplication, "Some description");
    }

    @Test
    public void test_construction() {
        assertEquals("The System - Web Application - Components", view.getName());
        assertEquals("Some description", view.getDescription());
        assertEquals(0, view.getElements().size());
        assertSame(softwareSystem, view.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), view.getSoftwareSystemId());
        assertEquals(webApplication.getId(), view.getContainerId());
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
    public void test_addAllElements_AddsAllSoftwareSystemsAndPeopleAndContainersAndComponents_WhenThereAreSomeSoftwareSystemsAndPeopleAndContainersAndComponentsInTheModel() {
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
    public void test_addAllContainers_DoesNothing_WhenThereAreNoContainers() {
        assertEquals(0, view.getElements().size());
        view.addAllContainers();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllContainers_AddsAllContainers_WhenThereAreSomeContainers() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        Container fileSystem = softwareSystem.addContainer("File System", "Stores something else", "");

        view.addAllContainers();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
        assertTrue(view.getElements().contains(new ElementView(fileSystem)));
    }

    @Test
    public void test_addAllComponents_DoesNothing_WhenThereAreNoComponents() {
        assertEquals(0, view.getElements().size());
        view.addAllComponents();
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addAllComponents_AddsAllComponents_WhenThereAreSomeComponents() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        Component componentB = webApplication.addComponent("Component B", "Does something", "Java");

        view.addAllComponents();

        assertEquals(2, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
        assertTrue(view.getElements().contains(new ElementView(componentB)));
    }

    @Test
    public void test_add_DoesNothing_WhenANullContainerIsSpecified() {
        assertEquals(0, view.getElements().size());
        view.add((Container) null);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_add_AddsTheContainer_WhenTheContainerIsNoInTheViewAlready() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");

        assertEquals(0, view.getElements().size());
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));
    }

    @Test
    public void test_add_DoesNothing_WhenTheSpecifiedContainerIsAlreadyInTheView() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));

        view.add(database);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_remove_DoesNothing_WhenANullContainerIsPassed() {
        assertEquals(0, view.getElements().size());
        view.remove((Container) null);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_remove_RemovesTheContainer_WhenTheContainerIsInTheView() {
        Container database = softwareSystem.addContainer("Database", "Stores something", "MySQL");
        view.add(database);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(database)));

        view.remove(database);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_remove_DoesNothing_WhenTheContainerIsNotInTheView() {
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
    public void test_add_DoesNothing_WhenANullComponentIsSpecified() {
        assertEquals(0, view.getElements().size());
        view.add((Component) null);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_add_AddsTheComponent_WhenTheComponentIsNotInTheViewAlready() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");

        assertEquals(0, view.getElements().size());
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));
    }

    @Test
    public void test_add_DoesNothing_WhenTheSpecifiedComponentIsAlreadyInTheView() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));

        view.add(componentA);
        assertEquals(1, view.getElements().size());
    }

    @Test
    public void test_add_DoesNothing_WhenTheContainerOfTheViewIsAdded() {
        assertEquals("the container itself is not added to the view", 0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count());
        view.add(webApplication);
        assertEquals("the container itself is not added to the view", 0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count());
    }

    @Test
    public void test_add_DoesNothing_WhenTheContainerOfTheViewIsAddedViaDependency() {
        final SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.External, "Some other system", "external system that uses our web application");

        final Relationship relationshipFromExternalSystem = softwareSystem.uses(webApplication, "");

        assertEquals("the container itself is not added to the view", 0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count());
        view.add(relationshipFromExternalSystem);
        assertEquals("the container itself is not added to the view", 0, view.getElements().stream().map(e -> e.getElement()).filter(e -> e.equals(webApplication)).count());
    }

    @Test
    public void test_remove_DoesNothing_WhenANullComponentIsPassed() {
        assertEquals(0, view.getElements().size());
        view.remove((Component) null);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_remove_RemovesTheComponent_WhenTheComponentIsInTheView() {
        Component componentA = webApplication.addComponent("Component A", "Does something", "Java");
        view.add(componentA);
        assertEquals(1, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(componentA)));

        view.remove(componentA);
        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_remove_RemovesTheComponentAndRelationships_WhenTheComponentIsInTheViewAndHasArelationshipToAnotherElement() {
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
    public void test_remove_DoesNothing_WhenTheComponentIsNotInTheView() {
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
    public void test_addNearestNeightbours_DoesNothing_WhenANullElementIsSpecified() {
        view.addNearestNeighbours(null);

        assertEquals(0, view.getElements().size());
    }

    @Test
    public void test_addNearestNeighbours_DoesNothing_WhenThereAreNoNeighbours() {
        view.addNearestNeighbours(softwareSystem);

        assertEquals(1, view.getElements().size());
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

        view = new ComponentView(webApplication, "");
        view.addNearestNeighbours(softwareSystemA);

        assertEquals(5, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(userA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystem)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(controller)));

        view = new ComponentView(webApplication, "");
        view.addNearestNeighbours(webApplication);

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(webApplication)));
        assertTrue(view.getElements().contains(new ElementView(database)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));

        view = new ComponentView(webApplication, "");
        view.addNearestNeighbours(controller);

        assertEquals(3, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(softwareSystemA)));
        assertTrue(view.getElements().contains(new ElementView(controller)));
        assertTrue(view.getElements().contains(new ElementView(service)));


        view = new ComponentView(webApplication, "");
        view.addNearestNeighbours(service);

        assertEquals(4, view.getElements().size());
        assertTrue(view.getElements().contains(new ElementView(controller)));
        assertTrue(view.getElements().contains(new ElementView(service)));
        assertTrue(view.getElements().contains(new ElementView(repository)));
        assertTrue(view.getElements().contains(new ElementView(softwareSystemB)));
    }

    @Test
    public void test_AddAllComponentsAndDirectDependencies() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "Description");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("System B", "Description");
        Person userA = model.addPerson("User A", "Description");

        Set<Element> expectedElementsInView = new HashSet<>();
        Set<Relationship> expectedRelationshipsInView = new HashSet<>();

        final Container containerA1 = softwareSystemA.addContainer("Container A1", "Description", "Tec");
        final Component componentA1_1 = containerA1.addComponent("Component A1-1", "Description");
        final Component componentA1_2 = containerA1.addComponent("Component A1-2", "Description");
        final Component componentA1_3 = containerA1.addComponent("Component A1-3", "Description");

        expectedElementsInView.add(componentA1_1);
        expectedElementsInView.add(componentA1_2);
        expectedElementsInView.add(componentA1_3);

        expectedRelationshipsInView.add(componentA1_1.uses(componentA1_2, ""));
        expectedRelationshipsInView.add(componentA1_1.uses(componentA1_3, ""));

        final Container containerA2 = softwareSystemA.addContainer("Container A2", "Description", "Tec");
        final Component componentA2_1 = containerA2.addComponent("Component A2-1", "Description");

        expectedRelationshipsInView.add(componentA1_1.uses(containerA2, ""));
        expectedElementsInView.add(containerA2);

        componentA2_1.uses(componentA1_1, ""); // this relationship must not be part of the diagram as componentA2_1 is from another container

        final Container containerA3 = softwareSystemA.addContainer("Container A3", "Description", "Tec");
        containerA2.uses(containerA3, ""); // this relationship must not make it into the view as it is outside of our container

        final Container containerA4 = softwareSystemA.addContainer("Container A4", "Description", "Tec");
        final Component componentA4_1 = containerA4.addComponent("Component A3-1", "Description");
        componentA4_1.uses(componentA1_1, ""); // this relationship must not be part of the diagram as componentA2_1 is from another container
        expectedElementsInView.add(containerA4);


        expectedRelationshipsInView.add(userA.uses(componentA1_1, ""));
        expectedElementsInView.add(userA);

        softwareSystemA.uses(softwareSystemB, "");// this relationship must not make it into the view as it is outside of our container

        view = new ComponentView(containerA1, "");
        view.addAllComponents();
        view.addDirectDependencies();

        assertThat(view.getElements()).isEqualTo(expectedElementsInView.stream().map(e -> new ElementView(e)).collect(Collectors.toSet()));
        assertThat(view.getRelationships()).isEqualTo(expectedRelationshipsInView.stream().map(e -> new RelationshipView(e)).collect(Collectors.toSet()));
    }

    /**
     * When someone tries to add a component of another container to a container view, then this must not be added
     */
    @Test
    public void test_AddComponentFromOtherContainer() {
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("System A", "Description");

        final Container containerA1 = softwareSystemA.addContainer("Container A1", "Description", "Tec");
        final Component componentA1_1 = containerA1.addComponent("Component A1-1", "Description");


        final Container containerA2 = softwareSystemA.addContainer("Container A2", "Description", "Tec");
        final Component componentA2_1 = containerA2.addComponent("Component A2-1", "Description");

        view = new ComponentView(containerA1, "");
        view.addAllComponents();

        assertThat(view.getElements()).contains(new ElementView(componentA1_1));

        // manually add another container to the view
        view.add(containerA2);
        // container should be added to the view
        assertThat(view.getElements()).contains(new ElementView(containerA2));

        // now manually add a component from another container to the view
        view.add(componentA2_1);
        // component must not be added to the view
        assertThat(view.getElements()).doesNotContain(new ElementView(componentA2_1));
    }

    @Test
    public void test_addDirectDependencies_AddsElementsWithoutRelationships_WhenThereAreNoRelationshipsWithTheComponents() {
        SoftwareSystem source = model.addSoftwareSystem("Source", "");
        SoftwareSystem destination = model.addSoftwareSystem("Destination", "");

        SoftwareSystem a = model.addSoftwareSystem("A", "");
        Container aa = a.addContainer("AA", "", "");
        Component aaa1 = aa.addComponent("AAA1", "", "");

        source.uses(aa, "");
        aa.uses(destination, "");

        view = new ComponentView(aa, "");
        view.addAllComponents();
        view.addDirectDependencies();

        // check that the view includes the desired elements
        Set<Element> elementsInView = view.getElements().stream().map(ElementView::getElement).collect(Collectors.toSet());
        assertTrue(elementsInView.contains(aaa1));

        assertEquals(0, view.getRelationships().size());
    }

}