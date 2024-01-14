package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;

public class EmptyDeploymentNodeInspection extends DeploymentNodeInspection {

    public EmptyDeploymentNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(DeploymentNode deploymentNode) {
        if (!deploymentNode.hasChildren() && !deploymentNode.hasSoftwareSystemInstances() && !deploymentNode.hasContainerInstances() && !deploymentNode.hasInfrastructureNodes()) {
            return violation("The " + terminologyFor(deploymentNode).toLowerCase() + " named \"" + deploymentNode.getName() + "\" is empty.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.empty";
    }

}