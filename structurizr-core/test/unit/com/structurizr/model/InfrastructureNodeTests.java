package com.structurizr.model;

import com.structurizr.AbstractWorkspaceTestBase;
import org.junit.Test;

import static org.junit.Assert.*;

public class InfrastructureNodeTests extends AbstractWorkspaceTestBase {

    @Test
    public void test_getCanonicalName() {
        DeploymentNode deploymentNode = model.addDeploymentNode("Amazon Web Services", "", "");
        InfrastructureNode infrastructureNode = deploymentNode.addInfrastructureNode("Route 53", "", "");

        assertEquals("InfrastructureNode://Default/Amazon Web Services/Route 53", infrastructureNode.getCanonicalName());
    }

    @Test
    public void test_getParent_ReturnsTheParentDeploymentNode() {
        DeploymentNode parent = model.addDeploymentNode("Parent", "", "");
        InfrastructureNode child = parent.addInfrastructureNode("Child", "", "");
        child.setParent(parent);
        assertSame(parent, child.getParent());
    }

    @Test
    public void test_getRequiredTags() {
        InfrastructureNode infrastructureNode = new InfrastructureNode();
        assertEquals(2, infrastructureNode.getRequiredTags().size());
        assertTrue(infrastructureNode.getRequiredTags().contains(Tags.ELEMENT));
        assertTrue(infrastructureNode.getRequiredTags().contains(Tags.INFRASTRUCTURE_NODE));
    }

    @Test
    public void test_getTags() {
        InfrastructureNode infrastructureNode = new InfrastructureNode();
        infrastructureNode.addTags("Tag 1", "Tag 2");
        assertEquals("Element,Infrastructure Node,Tag 1,Tag 2", infrastructureNode.getTags());
    }

}