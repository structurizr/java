package com.structurizr.inspection.model;

import com.structurizr.Workspace;

public class DeploymentNodeDescriptionInspection extends ElementDescriptionInspection {

    public DeploymentNodeDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.description";
    }

}