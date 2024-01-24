package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;

public class EmptyModelInspection extends AbstractModelInspection {

    public EmptyModelInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Workspace workspace) {
        if (workspace.getModel().isEmpty()) {
            return violation("The model is empty.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.empty";
    }

}