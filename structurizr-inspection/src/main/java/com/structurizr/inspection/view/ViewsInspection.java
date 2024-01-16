package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;

abstract class ViewsInspection extends Inspection {

    public ViewsInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run() {
        Severity severity = getSeverity(getWorkspace(), getWorkspace().getViews().getConfiguration());
        Violation violation = inspect(getWorkspace());

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(Workspace workspace);

}