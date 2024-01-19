package com.structurizr.inspection;

import com.structurizr.Workspace;

import java.util.ArrayList;
import java.util.List;

public abstract class Inspector {

    private final Workspace workspace;

    private final List<Violation> violations = new ArrayList<>();

    protected Inspector(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public List<Violation> getViolations() {
        return new ArrayList<>(violations);
    }

    protected void add(Violation violation) {
        if (violation != null) {
            violations.add(violation);
        }
    }

    public abstract SeverityStrategy getSeverityStrategy();

}