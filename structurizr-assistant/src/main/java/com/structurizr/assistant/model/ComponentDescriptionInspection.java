package com.structurizr.assistant.model;

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