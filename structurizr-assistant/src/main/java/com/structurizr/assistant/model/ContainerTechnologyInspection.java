package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.util.StringUtils;

public class ContainerTechnologyInspection extends ContainerInspection {

    public ContainerTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Container container) {
        if (StringUtils.isNullOrEmpty(container.getTechnology())) {
            return mediumPriorityRecommendation("Add a technology to the " + terminologyFor(container).toLowerCase() + " named \"" + container.getName() + "\".");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.container.technology";
    }

}