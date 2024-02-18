package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;

public class WorkspaceScopeInspection extends AbstractWorkspaceInspection {

    public WorkspaceScopeInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Workspace workspace) {
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