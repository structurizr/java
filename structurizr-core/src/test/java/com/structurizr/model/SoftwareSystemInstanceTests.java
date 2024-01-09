package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SoftwareSystemInstanceTests extends AbstractWorkspaceTestBase {

    private SoftwareSystem softwareSystem = model.addSoftwareSystem("System", "Description");
    private DeploymentNode deploymentNode = model.addDeploymentNode("Deployment Node", "Description", "Technology");

    @Test
    void construction() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertSame(softwareSystem, instance.getSoftwareSystem());
        assertEquals(softwareSystem.getId(), instance.getSoftwareSystemId());
        assertEquals(1, instance.getInstanceId());
    }

    @Test
    void getSoftwareSystemId() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertEquals(softwareSystem.getId(), instance.getSoftwareSystemId());
        instance.setSoftwareSystem(null);
        instance.setSoftwareSystemId("1234");
        assertEquals("1234", instance.getSoftwareSystemId());
    }

    @Test
    void getName_CannotBeChanged() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertEquals("System", instance.getName());

        instance.setName("foo");
        assertEquals("System", instance.getName());
    }

    @Test
    void getCanonicalName() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertEquals("SoftwareSystemInstance://Default/Deployment Node/System[1]", instance.getCanonicalName());
    }

    @Test
    void getParent_ReturnsTheParentDeploymentNode() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertEquals(deploymentNode, instance.getParent());
    }

    @Test
    void getRequiredTags() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertTrue(instance.getDefaultTags().isEmpty());
    }

    @Test
    void getTags() {
        softwareSystem.addTags("Tag 1");
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);
        instance.addTags("Primary Instance");

        assertEquals("Software System Instance,Primary Instance", instance.getTags());
    }

    @Test
    void removeTags_DoesNotRemoveAnyTags() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertTrue(instance.getTags().contains(Tags.SOFTWARE_SYSTEM_INSTANCE));

        instance.removeTag(Tags.SOFTWARE_SYSTEM_INSTANCE);

        assertTrue(instance.getTags().contains(Tags.SOFTWARE_SYSTEM_INSTANCE));
    }

    @Test
    void addHealthCheck() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);
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
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck(null, "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheNameIsEmpty() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck(" ", "http://localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The name must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsNull() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck("Name", null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsEmpty() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck("Name", " ");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The URL must not be null or empty.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheUrlIsInvalid() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck("Name", "localhost");
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("localhost is not a valid URL.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheIntervalIsLessThanZero() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck("Name", "https://localhost", -1, 0);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The polling interval must be zero or a positive integer.", iae.getMessage());
        }
    }

    @Test
    void addHealthCheck_ThrowsAnException_WhenTheTimeoutIsLessThanZero() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        try {
            instance.addHealthCheck("Name", "https://localhost", 60, -1);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("The timeout must be zero or a positive integer.", iae.getMessage());
        }
    }

    @Test
    void getDeploymentGroups_WhenNoGroupsHaveBeenSpecified() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem);

        assertEquals(1, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Default"));
    }

    @Test
    void getDeploymentGroups_WhenOneGroupHasBeenSpecified() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem, "Group 1");

        assertEquals(1, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Group 1"));
    }

    @Test
    void getDeploymentGroups_WhenMultipleGroupsAreSpecified() {
        SoftwareSystemInstance instance = deploymentNode.add(softwareSystem, "Group 1", "Group 2");

        assertEquals(2, instance.getDeploymentGroups().size());
        assertTrue(instance.getDeploymentGroups().contains("Group 1"));
        assertTrue(instance.getDeploymentGroups().contains("Group 2"));
    }

}