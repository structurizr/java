package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;

abstract class AbstractModelInspection extends Inspection {

    public AbstractModelInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run() {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, getWorkspace().getModel());
        Violation violation = inspect(getWorkspace());

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(Workspace workspace);

}