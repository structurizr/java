package com.structurizr.inspection;

import com.structurizr.PropertyHolder;
import com.structurizr.Workspace;

public abstract class Inspection {

    private static final String STRUCTURIZR_INSPECTION_PREFIX = "structurizr.inspection.";
    public static final String ALL_INSPECTIONS = "structurizr.inspection";

    private final Workspace workspace;
    private final Severity defaultSeverity;

    protected Inspection(Workspace workspace) {
        this.workspace = workspace;
        this.defaultSeverity = Severity.valueOf(workspace.getProperties().getOrDefault(ALL_INSPECTIONS, Severity.ERROR.toString()).toUpperCase());
    }

    protected abstract String getType();

    protected Workspace getWorkspace() {
        return workspace;
    }

    protected Severity getSeverity(String type, PropertyHolder... propertyHolders) {
        String value = defaultSeverity.toString();

        for (PropertyHolder propertyHolder : propertyHolders) {
            if (propertyHolder != null) {
                value = propertyHolder.getProperties().getOrDefault(STRUCTURIZR_INSPECTION_PREFIX + type, value);
            }
        }

        return Severity.valueOf(value.toUpperCase());
    }

    protected Violation noViolation() {
        return null;
    }

    protected Violation violation(String description) {
        return new Violation(STRUCTURIZR_INSPECTION_PREFIX + getType(), description);
    }

}