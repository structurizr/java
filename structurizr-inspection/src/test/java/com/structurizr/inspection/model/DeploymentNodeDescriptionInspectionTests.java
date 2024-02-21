package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
DeploymentNodeDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name");

        Violation violation = new DeploymentNodeDescriptionInspection(new DefaultInspector(workspace)).run(deploymentNode);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.deploymentnode.description", violation.getType());
        assertEquals("The deployment node \"Default/Name\" is missing a description.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name", "Description", "Technology");

        Violation violation = new DeploymentNodeDescriptionInspection(new DefaultInspector(workspace)).run(deploymentNode);
        assertNull(violation);
    }

}
