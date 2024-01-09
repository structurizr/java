package com.structurizr.assistant.workspace;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;

abstract class WorkspaceInspection extends Inspection {

    public WorkspaceInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run() {
        if (isEnabled(getType(), getWorkspace())) {
            return inspect(getWorkspace());
        }

        return null;
    }

    protected abstract Recommendation inspect(Workspace workspace);

}