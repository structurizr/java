package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.SoftwareSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmptyDeploymentNodeCheckTests {

    @Test
    public void run_Empty() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name");

        Recommendation recommendation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        Assertions.assertEquals(Priority.Low, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.deploymentnode.empty", recommendation.getType());
        assertEquals("The deployment node named \"Name\" is empty.", recommendation.getDescription());
    }

    @Test
    public void run_WithDeploymentNode() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.addDeploymentNode("Deployment Node");

        Recommendation recommendation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(recommendation);
    }

    @Test
    public void run_WithInfrastructureNode() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.addInfrastructureNode("Infrastructure Node");

        Recommendation recommendation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(recommendation);
    }

    @Test
    public void run_WithSoftwareSystemInstance() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.add(softwareSystem);

        Recommendation recommendation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(recommendation);
    }

    @Test
    public void run_WithContainerInstance() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.add(container);

        Recommendation recommendation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(recommendation);
    }

}
