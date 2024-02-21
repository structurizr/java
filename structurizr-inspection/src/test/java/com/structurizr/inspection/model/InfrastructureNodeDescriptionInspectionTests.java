package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.InfrastructureNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class
InfrastructureNodeDescriptionInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name");

        Violation violation = new InfrastructureNodeDescriptionInspection(new DefaultInspector(workspace)).run(infrastructureNode);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.infrastructurenode.description", violation.getType());
        assertEquals("The infrastructure node \"Default/Deployment Node/Name\" is missing a description.", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name", "Description", "Technology");

        Violation violation = new InfrastructureNodeDescriptionInspection(new DefaultInspector(workspace)).run(infrastructureNode);
        assertNull(violation);
    }

}
