package com.structurizr.inspection;

import com.structurizr.PropertyHolder;
import com.structurizr.Workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Inspection {

    private static final String STRUCTURIZR_INSPECTION_PREFIX = "structurizr.inspection.";

    private final Workspace workspace;

    protected Inspection(Workspace workspace) {
        this.workspace = workspace;
    }

    protected abstract String getType();

    protected Workspace getWorkspace() {
        return workspace;
    }

    protected Severity getSeverity(PropertyHolder... propertyHolders) {
        List<String> types = generateTypes();

        for (String type : types) {
            for (PropertyHolder propertyHolder : propertyHolders) {
                if (propertyHolder != null) {
                    if (propertyHolder.getProperties().containsKey(type)) {
                        return Severity.valueOf(propertyHolder.getProperties().get(type).toUpperCase());
                    }
                }
            }
        }

        return Severity.ERROR;
    }

    protected Violation noViolation() {
        return null;
    }

    protected Violation violation(String description) {
        return new Violation(STRUCTURIZR_INSPECTION_PREFIX + getType(), description);
    }

    List<String> generateTypes() {
        // example:
        // structurizr.inspection.model.component.description
        // structurizr.inspection.model.component.*
        // structurizr.inspection.model.*
        // structurizr.inspection.*

        List<String> types = new ArrayList<>();

        String[] parts = getType().split("\\.");
        String buf = STRUCTURIZR_INSPECTION_PREFIX;
        types.add(buf + "*");
        for (int i = 0; i < parts.length-1; i++) {
            buf = buf + parts[i] + ".";
            types.add(buf + "*");
        }

        types.add(STRUCTURIZR_INSPECTION_PREFIX + getType());
        Collections.reverse(types);

        return types;
    }

}