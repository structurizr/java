package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.Workspace;
import com.structurizr.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeploymentViewTests extends AbstractWorkspaceTestBase {

    private DeploymentView deploymentView;

    @Before
    public void setup() {
    }

    @Test
    public void test_getName_WithNoSoftwareSystemAndNoEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        assertEquals("Deployment", deploymentView.getName());
    }

    @Test
    public void test_getName_WithNoSoftwareSystemAndAnEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Deployment - Live", deploymentView.getName());
    }

    @Test
    public void test_getName_WithASoftwareSystemAndNoEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        assertEquals("Software System - Deployment", deploymentView.getName());
    }

    @Test
    public void test_getName_WithASoftwareSystemAndAnEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Software System - Deployment - Live", deploymentView.getName());
    }

    @Test
    public void test_addDeploymentNode_ThrowsAnException_WhenPassedNull() {
        try {
            deploymentView = views.createDeploymentView("key", "Description");
            deploymentView.add((DeploymentNode)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment node must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addRelationship_ThrowsAnException_WhenPassedNull() {
        try {
            deploymentView = views.createDeploymentView("key", "Description");
            deploymentView.add((Relationship)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addAllDeploymentNodes_DoesNothing_WhenThereAreNoTopLevelDeploymentNodes() {
        deploymentView = views.createDeploymentView("deployment", "Description");

        deploymentView.addAllDeploymentNodes();
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    public void test_addAllDeploymentNodes_DoesNothing_WhenThereAreTopLevelDeploymentNodesButNoContainerInstances() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        model.addDeploymentNode("Deployment Node", "Description", "Technology");

        deploymentView.addAllDeploymentNodes();
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    public void test_addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstances_WhenThereAreTopLevelDeploymentNodesWithContainerInstances() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNode.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(2, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNode)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance)));
    }

    @Test
    public void test_addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstances_WhenThereAreChildDeploymentNodesWithContainerInstances() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance)));
    }

    @Test
    public void test_addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstancesOnlyForTheSoftwareSystemInScope() {
        SoftwareSystem softwareSystem1 = model.addSoftwareSystem("Software System 1", "");
        Container container1 = softwareSystem1.addContainer("Container 1", "Description", "Technology");
        DeploymentNode deploymentNode1 = model.addDeploymentNode("Deployment Node 1", "Description", "Technology");
        ContainerInstance containerInstance1 = deploymentNode1.add(container1);

        SoftwareSystem softwareSystem2 = model.addSoftwareSystem("Software System 2", "");
        Container container2 = softwareSystem2.addContainer("Container 2", "Description", "Technology");
        DeploymentNode deploymentNode2 = model.addDeploymentNode("Deployment Node 2", "Description", "Technology");
        ContainerInstance containerInstance2 = deploymentNode2.add(container2);

        // two containers from different software systems on the same deployment node
        deploymentNode1.add(container2);

        deploymentView = views.createDeploymentView(softwareSystem1, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();

        assertEquals(2, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNode1)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance1)));
    }

    @Test
    public void test_addDeploymentNode_AddsTheParentToo() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.add(deploymentNodeChild);
        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance)));
    }

}