package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    public void test_add_ThrowsAnException_WhenPassedANullSourceElement() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            dynamicView.add(null, softwareSystemA);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A source element must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenPassedANullDestinationElement() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            dynamicView.add(person, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A destination element must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenADeploymentNodeIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node", "Description", "Technology");
            dynamicView.add(deploymentNode, softwareSystemA);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Only people, software systems, containers and components can be added to dynamic views.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenAContainerInstanceIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node", "Description", "Technology");
            ContainerInstance containerInstance = deploymentNode.add(containerA1);
            dynamicView.add(containerInstance, softwareSystemA);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Only people, software systems, containers and components can be added to dynamic views.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsNotSpecifiedButAContainerIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node", "Description", "Technology");
            dynamicView.add(containerA1, containerA1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Only people and software systems can be added to this dynamic view.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsNotSpecifiedButAComponentIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment node", "Description", "Technology");
            dynamicView.add(componentA1, componentA1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Only people and software systems can be added to this dynamic view.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenTheScopeOfTheDynamicViewIsASoftwareSystemButAComponentIsAdded() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
            dynamicView.add(componentA1, containerA1);
            fail();
        } catch (Exception e) {
            assertEquals("Components can't be added to a dynamic view when the scope is a software system.", e.getMessage());
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
    public void test_add_ThrowsAnException_WhenARelationshipBetweenTheSourceAndDestinationElementsDoesNotExist() {
        try {
            DynamicView dynamicView = workspace.getViews().createDynamicView("key", "Description");
            SoftwareSystem ss1 = workspace.getModel().addSoftwareSystem("Software System 1", "");
            SoftwareSystem ss2 = workspace.getModel().addSoftwareSystem("Software System 2", "");
            dynamicView.add(ss1, ss2);
            fail();
        } catch (Exception e) {
            assertEquals("A relationship between Software System 1 and Software System 2 does not exist in model.", e.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenARelationshipBetweenTheSourceAndDestinationElementsWithTheSpecifiedTechnologyDoesNotExist() {
        try {
            workspace = new Workspace("Name", "Description");
            model = workspace.getModel();

            SoftwareSystem ss1 = workspace.getModel().addSoftwareSystem("Software System 1", "");
            SoftwareSystem ss2 = workspace.getModel().addSoftwareSystem("Software System 2", "");
            ss1.uses(ss2, "Uses 1", "Tech 1");

            DynamicView view = workspace.getViews().createDynamicView("key", "Description");

            view.add(ss1, "Uses", "Tech 1", ss2);
            view.add(ss1, "Uses", "Tech 2", ss2);
            fail();
        } catch (Exception e) {
            assertEquals("A relationship between Software System 1 and Software System 2 with technology Tech 2 does not exist in model.", e.getMessage());
        }
    }

    @Test
    public void test_add_AddsTheSourceAndDestinationElements_WhenARelationshipBetweenThemExists() {
        final DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        dynamicView.add(containerA1, containerA2);
        assertEquals(2, dynamicView.getElements().size());
    }

    @Test
    public void test_add_AddsTheSourceAndDestinationElements_WhenARelationshipBetweenThemExistsAndTheDestinationIsAnExternalSoftwareSystem() {
        DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        containerA2.uses(softwareSystemB, "", "");
        dynamicView.add(containerA2, softwareSystemB);
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
    public void test_normalSequence_WhenThereAreMultipleTechnologies() {
        workspace = new Workspace("Name", "Description");
        model = workspace.getModel();

        SoftwareSystem ss1 = workspace.getModel().addSoftwareSystem("Software System 1", "");
        SoftwareSystem ss2 = workspace.getModel().addSoftwareSystem("Software System 2", "");

        Relationship r1 = ss1.uses(ss2, "Uses 1", "Tech 1");
        Relationship r2 = ss1.uses(ss2, "Uses 2", "Tech 2");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");

        RelationshipView rv1 = view.add(ss1, "Uses", "Tech 1", ss2);
        RelationshipView rv2 = view.add(ss1, "Uses", "Tech 2", ss2);

        assertSame(r1, rv1.getRelationship());
        assertSame(r2, rv2.getRelationship());
    }

    @Test
    public void test_parallelSequence() {
        workspace = new Workspace("Name", "Description");
        model = workspace.getModel();
        SoftwareSystem softwareSystemA = model.addSoftwareSystem("A", "");
        SoftwareSystem softwareSystemB = model.addSoftwareSystem("B", "");
        SoftwareSystem softwareSystemC1 = model.addSoftwareSystem("C1", "");
        SoftwareSystem softwareSystemC2 = model.addSoftwareSystem("C2", "");
        SoftwareSystem softwareSystemD = model.addSoftwareSystem("D", "");
        SoftwareSystem softwareSystemE = model.addSoftwareSystem("E", "");

        // A -> B -> C1 -> D -> E
        // A -> B -> C2 -> D -> E
        softwareSystemA.uses(softwareSystemB, "uses");
        softwareSystemB.uses(softwareSystemC1, "uses");
        softwareSystemC1.uses(softwareSystemD, "uses");
        softwareSystemB.uses(softwareSystemC2, "uses");
        softwareSystemC2.uses(softwareSystemD, "uses");
        softwareSystemD.uses(softwareSystemE, "uses");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");

        view.add(softwareSystemA, softwareSystemB);
        view.startParallelSequence();
        view.add(softwareSystemB, softwareSystemC1);
        view.add(softwareSystemC1, softwareSystemD);
        view.endParallelSequence();
        view.startParallelSequence();
        view.add(softwareSystemB, softwareSystemC2);
        view.add(softwareSystemC2, softwareSystemD);
        view.endParallelSequence(true);
        view.add(softwareSystemD, softwareSystemE);

        assertEquals(1, view.getRelationships().stream().filter(r -> r.getOrder().equals("1")).count());
        assertEquals(2, view.getRelationships().stream().filter(r -> r.getOrder().equals("2")).count());
        assertEquals(2, view.getRelationships().stream().filter(r -> r.getOrder().equals("3")).count());
        assertEquals(1, view.getRelationships().stream().filter(r -> r.getOrder().equals("4")).count());
    }

    @Test
    public void test_getRelationships_WhenTheOrderPropertyIsAnInteger() {
        containerA1.uses(containerA2, "uses");
        DynamicView view = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        for (int i = 0; i < 10; i++) {
            view.add(containerA1, containerA2);
        }

        List<RelationshipView> relationships = new LinkedList<>(view.getRelationships());
        assertEquals("1", relationships.get(0).getOrder());
        assertEquals("2", relationships.get(1).getOrder());
        assertEquals("3", relationships.get(2).getOrder());
        assertEquals("4", relationships.get(3).getOrder());
        assertEquals("5", relationships.get(4).getOrder());
        assertEquals("6", relationships.get(5).getOrder());
        assertEquals("7", relationships.get(6).getOrder());
        assertEquals("8", relationships.get(7).getOrder());
        assertEquals("9", relationships.get(8).getOrder());
        assertEquals("10", relationships.get(9).getOrder());
    }

    @Test
    public void test_getRelationships_WhenTheOrderPropertyIsADecimal() {
        containerA1.uses(containerA2, "uses");
        DynamicView view = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        for (int i = 0; i < 10; i++) {
            RelationshipView relationshipView = view.add(containerA1, containerA2);
            relationshipView.setOrder("1." + i);
        }

        List<RelationshipView> relationships = new LinkedList<>(view.getRelationships());
        assertEquals("1.0", relationships.get(0).getOrder());
        assertEquals("1.1", relationships.get(1).getOrder());
        assertEquals("1.2", relationships.get(2).getOrder());
        assertEquals("1.3", relationships.get(3).getOrder());
        assertEquals("1.4", relationships.get(4).getOrder());
        assertEquals("1.5", relationships.get(5).getOrder());
        assertEquals("1.6", relationships.get(6).getOrder());
        assertEquals("1.7", relationships.get(7).getOrder());
        assertEquals("1.8", relationships.get(8).getOrder());
        assertEquals("1.9", relationships.get(9).getOrder());
    }

    @Test
    public void test_getRelationships_WhenTheOrderPropertyIsAString() {
        String characters = "abcdefghij";
        containerA1.uses(containerA2, "uses");
        DynamicView view = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        for (int i = 0; i < 10; i++) {
            RelationshipView relationshipView = view.add(containerA1, containerA2);
            relationshipView.setOrder("1" + characters.charAt(i));
        }

        List<RelationshipView> relationships = new LinkedList<>(view.getRelationships());
        assertEquals("1a", relationships.get(0).getOrder());
        assertEquals("1b", relationships.get(1).getOrder());
        assertEquals("1c", relationships.get(2).getOrder());
        assertEquals("1d", relationships.get(3).getOrder());
        assertEquals("1e", relationships.get(4).getOrder());
        assertEquals("1f", relationships.get(5).getOrder());
        assertEquals("1g", relationships.get(6).getOrder());
        assertEquals("1h", relationships.get(7).getOrder());
        assertEquals("1i", relationships.get(8).getOrder());
        assertEquals("1j", relationships.get(9).getOrder());
    }

    @Test
    public void test_response() {
        workspace = new Workspace("Name", "Description");
        model = workspace.getModel();

        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "Description");
        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "Description");
        Relationship relationship = softwareSystem1.uses(softwareSystem2, "Uses");
        
        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(softwareSystem1, "Asks for X", softwareSystem2);
        view.add(softwareSystem2, "Returns X", softwareSystem1); // this relationship doesn't exist, so is assumed to be a response

        List<RelationshipView> list = new ArrayList<>(view.getRelationships());
        RelationshipView relationshipView = list.get(0);
        assertSame(relationship, relationshipView.getRelationship());
        assertEquals("Asks for X", relationshipView.getDescription());
        assertFalse(relationshipView.isResponse());

        relationshipView = list.get(1);
        assertSame(relationship, relationshipView.getRelationship());
        assertEquals("Returns X", relationshipView.getDescription());
        assertTrue(relationshipView.isResponse());
    }

}