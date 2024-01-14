package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
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

        Violation violation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("structurizr.inspection.model.deploymentnode.empty", violation.getType());
        assertEquals("The deployment node named \"Name\" is empty.", violation.getMessage());
    }

    @Test
    public void run_WithDeploymentNode() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.addDeploymentNode("Deployment Node");

        Violation violation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(violation);
    }

    @Test
    public void run_WithInfrastructureNode() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.addInfrastructureNode("Infrastructure Node");

        Violation violation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(violation);
    }

    @Test
    public void run_WithSoftwareSystemInstance() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.add(softwareSystem);

        Violation violation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(violation);
    }

    @Test
    public void run_WithContainerInstance() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        Container container = softwareSystem.addContainer("Container");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Deployment Node", "Description", "Technology");
        deploymentNode.add(container);

        Violation violation = new EmptyDeploymentNodeInspection(workspace).run(deploymentNode);
        assertNull(violation);
    }

}
