package com.structurizr.assistant.workspace;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.configuration.WorkspaceScope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WorkspaceToolingInspectionTests {

    @Test
    public void run_WithLastModifiedAgentAsCloudServiceDslEditor() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-cloud/dsl-editor/1234567890");

        Recommendation recommendation = new WorkspaceToolingInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.tooling", recommendation.getType());
        assertEquals("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.", recommendation.getDescription());
    }

    @Test
    public void run_WithLastModifiedAgentAsOnpremisesInstallationDslEditor() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-onpremises/dsl-editor/1234567890");

        Recommendation recommendation = new WorkspaceToolingInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.tooling", recommendation.getType());
        assertEquals("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.", recommendation.getDescription());
    }

    @Test
    public void run_WithLastModifiedAgentAsStructurizrCli() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-cli/2.0.0");

        Recommendation recommendation = new WorkspaceToolingInspection(workspace).run();
        assertNull(recommendation);
    }

}
