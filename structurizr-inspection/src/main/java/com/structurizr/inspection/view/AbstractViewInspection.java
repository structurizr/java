package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.View;

abstract class AbstractViewInspection extends Inspection {

    public AbstractViewInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(View view) {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, view);
        Violation violation = inspect(view);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(View view);

}