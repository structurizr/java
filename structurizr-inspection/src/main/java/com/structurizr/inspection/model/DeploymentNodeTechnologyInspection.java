package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.util.StringUtils;

public class DeploymentNodeTechnologyInspection extends AbstractDeploymentNodeInspection {

    public DeploymentNodeTechnologyInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(DeploymentNode deploymentNode) {
        if (StringUtils.isNullOrEmpty(deploymentNode.getDescription())) {
            return violation("Add a technology to the " + terminologyFor(deploymentNode).toLowerCase() + " named \"" + deploymentNode.getName() + "\".");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.technology";
    }

}