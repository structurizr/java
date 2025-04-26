package com.structurizr.view;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeploymentViewTests extends AbstractWorkspaceTestBase {

    private DeploymentView deploymentView;

    @BeforeEach
    public void setup() {
    }

    @Test
    void getName_WithNoSoftwareSystemAndNoEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        assertEquals("Deployment - Default", deploymentView.getName());
    }

    @Test
    void getName_WithNoSoftwareSystemAndAnEnvironment() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Deployment - Live", deploymentView.getName());
    }

    @Test
    void getName_WithASoftwareSystemAndNoEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        assertEquals("Software System - Deployment - Default", deploymentView.getName());
    }

    @Test
    void getName_WithASoftwareSystemAndAnEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.setEnvironment("Live");
        assertEquals("Software System - Deployment - Live", deploymentView.getName());
    }

    @Test
    void addDeploymentNode_ThrowsAnException_WhenPassedNull() {
        try {
            deploymentView = views.createDeploymentView("key", "Description");
            deploymentView.add((DeploymentNode) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A deployment node must be specified.", iae.getMessage());
        }
    }

    @Test
    void addRelationship_ThrowsAnException_WhenPassedNull() {
        try {
            deploymentView = views.createDeploymentView("key", "Description");
            deploymentView.add((Relationship) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A relationship must be specified.", iae.getMessage());
        }
    }

    @Test
    void addAllDeploymentNodes_DoesNothing_WhenThereAreNoTopLevelDeploymentNodes() {
        deploymentView = views.createDeploymentView("deployment", "Description");

        deploymentView.addAllDeploymentNodes();
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    void addAllDeploymentNodes_DoesNothing_WhenThereAreTopLevelDeploymentNodesButNoContainerInstances() {
        deploymentView = views.createDeploymentView("deployment", "Description");
        model.addDeploymentNode("Deployment Node", "Description", "Technology");

        deploymentView.addAllDeploymentNodes();
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    void addAllDeploymentNodes_DoesNothing_WhenThereNoDeploymentNodesForTheDeploymentEnvironment() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNode.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.setEnvironment("Live");
        deploymentView.addAllDeploymentNodes();
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    void addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstances_WhenThereAreTopLevelDeploymentNodesWithContainerInstances() {
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
    void addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstances_WhenThereAreChildDeploymentNodesWithContainerInstances() {
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
    void addAllDeploymentNodes_AddsDeploymentNodesAndContainerInstancesOnlyForTheSoftwareSystemInScope() {
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
    void addDeploymentNode_AddsTheParentToo() {
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

    @Test
    void addDeploymentNode_ThrowsAnException_WhenAddingADeploymentNodeFromAnotherDeploymentEnvironment() {
        DeploymentNode devDeploymentNode = model.addDeploymentNode("Dev", "Deployment Node", "Description", "Technology");
        devDeploymentNode.addInfrastructureNode("Load Balancer");
        DeploymentNode liveDeploymentNode = model.addDeploymentNode("Live", "Deployment Node", "Description", "Technology");
        liveDeploymentNode.addInfrastructureNode("Load Balancer");

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.setEnvironment("Dev");
        deploymentView.add(devDeploymentNode); // should work

        try {
            deploymentView.add(liveDeploymentNode); // should fail
            fail();
        } catch (Exception e) {
            assertEquals("Only elements in the Dev deployment environment can be added to this view.", e.getMessage());
        }
    }

    @Test
    void addSoftwareSystemInstance_ThrowsAnException_WhenTheSoftwareSystemInstanceIsTheSoftwareSystemInScope() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add(softwareSystem);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.add(deploymentNode);

        // the software system instance won't have been added (neither will the empty parent deployment node)
        assertEquals(0, deploymentView.getElements().size());
        assertNull(deploymentView.getElementView(softwareSystemInstance));
    }

    @Test
    void addSoftwareSystemInstance_DoesNotAddTheSoftwareSystemInstance_WhenAChildContainerInstanceHasAlreadyBeenAdded() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNode1 = model.addDeploymentNode("Deployment Node 1", "Description", "Technology");
        DeploymentNode deploymentNode2 = model.addDeploymentNode("Deployment Node 2", "Description", "Technology");
        ContainerInstance containerInstance = deploymentNode1.add(container);
        SoftwareSystemInstance softwareSystemInstance = deploymentNode2.add(softwareSystem);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(deploymentNode1);
        deploymentView.add(deploymentNode2);

        // the software system instance won't have been added (neither will the empty parent deployment node)
        assertEquals(2, deploymentView.getElements().size());
        assertNotNull(deploymentView.getElementView(containerInstance));
        assertNull(deploymentView.getElementView(softwareSystemInstance));
    }

    @Test
    void addContainerInstance_DoesNotAddTheContainerInstance_WhenTheParentSoftwareSystemInstanceHasAlreadyBeenAdded() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNode1 = model.addDeploymentNode("Deployment Node 1", "Description", "Technology");
        DeploymentNode deploymentNode2 = model.addDeploymentNode("Deployment Node 2", "Description", "Technology");
        SoftwareSystemInstance softwareSystemInstance = deploymentNode1.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNode2.add(container);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(deploymentNode1);
        deploymentView.add(deploymentNode2);

        // the container instance won't have been added (neither will the empty parent deployment node)
        assertEquals(2, deploymentView.getElements().size());
        assertNotNull(deploymentView.getElementView(softwareSystemInstance));
        assertNull(deploymentView.getElementView(containerInstance));
    }

    @Test
    void addAnimationStep_ThrowsAnException_WhenNoElementInstancesAreSpecified() {
        try {
            deploymentView = views.createDeploymentView("deployment", "Description");
            deploymentView.addAnimation((ContainerInstance[]) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more software system/container instances must be specified.", iae.getMessage());
        }
    }

    @Test
    void addAnimationStep_ThrowsAnException_WhenNoInfrastructureNodesAreSpecified() {
        try {
            deploymentView = views.createDeploymentView("deployment", "Description");
            deploymentView.addAnimation((InfrastructureNode[]) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more infrastructure nodes must be specified.", iae.getMessage());
        }
    }

    @Test
    void addAnimationStep_ThrowsAnException_WhenNoElementInstancesOrInfrastructureNodesAreSpecified() {
        try {
            deploymentView = views.createDeploymentView("deployment", "Description");
            deploymentView.addAnimation(null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more software system/container instances and/or infrastructure nodes must be specified.", iae.getMessage());
        }
    }

    @Test
    void addAnimationStep() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");
        Container database = softwareSystem.addContainer("Database", "Description", "Technology");
        webApplication.uses(database, "Reads from and writes to", "JDBC/HTTPS");

        DeploymentNode developerLaptop = model.addDeploymentNode("Developer Laptop", "Description", "Technology");
        DeploymentNode apacheTomcat = developerLaptop.addDeploymentNode("Apache Tomcat", "Description", "Technology");
        DeploymentNode oracle = developerLaptop.addDeploymentNode("Oracle", "Description", "Technology");
        ContainerInstance webApplicationInstance = apacheTomcat.add(webApplication);
        ContainerInstance databaseInstance = oracle.add(database);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.add(developerLaptop);

        deploymentView.addAnimation(webApplicationInstance);
        deploymentView.addAnimation(databaseInstance);

        Animation step1 = deploymentView.getAnimations().stream().filter(step -> step.getOrder() == 1).findFirst().get();
        assertEquals(3, step1.getElements().size());
        assertTrue(step1.getElements().contains(developerLaptop.getId()));
        assertTrue(step1.getElements().contains(apacheTomcat.getId()));
        assertTrue(step1.getElements().contains(webApplicationInstance.getId()));
        assertEquals(0, step1.getRelationships().size());

        Animation step2 = deploymentView.getAnimations().stream().filter(step -> step.getOrder() == 2).findFirst().get();
        assertEquals(2, step2.getElements().size());
        assertTrue(step2.getElements().contains(oracle.getId()));
        assertTrue(step2.getElements().contains(databaseInstance.getId()));
        assertEquals(1, step2.getRelationships().size());
        assertTrue(step2.getRelationships().contains(webApplicationInstance.getRelationships().stream().findFirst().get().getId()));
    }

    @Test
    void addAnimationStep_IgnoresContainerInstancesThatDoNotExistInTheView() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");
        Container database = softwareSystem.addContainer("Database", "Description", "Technology");
        webApplication.uses(database, "Reads from and writes to", "JDBC/HTTPS");

        DeploymentNode developerLaptop = model.addDeploymentNode("Developer Laptop", "Description", "Technology");
        DeploymentNode apacheTomcat = developerLaptop.addDeploymentNode("Apache Tomcat", "Description", "Technology");
        DeploymentNode oracle = developerLaptop.addDeploymentNode("Oracle", "Description", "Technology");
        ContainerInstance webApplicationInstance = apacheTomcat.add(webApplication);
        ContainerInstance databaseInstance = oracle.add(database);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.add(apacheTomcat);

        deploymentView.addAnimation(webApplicationInstance, databaseInstance);

        Animation step1 = deploymentView.getAnimations().stream().filter(step -> step.getOrder() == 1).findFirst().get();
        assertEquals(3, step1.getElements().size());
        assertTrue(step1.getElements().contains(developerLaptop.getId()));
        assertTrue(step1.getElements().contains(apacheTomcat.getId()));
        assertTrue(step1.getElements().contains(webApplicationInstance.getId()));
        assertEquals(0, step1.getRelationships().size());
    }

    @Test
    void addAnimationStep_ThrowsAnException_WhenContainerInstancesAreSpecifiedButNoneOfThemExistInTheView() {
        try {
            SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
            Container webApplication = softwareSystem.addContainer("Web Application", "Description", "Technology");
            Container database = softwareSystem.addContainer("Database", "Description", "Technology");
            webApplication.uses(database, "Reads from and writes to", "JDBC/HTTPS");

            DeploymentNode developerLaptop = model.addDeploymentNode("Developer Laptop", "Description", "Technology");
            DeploymentNode apacheTomcat = developerLaptop.addDeploymentNode("Apache Tomcat", "Description", "Technology");
            DeploymentNode oracle = developerLaptop.addDeploymentNode("Oracle", "Description", "Technology");
            ContainerInstance webApplicationInstance = apacheTomcat.add(webApplication);
            ContainerInstance databaseInstance = oracle.add(database);

            deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");

            deploymentView.addAnimation(webApplicationInstance, databaseInstance);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("None of the specified elements exist in this view.", iae.getMessage());
        }
    }

    @Test
    void remove_RemovesTheInfrastructureNode() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(4, deploymentView.getElements().size());

        deploymentView.remove(infrastructureNode);
        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance)));
    }

    @Test
    void remove_RemovesTheSoftwareSystemInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node");
        SoftwareSystemInstance softwareSystemInstance = deploymentNodeChild.add(softwareSystem);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(4, deploymentView.getElements().size());

        deploymentView.remove(softwareSystemInstance);
        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(infrastructureNode)));
    }

    @Test
    void remove_RemovesTheContainerInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(4, deploymentView.getElements().size());

        deploymentView.remove(containerInstance);
        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(infrastructureNode)));
    }

    @Test
    void remove_RemovesTheDeploymentNodeAndChildren() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(4, deploymentView.getElements().size());

        deploymentView.remove(deploymentNodeChild);
        assertEquals(1, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
    }

    @Test
    void remove_RemovesTheChildDeploymentNodeAndChildren() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "Description", "Technology");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView(softwareSystem, "deployment", "Description");
        deploymentView.addAllDeploymentNodes();
        assertEquals(4, deploymentView.getElements().size());

        deploymentView.remove(deploymentNodeParent);
        assertEquals(0, deploymentView.getElements().size());
    }

    @Test
    void add_AddsTheInfrastructureNode() {
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode1 = deploymentNodeChild.addInfrastructureNode("Infrastructure Node 1");
        InfrastructureNode infrastructureNode2 = deploymentNodeChild.addInfrastructureNode("Infrastructure Node 2");

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(infrastructureNode1);

        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(infrastructureNode1)));
    }

    @Test
    void add_AddsTheSoftwareSystemInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node ");
        SoftwareSystemInstance softwareSystemInstance = deploymentNodeChild.add(softwareSystem);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(softwareSystemInstance);

        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(softwareSystemInstance)));
    }

    @Test
    void addSoftwareSystemInstance_ThrowsAnException_WhenAChildContainerInstanceHasAlreadyBeenAdded() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        SoftwareSystemInstance softwareSystemInstance = deploymentNodeChild.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(containerInstance);

        try {
            deploymentView.add(softwareSystemInstance);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A child of Software System is already in this view.", e.getMessage());
        }
    }

    @Test
    void add_AddsTheContainerInstance() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        InfrastructureNode infrastructureNode = deploymentNodeChild.addInfrastructureNode("Infrastructure Node ");
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(containerInstance);

        assertEquals(3, deploymentView.getElements().size());
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeParent)));
        assertTrue(deploymentView.getElements().contains(new ElementView(deploymentNodeChild)));
        assertTrue(deploymentView.getElements().contains(new ElementView(containerInstance)));
    }

    @Test
    void addContainerInstance_ThrowsAnException_WhenTheParentSoftwareSystemInstanceHasAlreadyBeenAdded() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNodeParent = model.addDeploymentNode("Deployment Node", "Description", "Technology");
        DeploymentNode deploymentNodeChild = deploymentNodeParent.addDeploymentNode("Deployment Node", "Description", "Technology");
        SoftwareSystemInstance softwareSystemInstance = deploymentNodeChild.add(softwareSystem);
        ContainerInstance containerInstance = deploymentNodeChild.add(container);

        deploymentView = views.createDeploymentView("deployment", "Description");
        deploymentView.add(softwareSystemInstance);

        try {
            deploymentView.add(containerInstance);
            fail();
        } catch (ElementNotPermittedInViewException e) {
            assertEquals("A parent of Container is already in this view.", e.getMessage());
        }
    }

}