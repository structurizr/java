package com.structurizr.inspection;

import com.structurizr.Workspace;

import java.util.ArrayList;
import java.util.List;

public abstract class Inspector {

    private final Workspace workspace;

    private final List<Violation> violations = new ArrayList<>();
    private int numberOfInspections = 0;

    protected Inspector(Workspace workspace) {
        this.workspace = workspace;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public List<Violation> getViolations() {
        return new ArrayList<>(violations);
    }

    public int getNumberOfInspections() {
        return numberOfInspections;
    }

    protected void add(Violation violation) {
        numberOfInspections++;

        if (violation != null) {
            violations.add(violation);
        }
    }

    public abstract SeverityStrategy getSeverityStrategy();

}