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
InfrastructureNodeTechnologyInspectionTests {

    @Test
    public void run_WithoutDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name");

        Violation violation = new InfrastructureNodeTechnologyInspection(new DefaultInspector(workspace)).run(infrastructureNode);
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("model.infrastructurenode.technology", violation.getType());
        assertEquals("Add a technology to the infrastructure node named \"Name\".", violation.getMessage());
    }

    @Test
    public void run_WithDescription() {
        Workspace workspace = new Workspace("Name", "Description");
        InfrastructureNode infrastructureNode = workspace.getModel().addDeploymentNode("Deployment Node")
                .addInfrastructureNode("Name", "Description", "Technology");

        Violation violation = new InfrastructureNodeTechnologyInspection(new DefaultInspector(workspace)).run(infrastructureNode);
        assertNull(violation);
    }

}
