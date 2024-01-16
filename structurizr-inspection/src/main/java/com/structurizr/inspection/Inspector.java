package com.structurizr.inspection;

import java.util.ArrayList;
import java.util.List;

abstract class Inspector {

    private final List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() {
        return new ArrayList<>(violations);
    }

    protected void add(Violation violation) {
        if (violation != null) {
            violations.add(violation);
        }
    }

}