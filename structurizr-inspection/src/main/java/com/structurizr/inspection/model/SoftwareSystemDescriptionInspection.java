package com.structurizr.inspection.model;

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