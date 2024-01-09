package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.util.StringUtils;

public class DeploymentNodeTechnologyInspection extends DeploymentNodeInspection {

    public DeploymentNodeTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(DeploymentNode deploymentNode) {
        if (StringUtils.isNullOrEmpty(deploymentNode.getDescription())) {
            return mediumPriorityRecommendation("Add a technology to the " + terminologyFor(deploymentNode).toLowerCase() + " named \"" + deploymentNode.getName() + "\".");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.deploymentnode.technology";
    }

}