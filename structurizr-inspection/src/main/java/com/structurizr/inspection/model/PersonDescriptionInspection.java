package com.structurizr.inspection.model;

import com.structurizr.Workspace;

public class PersonDescriptionInspection extends ElementDescriptionInspection {

    public PersonDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected String getType() {
        return "model.person.description";
    }

}