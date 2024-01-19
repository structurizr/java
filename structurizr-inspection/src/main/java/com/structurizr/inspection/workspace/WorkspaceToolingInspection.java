package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.util.StringUtils;

public class WorkspaceToolingInspection extends AbstractWorkspaceInspection {

    private static final String CLOUD_SERVICE_DSL_EDITOR = "structurizr-cloud/dsl-editor";
    private static final String ONPREMISES_DSL_EDITOR = "structurizr-onpremises/dsl-editor";

    public WorkspaceToolingInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    public Violation inspect(Workspace workspace) {
        if (!StringUtils.isNullOrEmpty(workspace.getLastModifiedAgent())) {
            if (workspace.getLastModifiedAgent().startsWith(CLOUD_SERVICE_DSL_EDITOR) || workspace.getLastModifiedAgent().startsWith(ONPREMISES_DSL_EDITOR)) {
                return violation("The browser-based DSL editor is the easiest way to get started without installing any tooling, but it does not provide access to the full feature set of the Structurizr DSL. It is recommended that you use the Structurizr DSL in conjunction with the Structurizr CLI's \"push\" command.");
            }
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "workspace.tooling";
    }

}