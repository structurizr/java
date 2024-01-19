package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class DeploymentNodeDescriptionInspection extends ElementDescriptionInspection {

    public DeploymentNodeDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.description";
    }

}