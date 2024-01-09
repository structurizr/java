package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Component;
import com.structurizr.util.StringUtils;

public class ComponentTechnologyInspection extends ComponentInspection {

    public ComponentTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Component component) {
        if (StringUtils.isNullOrEmpty(component.getDescription())) {
            return lowPriorityRecommendation("Add a technology to the " + terminologyFor(component).toLowerCase() + " named \"" + component.getName() + "\".");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.component.technology";
    }

}