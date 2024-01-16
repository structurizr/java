package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;

abstract class ModelInspection extends Inspection {

    public ModelInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run() {
        Severity severity = getSeverity(getWorkspace(), getWorkspace().getModel());
        Violation violation = inspect(getWorkspace());

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(Workspace workspace);

}