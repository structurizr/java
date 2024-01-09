package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;
import com.structurizr.util.StringUtils;

abstract class ElementDescriptionInspection extends ElementInspection {

    public ElementDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Element element) {
        if (StringUtils.isNullOrEmpty(element.getDescription())) {
            return mediumPriorityRecommendation("Add a description to the " + terminologyFor(element).toLowerCase() + " named \"" + element.getName() + "\".");
        }

        return noRecommendation();
    }

}