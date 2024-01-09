package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.DeploymentNode;

public class EmptyDeploymentNodeInspection extends DeploymentNodeInspection {

    public EmptyDeploymentNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(DeploymentNode deploymentNode) {
        if (!deploymentNode.hasChildren() && !deploymentNode.hasSoftwareSystemInstances() && !deploymentNode.hasContainerInstances() && !deploymentNode.hasInfrastructureNodes()) {
            return lowPriorityRecommendation("The " + terminologyFor(deploymentNode).toLowerCase() + " named \"" + deploymentNode.getName() + "\" is empty.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.empty";
    }

}