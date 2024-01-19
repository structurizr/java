package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;

abstract class AbstractDeploymentNodeInspection extends AbstractElementInspection {

    public AbstractDeploymentNodeInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Element element) {
        return inspect((DeploymentNode)element);
    }

    protected abstract Violation inspect(DeploymentNode deploymentNode);

}