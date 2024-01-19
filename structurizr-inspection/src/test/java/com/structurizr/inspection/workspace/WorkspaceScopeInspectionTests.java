package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.configuration.WorkspaceScope;
import com.structurizr.inspection.DefaultInspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WorkspaceScopeInspectionTests {

    @Test
    public void run_WithUnscopedWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");

        Violation violation = new WorkspaceScopeInspection(new DefaultInspector(workspace)).run();
        Assertions.assertEquals(Severity.ERROR, violation.getSeverity());
        assertEquals("workspace.scope", violation.getType());
        assertEquals("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".", violation.getMessage());
    }

    @Test
    public void run_WithScopedWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getConfiguration().setScope(WorkspaceScope.SoftwareSystem);

        Violation violation = new WorkspaceScopeInspection(new DefaultInspector(workspace)).run();
        assertNull(violation);
    }

}
