package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;

abstract class AbstractElementInspection extends Inspection {

    public AbstractElementInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(Element element) {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, element);
        Violation violation = inspect(element);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Violation inspect(Element element);

}