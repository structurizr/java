package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.WorkspaceScope;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkspaceScopeValidatorFactoryTests {

    @Test
    void getValidator() {
        Workspace workspace = new Workspace("Name", "Description");
        assertTrue(WorkspaceScopeValidatorFactory.getValidator(workspace) instanceof UndefinedWorkspaceScopeValidator);

        workspace.setScope(WorkspaceScope.Landscape);
        assertTrue(WorkspaceScopeValidatorFactory.getValidator(workspace) instanceof LandscapeWorkspaceScopeValidator);

        workspace.setScope(WorkspaceScope.SoftwareSystem);
        assertTrue(WorkspaceScopeValidatorFactory.getValidator(workspace) instanceof SoftwareSystemWorkspaceScopeValidator);
    }

}