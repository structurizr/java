package com.structurizr.inspection.model;

import com.structurizr.Workspace;

public class ComponentDescriptionInspection extends ElementDescriptionInspection {

    public ComponentDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected String getType() {
        return "model.component.description";
    }

}