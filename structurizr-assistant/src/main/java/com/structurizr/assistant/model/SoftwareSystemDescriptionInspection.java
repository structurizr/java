package com.structurizr.assistant.model;

import com.structurizr.Workspace;

public class SoftwareSystemDescriptionInspection extends ElementDescriptionInspection {

    public SoftwareSystemDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.description";
    }

}