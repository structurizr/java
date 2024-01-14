package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.util.StringUtils;

public class DeploymentNodeTechnologyInspection extends DeploymentNodeInspection {

    public DeploymentNodeTechnologyInspection(Workspace workspace) {
        super(workspace);
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