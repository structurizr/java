package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerInstanceTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem("System", "Description");
    private Container database = softwareSystem.addContainer("Database Schema", "Stores data", "MySQL");
    private DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");

    @Test
    void construction() {
        ContainerInstance instance = deploymentNode.add(database);

        assertSame(database, instance.getContainer());
        assertEquals(database.getId(), instance.getContainerId());
        assertEquals(1, instance.getInstanceId());
    }

    @Test
    void getContainerId() {
        ContainerInstance instance = deploymentNode.add(database);

        assertEquals(database.getId(), instance.getContainerId());
        instance.setContainer(null);
        instance.setContainerId("1234");
        assertEquals("1234", instance.getContainerId());
    }

    @Test
    void getName() {
        ContainerInstance instance = deploymentNode.add(database);

        assertEquals("Database Schema", instance.getName());

        instance.setName("foo");
        assertEquals("Database Schema", instance.getName());
    }

    @Test
    void getCanonicalName() {
        ContainerInstance instance = deploymentNode.add(database);

        assertEquals("ContainerInstance://Default/Deployment Node/System.Database Schema[1]", instance.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentDeploymentNode() {
        ContainerInstance instance = deploymentNode.add(database);

        assertEquals(deploymentNode, instance.getParent());
    }

    @Test
    void getRequiredTags() {
        ContainerInstance instance = deploymentNode.add(database);

        assertTrue(instance.getDefaultTags().isEmpty());
    }

    @Test
    void getTags() {
        database.addTags("Database");
        ContainerInstance instance = deploymentNode.add(database);
        instance.addTags("Primary Instance");

        assertEquals("Container Instance,Primary Instance", instance.getTags());
    }

    @Test
    void removeTags_DoesNotRemoveAnyTags() {
        ContainerInstance instance = deploymentNode.add(database);

        assertTrue(instance.getTags().contains(Tags.CONTAINER_INSTANCE));

        instance.removeTag(Tags.CONTAINER_INSTANCE);

        assertTrue(instance.getTags().contains(Tags.CONTAINER_INSTANCE));
    }

    @Test
    void getDeploymentGroups_WhenNoGroupsHaveBeenSpecified() {
        ContainerInstance instance = deploymentNode.add(database);

        assertEquals(1, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Default"));
    }

    @Test
    void getDeploymentGroups_WhenOneGroupHasBeenSpecified() {
        ContainerInstance instance = deploymentNode.add(database, "Group 1");

        assertEquals(1, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Group 1"));
    }

    @Test
    void getDeploymentGroups_WhenMultipleGroupsAreSpecified() {
        ContainerInstance instance = deploymentNode.add(database, "Group 1", "Group 2");

        assertEquals(2, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Group 1"));
        assertTrue(instance.getDeploymentGroups().contains("Group 2"));
    }

    @Test
    void addHealthCheck() {
        ContainerInstance instance = deploymentNode.add(database);
        assertTrue(instance.getHealthChecks().isEmpty());

        HttpHealthCheck healthCheck = instance.addHealthCheck("Test web application is working", "http://localhost:8080");
        assertEquals("Test web application is working", healthCheck.getName());
        assertEquals("http://localhost:8080", healthCheck.getUrl());
        assertEquals(60, healthCheck.getInterval());
        assertEquals(0, healthCheck.getTimeout());
        assertEquals(1, instance.getHealthChecks().size());
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheNameIsNull() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck(null, "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheNameIsEmpty() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck(" ", "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsNull() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck("Name", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsEmpty() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck("Name", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsInvalid() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck("Name", "localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("localhost is not a valid URL.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheIntervalIsLessThanZero() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck("Name", "https://localhost", -1, 0);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The polling interval must be zero or a positive integer.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheTimeoutIsLessThanZero() {
        ContainerInstance instance = deploymentNode.add(database);

        try {
            instance.addHealthCheck("Name", "https://localhost", 60, -1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The timeout must be zero or a positive integer.", iae.getMessage());
        }
    }

}