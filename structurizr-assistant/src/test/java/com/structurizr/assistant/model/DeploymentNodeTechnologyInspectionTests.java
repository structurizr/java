package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Priority;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.DeploymentNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeploymentNodeTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name");

        Recommendation recommendation = new DeploymentNodeTechnologyInspection(workspace).run(deploymentNode);
        Assertions.assertEquals(Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.deploymentnode.technology", recommendation.getType());
        assertEquals("Add a technology to the deployment node named \"Name\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        DeploymentNode deploymentNode = workspace.getModel().addDeploymentNode("Name", "Description", "Technology");

        Recommendation recommendation = new DeploymentNodeTechnologyInspection(workspace).run(deploymentNode);
        assertNull(recommendation);
    }

}
