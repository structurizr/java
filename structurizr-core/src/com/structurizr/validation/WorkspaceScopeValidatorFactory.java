package com.structurizr.validation;

import com.structurizr.Workspace;
import com.structurizr.WorkspaceScope;

public final class WorkspaceScopeValidatorFactory {

    public static WorkspaceScopeValidator getValidator(Workspace workspace) {
        if (workspace.getScope() == WorkspaceScope.Landscape) {
            return new LandscapeWorkspaceScopeValidator();
        } else if (workspace.getScope() == WorkspaceScope.SoftwareSystem) {
            return new SoftwareSystemWorkspaceScopeValidator();
        } else {
            return new UndefinedWorkspaceScopeValidator();
        }
    }

}