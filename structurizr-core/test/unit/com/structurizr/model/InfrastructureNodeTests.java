package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfrastructureNodeTests extends AbstractWorkspaceTestBase {

    @Test
    void test_getCanonicalName() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services", "", "");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Route 53", "", "");

        assertEquals("InfrastructureNode://Default/Amazon Web Services/Route 53", infrastructureNode.getCanonicalName());
    }

    @Test
    void test_getParent_ReturnsTheParentDeploymentNode() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
        InfrastructureNode child = parent.addInfrastructureNode("Child", "", "");
        child.setParent(parent);
        assertSame(parent, child.getParent());
    }

    @Test
    void test_getRequiredTags() {
        InfrastructureNode infrastructureNode = new InfrastructureNode();
        assertEquals(2, infrastructureNode.getDefaultTags().size());
        assertTrue(infrastructureNode.getDefaultTags().contains(Tags.ELEMENT));
        assertTrue(infrastructureNode.getDefaultTags().contains(Tags.INFRASTRUCTURE_NODE));
    }

    @Test
    void test_getTags() {
        InfrastructureNode infrastructureNode = new InfrastructureNode();
        infrastructureNode.addTags("Tag 1", "Tag 2");
        assertEquals("Element,Infrastructure Node,Tag 1,Tag 2", infrastructureNode.getTags());
    }

}