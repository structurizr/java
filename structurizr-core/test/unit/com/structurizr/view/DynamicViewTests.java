package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicViewTests extends AbstractWorkspaceTestBase {

    private Person person;
    private SoftwareSystem softwareSystemA;
    private Container containerA1;
    private Container containerA2;
    private Container containerA3;
    private Component componentA1;
    private Component componentA2;

    private SoftwareSystem softwareSystemB;
    private Container containerB1;
    private Component componentB1;

    private Relationship relationship;

    @Before
    public void setup() {
        person = model.addPerson("Person", "");
        softwareSystemA = model.addSoftwareSystem("Software System A", "");
        containerA1 = softwareSystemA.addContainer("Container A1", "", "");
        componentA1 = containerA1.addComponent("Component A1", "");
        containerA2 = softwareSystemA.addContainer("Container A2", "", "");
        componentA2 = containerA2.addComponent("Component A2", "");
        containerA3 = softwareSystemA.addContainer("Container A3", "", "");
        relationship = containerA1.uses(containerA2, "uses");

        softwareSystemB = model.addSoftwareSystem("Software System B", "");
        containerB1 = softwareSystemB.addContainer("Container B1", "", "");
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemButAContainerInAnotherSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(containerB1, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemButAComponentIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(componentA1, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemAndTheSameSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(softwareSystemA, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Software System A is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndTheSameContainerIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(containerA1, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Container A1 is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndTheParentSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(softwareSystemA, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Software System A is already the scope of this view and cannot be added to it.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndAContainerInAnotherSoftwareSystemIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(containerB1, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Only containers that reside inside Software System A can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsAContainerAndAComponentInAnotherContainerIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(containerA1, "key", "Description");
            dynamicView.add(componentA2, containerA2);
            fail();
        } catch (Exception e) {
            assertEquals("Only components that reside inside Container A1 can be added to this view.", e.getMessage());
        }
    }

    @Test
    public void test_add_AddsTheSourceAndDestinationElements_WhenARelationshipBetweenThemExists() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(containerA1, containerA2);
        assertEquals(2, dynamicView.getElements().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_add_ThrowsAnException_WhenThereIsNoRelationshipBetweenTheSourceAndDestinationElements() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(containerA1, containerA3);
    }

    @Test
    public void test_addRelationshipDirectly() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(relationship);
        assertEquals(2, dynamicView.getElements().size());
    }

    @Test
    public void test_normalSequence() {
        workspace = new Workspace("Name", "Description");
        model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "Description");
        Container container1 = softwareSystem.addContainer("Container 1", "Description", "Technology");
        Container container2 = softwareSystem.addContainer("Container 2", "Description", "Technology");
        Container container3 = softwareSystem.addContainer("Container 3", "Description", "Technology");

        container1.uses(container2, "Uses");
        container1.uses(container3, "Uses");

        DynamicView view = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");

        view.add(container1, container2);
        view.add(container1, container3);

        assertSame(container2, view.getRelationships().stream().filter(r -> r.getOrder().equals("1")).findFirst().get().getRelationship().getDestination());
        assertSame(container3, view.getRelationships().stream().filter(r -> r.getOrder().equals("2")).findFirst().get().getRelationship().getDestination());
    }

    @Test
    public void test_parallelSequence() {
        workspace = new Workspace("Name", "Description");
        model = workspace.getModel();
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        Person user = model.addPerson("User", "Description");
        Container microservice1 = softwareSystem.addContainer("Microservice 1", "", "");
        Container database1 = softwareSystem.addContainer("Database 1", "", "");
        Container microservice2 = softwareSystem.addContainer("Microservice 2", "", "");
        Container database2 = softwareSystem.addContainer("Database 2", "", "");
        Container microservice3 = softwareSystem.addContainer("Microservice 3", "", "");
        Container database3 = softwareSystem.addContainer("Database 3", "", "");
        Container messageBus = softwareSystem.addContainer("Message Bus", "", "");

        user.uses(microservice1, "Updates using");
        microservice1.delivers(user, "Sends updates to");

        microservice1.uses(database1, "Stores data in");
        microservice1.uses(messageBus, "Sends messages to");
        microservice1.uses(messageBus, "Sends messages to");

        messageBus.uses(microservice2, "Sends messages to");
        messageBus.uses(microservice3, "Sends messages to");

        microservice2.uses(database2, "Stores data in");
        microservice3.uses(database3, "Stores data in");

        DynamicView view = workspace.getViews().createDynamicView(softwareSystem, "key", "Description");

        view.add(user, "1", microservice1);
        view.add(microservice1, "2", database1);
        view.add(microservice1, "3", messageBus);

        view.startParallelSequence();
        view.add(messageBus, "4", microservice2);
        view.add(microservice2, "5", database2);
        view.endParallelSequence();

        view.startParallelSequence();
        view.add(messageBus, "4", microservice3);
        view.add(microservice3, "5", database3);
        view.endParallelSequence();

        view.add(microservice1, "5", database1);

        System.out.println(view.toString());

        assertEquals(1, view.getRelationships().stream().filter(r -> r.getOrder().equals("1")).count());
        assertEquals(1, view.getRelationships().stream().filter(r -> r.getOrder().equals("2")).count());
        assertEquals(1, view.getRelationships().stream().filter(r -> r.getOrder().equals("3")).count());
        assertEquals(3, view.getRelationships().stream().filter(r -> r.getOrder().equals("4")).count());
        assertEquals(2, view.getRelationships().stream().filter(r -> r.getOrder().equals("5")).count());
    }

    @Test
    public void test_dynamicViewWithSoftwareSystemAsDestination() {
        DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");

        containerA2.uses(softwareSystemB, "", "");

        dynamicView.add(containerA2, softwareSystemB);
    }

}
