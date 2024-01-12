package com.structurizr.assistant.workspace;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;

public class WorkspaceToolingInspection extends WorkspaceInspection {

    private static final String CLOUD_SERVICE_DSL_EDITOR = "structurizr-cloud/dsl-editor";
    private static final String ONPREMISES_DSL_EDITOR = "structurizr-onpremises/dsl-editor";

    public WorkspaceToolingInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(Workspace workspace) {
        if (workspace.getLastModifiedAgent().startsWith(CLOUD_SERVICE_DSL_EDITOR) || workspace.getLastModifiedAgent().startsWith(ONPREMISES_DSL_EDITOR)) {
            return highPriorityRecommendation("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "workspace.tooling";
    }

}