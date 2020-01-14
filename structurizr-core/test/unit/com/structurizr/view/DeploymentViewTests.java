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

    @Test
    public void test_addAnimationStep_ThrowsAnException_WhenNoContainerInstancesAreSpecified() {
        try {
            deploymentView = views.createDeploymentView("deployment", "Description");
            deploymentView.addAnimation();
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("One or more container instances must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addAnimationStep() {
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
    public void test_addAnimationStep_IgnoresContainerInstancesThatDoNotExistInTheView() {
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
    public void test_addAnimationStep_ThrowsAnException_WhenContainerInstancesAreSpecifiedButNoneOfThemExistInTheView() {
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
            assertEquals("None of the specified container instances exist in this view.", iae.getMessage());
        }
    }

}