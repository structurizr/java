package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ModelView;

abstract class AbstractModelViewInspection extends Inspection {

    public AbstractModelViewInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(ModelView view) {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, view);
        Violation violation = inspect(view);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(ModelView view);

}