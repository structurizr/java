package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;

abstract class DeploymentNodeInspection extends ElementInspection {

    public DeploymentNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Element element) {
        return inspect((DeploymentNode)element);
    }

    protected abstract Violation inspect(DeploymentNode deploymentNode);

}