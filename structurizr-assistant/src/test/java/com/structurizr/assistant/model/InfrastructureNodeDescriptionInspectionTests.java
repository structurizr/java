package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.InfrastructureNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InfrastructureNodeDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name");

        Recommendation recommendation = new InfrastructureNodeDescriptionInspection(workspace).run(infrastructureNode);
        Assertions.assertEquals(Recommendation.Priority.Medium, recommendation.getPriority());
        assertEquals("structurizr.recommendations.model.infrastructurenode.description", recommendation.getType());
        assertEquals("Add a description to the infrastructure node named \"Name\".", recommendation.getDescription());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name", "Description", "Technology");

        Recommendation recommendation = new InfrastructureNodeDescriptionInspection(workspace).run(infrastructureNode);
        assertNull(recommendation);
    }

}
