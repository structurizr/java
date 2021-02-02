package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.util.MapUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeploymentNodeTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_getCanonicalName_WhenTheDeploymentNodeHasNoParent() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Ubuntu Server", "", "");

        assertEquals("DeploymentNode://Default/Ubuntu Server", deploymentNode.getCanonicalName());
    }

    @Test
    public void test_getCanonicalName_WhenTheDeploymentNodeHasAParent() {
        DeploymentNode parent = model.addDeploymentNode("Windows Server", "", "");
        DeploymentNode child = parent.addDeploymentNode("Apache Tomcat", "", "");

        assertEquals("DeploymentNode://Default/Windows Server/Apache Tomcat", child.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentDeploymentNode() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
        assertNull(parent.getParent());

        DeploymentNode child = parent.addDeploymentNode("Child", "", "");
        child.setParent(parent);
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_getRequiredTags() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertEquals(2, deploymentNode.getRequiredTags().size());
        assertTrue(deploymentNode.getRequiredTags().contains(Tags.ELEMENT));
        assertTrue(deploymentNode.getRequiredTags().contains(Tags.DEPLOYMENT_NODE));
    }

    @Test
    public void test_getTags() {
        DeploymentNode deploymentNode = new DeploymentNode();
        deploymentNode.addTags("Tag 1", "Tag 2");
        assertEquals("Element,Deployment Node,Tag 1,Tag 2", deploymentNode.getTags());
    }

    @Test
    public void test_add_ThrowsAnException_WhenASoftwareSystemIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((SoftwareSystem) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_add_ThrowsAnException_WhenAContainerIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((Container)null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_add_AddsAContainerInstance_WhenAContainerIsSpecified() {
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container", "", "");
        DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "", "");
        ContainerInstance containerInstance = deploymentNode.add(container);

        assertNotNull(containerInstance);
        assertSame(container, containerInstance.getContainer());
        assertTrue(deploymentNode.getContainerInstances().contains(containerInstance));
        assertEquals("ContainerInstance://Default/Deployment Node/Software System.Container[1]", containerInstance.getCanonicalName());
    }

    @Test
    public void test_addDeploymentNode_ThrowsAnException_WhenANameIsNotSpecified() {
        try {
            DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
            parent.addDeploymentNode(null, "", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_addDeploymentNode_AddsAChildDeploymentNode_WhenANameIsSpecified() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");

        DeploymentNode child = parent.addDeploymentNode("Child 1", "Description", "Technology");
        assertNotNull(child);
        assertEquals("Child 1", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals(1, child.getInstances());
        assertTrue(child.getProperties().isEmpty());
        assertTrue(parent.getChildren().contains(child));

        child = parent.addDeploymentNode("Child 2", "Description", "Technology", 4);
        assertNotNull(child);
        assertEquals("Child 2", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals(4, child.getInstances());
        assertTrue(child.getProperties().isEmpty());
        assertTrue(parent.getChildren().contains(child));

        child = parent.addDeploymentNode("Child 3", "Description", "Technology", 4, MapUtils.create("name=value"));
        assertNotNull(child);
        assertEquals("Child 3", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals(4, child.getInstances());
        assertEquals(1, child.getProperties().size());
        assertEquals("value", child.getProperties().get("name"));
        assertTrue(parent.getChildren().contains(child));
    }

    @Test
    public void test_uses_ThrowsAnException_WhenANullDestinationIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "", "");
            deploymentNode.uses((DeploymentNode)null, "", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The destination must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_uses_AddsARelationship() {
        DeploymentNode primaryNode = model.addDeploymentNode("MySQL - Primary", "", "");
        DeploymentNode secondaryNode = model.addDeploymentNode("MySQL - Secondary", "", "");
        Relationship relationship = primaryNode.uses(secondaryNode, "Replicates data to", "Some technology");

        assertNotNull(relationship);
        assertSame(primaryNode, relationship.getSource());
        assertSame(secondaryNode, relationship.getDestination());
        assertEquals("Replicates data to", relationship.getDescription());
        assertEquals("Some technology", relationship.getTechnology());
    }

    @Test
    public void test_getDeploymentNodeWithName_ThrowsAnException_WhenANameIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.getDeploymentNodeWithName(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    public void test_getDeploymentNodeWithName_ReturnsNull_WhenThereIsNoDeploymentNodeWithTheSpecifiedName() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertNull(deploymentNode.getDeploymentNodeWithName("foo"));
    }

    @Test
    public void test_getDeploymentNodeWithName_ReturnsTheNamedDeploymentNode_WhenThereIsADeploymentNodeWithTheSpecifiedName() {
        DeploymentNode parent = model.addDeploymentNode("parent", "", "");
        DeploymentNode child = parent.addDeploymentNode("child", "", "");
        assertSame(child, parent.getDeploymentNodeWithName("child"));
    }

    @Test
    public void test_getInfrastructureNodeWithName_ReturnsNull_WhenThereIsNoInfrastructureNodeWithTheSpecifiedName() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertNull(deploymentNode.getInfrastructureNodeWithName("foo"));
    }

    @Test
    public void test_getInfrastructureNodeWithName_ReturnsTheNamedDeploymentNode_WhenThereIsAInfrastructureNodeWithTheSpecifiedName() {
        DeploymentNode parent = model.addDeploymentNode("parent", "", "");
        InfrastructureNode child = parent.addInfrastructureNode("child", "", "");
        assertSame(child, parent.getInfrastructureNodeWithName("child"));
    }

    @Test
    public void test_setInstances() {
        DeploymentNode deploymentNode = new DeploymentNode();
        deploymentNode.setInstances(8);

        assertEquals(8, deploymentNode.getInstances());
    }

    @Test
    public void test_setInstances_ThrowsAnException_WhenZeroIsSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances(0);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Number of instances must be a positive integer.", iae.getMessage());
        }
    }

    @Test
    public void test_setInstances_ThrowsAnException_WhenANegativeNumberIsSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances(-1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Number of instances must be a positive integer.", iae.getMessage());
        }
    }

}