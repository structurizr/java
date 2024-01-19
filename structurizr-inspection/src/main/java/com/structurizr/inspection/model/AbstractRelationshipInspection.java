package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

public abstract class AbstractRelationshipInspection extends Inspection {

    public AbstractRelationshipInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(Relationship relationship) {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, relationship);
        Violation violation = inspect(relationship);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Violation inspect(Relationship relationship);

}