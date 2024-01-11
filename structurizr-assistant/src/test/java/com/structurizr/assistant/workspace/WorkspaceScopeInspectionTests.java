package com.structurizr.assistant.workspace;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.configuration.WorkspaceScope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WorkspaceScopeInspectionTests {

    @Test
    public void run_WithUnscopedWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");

        Recommendation recommendation = new WorkspaceScopeInspection(workspace).run();
        Assertions.assertEquals(Recommendation.Priority.High, recommendation.getPriority());
        assertEquals("structurizr.recommendations.workspace.scope", recommendation.getType());
        assertEquals("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".", recommendation.getDescription());
    }

    @Test
    public void run_WithScopedWorkspace() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getConfiguration().setScope(WorkspaceScope.SoftwareSystem);

        Recommendation recommendation = new WorkspaceScopeInspection(workspace).run();
        assertNull(recommendation);
    }

}
