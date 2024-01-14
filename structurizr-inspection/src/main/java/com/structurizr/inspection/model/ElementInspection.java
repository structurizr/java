package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;

abstract class ElementInspection extends Inspection {

    public ElementInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run(Element element) {
        Element parentElement = element.getParent();

        Severity severity = getSeverity(getType(), getWorkspace(), getWorkspace().getModel(), parentElement, element);
        Violation violation = inspect(element);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Violation inspect(Element element);

}