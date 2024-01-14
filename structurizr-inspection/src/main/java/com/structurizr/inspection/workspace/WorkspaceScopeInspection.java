package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;

public class WorkspaceScopeInspection extends WorkspaceInspection {

    public WorkspaceScopeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Violation inspect(Workspace workspace) {
        if (workspace.getConfiguration().getScope() == null) {
            return violation("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}