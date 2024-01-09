package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Component;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public class RelationshipDescriptionInspection extends RelationshipInspection {

    public RelationshipDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (StringUtils.isNullOrEmpty(relationship.getDescription())) {
            if (source instanceof Component && destination instanceof Component) {
                return lowPriorityRecommendation("Add a description to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            } else {
                return mediumPriorityRecommendation("Add a description to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            }
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.relationship.description";
    }

}