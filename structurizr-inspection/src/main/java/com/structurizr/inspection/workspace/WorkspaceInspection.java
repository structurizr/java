package com.structurizr.inspection.workspace;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;

abstract class WorkspaceInspection extends Inspection {

    public WorkspaceInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run() {
        Severity severity = getSeverity(getWorkspace());
        Violation violation = inspect(getWorkspace());

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(Workspace workspace);

}