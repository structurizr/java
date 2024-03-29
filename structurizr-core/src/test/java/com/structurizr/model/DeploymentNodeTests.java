package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import com.structurizr.util.MapUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeploymentNodeTests extends AbstractWorkspaceTestBase {

    @Test
    void getCanonicalName_WhenTheDeploymentNodeHasNoParent() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Ubuntu Server", "", "");

        assertEquals("DeploymentNode://Default/Ubuntu Server", deploymentNode.getCanonicalName());
    }

    @Test
    void getCanonicalName_WhenTheDeploymentNodeHasAParent() {
        DeploymentNode l1 = model.addDeploymentNode("Level 1", "", "");
        DeploymentNode l2 = l1.addDeploymentNode("Level 2", "", "");
        DeploymentNode l3 = l2.addDeploymentNode("Level 3", "", "");

        assertEquals("DeploymentNode://Default/Level 1", l1.getCanonicalName());
        assertEquals("DeploymentNode://Default/Level 1/Level 2", l2.getCanonicalName());
        assertEquals("DeploymentNode://Default/Level 1/Level 2/Level 3", l3.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentDeploymentNode() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
        assertNull(parent.getParent());

        DeploymentNode child = parent.addDeploymentNode("Child", "", "");
        child.setParent(parent);
        assertSame(parent, child.getParent());
    }

    @Test
    void getRequiredTags() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertEquals(2, deploymentNode.getDefaultTags().size());
        assertTrue(deploymentNode.getDefaultTags().contains(Tags.ELEMENT));
        assertTrue(deploymentNode.getDefaultTags().contains(Tags.DEPLOYMENT_NODE));
    }

    @Test
    void getTags() {
        DeploymentNode deploymentNode = new DeploymentNode();
        deploymentNode.addTags("Tag 1", "Tag 2");
        assertEquals("Element,Deployment Node,Tag 1,Tag 2", deploymentNode.getTags());
    }

    @Test
    void add_ThrowsAnException_WhenASoftwareSystemIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((SoftwareSystem) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A software system must be specified.", iae.getMessage());
        }
    }

    @Test
    void add_ThrowsAnException_WhenAContainerIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");
            deploymentNode.add((Container) null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A container must be specified.", iae.getMessage());
        }
    }

    @Test
    void add_AddsAContainerInstance_WhenAContainerIsSpecified() {
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
    void addDeploymentNode_ThrowsAnException_WhenANameIsNotSpecified() {
        try {
            DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
            parent.addDeploymentNode(null, "", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    void addDeploymentNode_AddsAChildDeploymentNode_WhenANameIsSpecified() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");

        DeploymentNode child = parent.addDeploymentNode("Child 1", "Description", "Technology");
        assertNotNull(child);
        assertEquals("Child 1", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals("1", child.getInstances());
        assertTrue(child.getProperties().isEmpty());
        assertTrue(parent.getChildren().contains(child));

        child = parent.addDeploymentNode("Child 2", "Description", "Technology", 4);
        assertNotNull(child);
        assertEquals("Child 2", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals("4", child.getInstances());
        assertTrue(child.getProperties().isEmpty());
        assertTrue(parent.getChildren().contains(child));

        child = parent.addDeploymentNode("Child 3", "Description", "Technology", 4, MapUtils.create("name=value"));
        assertNotNull(child);
        assertEquals("Child 3", child.getName());
        assertEquals("Description", child.getDescription());
        assertEquals("Technology", child.getTechnology());
        assertEquals("Default", child.getEnvironment());
        assertEquals("4", child.getInstances());
        assertEquals(1, child.getProperties().size());
        assertEquals("value", child.getProperties().get("name"));
        assertTrue(parent.getChildren().contains(child));
    }

    @Test
    void uses_ThrowsAnException_WhenANullDestinationIsSpecified() {
        try {
            DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "", "");
            deploymentNode.uses((DeploymentNode) null, "", "");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The destination must be specified.", iae.getMessage());
        }
    }

    @Test
    void uses_AddsARelationship() {
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
    void getDeploymentNodeWithName_ThrowsAnException_WhenANameIsNotSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.getDeploymentNodeWithName(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A name must be specified.", iae.getMessage());
        }
    }

    @Test
    void getDeploymentNodeWithName_ReturnsNull_WhenThereIsNoDeploymentNodeWithTheSpecifiedName() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertNull(deploymentNode.getDeploymentNodeWithName("foo"));
    }

    @Test
    void getDeploymentNodeWithName_ReturnsTheNamedDeploymentNode_WhenThereIsADeploymentNodeWithTheSpecifiedName() {
        DeploymentNode parent = model.addDeploymentNode("parent", "", "");
        DeploymentNode child = parent.addDeploymentNode("child", "", "");
        assertSame(child, parent.getDeploymentNodeWithName("child"));
    }

    @Test
    void getInfrastructureNodeWithName_ReturnsNull_WhenThereIsNoInfrastructureNodeWithTheSpecifiedName() {
        DeploymentNode deploymentNode = new DeploymentNode();
        assertNull(deploymentNode.getInfrastructureNodeWithName("foo"));
    }

    @Test
    void getInfrastructureNodeWithName_ReturnsTheNamedDeploymentNode_WhenThereIsAInfrastructureNodeWithTheSpecifiedName() {
        DeploymentNode parent = model.addDeploymentNode("parent", "", "");
        InfrastructureNode child = parent.addInfrastructureNode("child", "", "");
        assertSame(child, parent.getInfrastructureNodeWithName("child"));
    }

    @Test
    void setInstances_ThrowsAnException_WhenAZeroIsSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances("0");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Number of instances must be a positive integer or a range.", iae.getMessage());
        }
    }

    @Test
    void setInstances_ThrowsAnException_WhenANegativeNumberIsSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances("-1");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Number of instances must be a positive integer or a range.", iae.getMessage());
        }
    }

    @Test
    void setInstancesAsPositiveInteger() {
        DeploymentNode deploymentNode = new DeploymentNode();

        deploymentNode.setInstances("8");
        assertEquals("8", deploymentNode.getInstances());

        deploymentNode.setInstances(8);
        assertEquals("8", deploymentNode.getInstances());
    }

    @Test
    void setInstances_ThrowsAnException_WhenAnInvalidRangeIsSpecified() {
        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances("x..N");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Number of instances must be a positive integer or a range.", iae.getMessage());
        }

        try {
            DeploymentNode deploymentNode = new DeploymentNode();
            deploymentNode.setInstances("2..1");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("Range upper bound must be greater than the lower bound.", iae.getMessage());
        }
    }

    @Test
    void setInstancesAsRangeWithBoundedUpperRange() {
        DeploymentNode deploymentNode = new DeploymentNode();

        deploymentNode.setInstances("0..2");
        assertEquals("0..2", deploymentNode.getInstances());
        
        deploymentNode.setInstances("1..2");
        assertEquals("1..2", deploymentNode.getInstances());

        deploymentNode.setInstances("5..10");
        assertEquals("5..10", deploymentNode.getInstances());
    }

    @Test
    void setInstancesAsRangeWithUnboundedUpperRange() {
        DeploymentNode deploymentNode = new DeploymentNode();

        deploymentNode.setInstances("0..N");
        assertEquals("0..N", deploymentNode.getInstances());

        deploymentNode.setInstances("1..N");
        assertEquals("1..N", deploymentNode.getInstances());

        deploymentNode.setInstances("0..*");
        assertEquals("0..*", deploymentNode.getInstances());

        deploymentNode.setInstances("1..*");
        assertEquals("1..*", deploymentNode.getInstances());
    }

}