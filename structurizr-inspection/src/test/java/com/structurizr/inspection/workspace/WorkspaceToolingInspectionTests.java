package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WorkspaceToolingInspectionTests {

    @Test
    public void run_WithLastModifiedAgentAsCloudServiceDslEditor() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-cloud/dsl-editor/1234567890");

        Violation violation = new WorkspaceToolingInspection(new DefaultInspector(workspace)).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("workspace.tooling", violation.getType());
        assertEquals("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.", violation.getMessage());
    }

    @Test
    public void run_WithLastModifiedAgentAsOnpremisesInstallationDslEditor() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-onpremises/dsl-editor/1234567890");

        Violation violation = new WorkspaceToolingInspection(new DefaultInspector(workspace)).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("workspace.tooling", violation.getType());
        assertEquals("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.", violation.getMessage());
    }

    @Test
    public void run_WithLastModifiedAgentAsStructurizrCli() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.setLastModifiedAgent("structurizr-cli/2.0.0");

        Violation violation = new WorkspaceToolingInspection(new DefaultInspector(workspace)).run();
        assertNull(violation);
    }

}
