package com.structurizr.inspection;

import com.structurizr.Workspace;

public abstract class Inspection {

    private final Inspector inspector;

    protected Inspection(Inspector inspector) {
        this.inspector = inspector;
    }

    protected abstract String getType();

    public Inspector getInspector() {
        return inspector;
    }

    protected Workspace getWorkspace() {
        return inspector.getWorkspace();
    }

    protected Violation noViolation() {
        return null;
    }

    protected Violation violation(String description) {
        return new Violation(this, description);
    }

}