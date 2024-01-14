package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;

public class EmptyModelInspection extends ModelInspection {

    public EmptyModelInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Workspace workspace) {
        if (workspace.getModel().isEmpty()) {
            return violation("Add some elements to the model.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.empty";
    }

}