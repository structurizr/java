package com.structurizr.assistant.model;

import com.structurizr.Workspace;

public class ContainerDescriptionInspection extends ElementDescriptionInspection {

    public ContainerDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected String getType() {
        return "model.container.description";
    }

}