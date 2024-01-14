package com.structurizr.inspection.model;

import com.structurizr.Workspace;

public class InfrastructureNodeDescriptionInspection extends ElementDescriptionInspection {

    public InfrastructureNodeDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    protected String getType() {
        return "model.infrastructurenode.description";
    }

}