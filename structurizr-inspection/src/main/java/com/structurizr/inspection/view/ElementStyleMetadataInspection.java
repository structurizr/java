package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ElementStyle;

public class ElementStyleMetadataInspection extends Inspection {

    public ElementStyleMetadataInspection(Inspector inspector) {
        super(inspector);
    }

    public final Violation run(ElementStyle elementStyle) {
        Severity severity = getInspector().getSeverityStrategy().getSeverity(this, elementStyle);
        Violation violation = inspect(elementStyle);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected Violation inspect(ElementStyle elementStyle) {
        if (elementStyle.getMetadata() != null && !elementStyle.getMetadata()) {
            return violation("The element style for tag \"" + elementStyle.getTag() + "\" has metadata hidden, which may introduce ambiguity on rendered diagrams.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "views.styles.element.metadata";
    }

}