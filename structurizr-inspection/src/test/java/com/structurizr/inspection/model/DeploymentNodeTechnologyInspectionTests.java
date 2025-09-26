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
DeploymentNodeTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name", "Description", "");

        Violation violation = new DeploymentNodeTechnologyInspection(new DefaultInspector(workspace)).run(deploymentNode);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.deploymentnode.technology", violation.getType());
        assertEquals("The deployment node \"Default/Name\" is missing a technology.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name", "Description", "Technology");

        Violation violation = new DeploymentNodeTechnologyInspection(new DefaultInspector(workspace)).run(deploymentNode);
        assertNull(violation);
    }

}
