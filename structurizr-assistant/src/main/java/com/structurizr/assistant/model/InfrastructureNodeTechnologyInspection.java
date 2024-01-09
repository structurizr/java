package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.InfrastructureNode;
import com.structurizr.util.StringUtils;

public class InfrastructureNodeTechnologyInspection extends InfrastructureNodeInspection {

    public InfrastructureNodeTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(InfrastructureNode infrastructureNode) {
        if (StringUtils.isNullOrEmpty(infrastructureNode.getTechnology())) {
            return mediumPriorityRecommendation("Add a technology to the " + terminologyFor(infrastructureNode).toLowerCase() + " named \"" + infrastructureNode.getName() + "\".");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.infrastructurenode.technology";
    }

}