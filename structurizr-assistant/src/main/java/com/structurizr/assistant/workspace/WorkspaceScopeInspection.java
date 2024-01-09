package com.structurizr.assistant.workspace;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;

public class WorkspaceScopeInspection extends WorkspaceInspection {

    public WorkspaceScopeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(Workspace workspace) {
        if (workspace.getConfiguration().getScope() == null) {
            return highPriorityRecommendation("This workspace has no defined scope. It is recommended that the workspace scope is set to \"Landscape\" or \"SoftwareSystem\".");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}