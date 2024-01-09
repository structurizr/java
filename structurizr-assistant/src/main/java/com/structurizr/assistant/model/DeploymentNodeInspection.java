package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;

abstract class DeploymentNodeInspection extends ElementInspection {

    public DeploymentNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Element element) {
        return inspect((DeploymentNode)element);
    }

    protected abstract Recommendation inspect(DeploymentNode deploymentNode);

}