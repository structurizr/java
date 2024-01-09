package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public class RelationshipTechnologyInspection extends RelationshipInspection {

    public RelationshipTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (StringUtils.isNullOrEmpty(relationship.getTechnology())) {
            if (source instanceof Container && destination instanceof Container) {
                return mediumPriorityRecommendation("Add a technology to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            } else {
                return lowPriorityRecommendation("Add a technology to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            }
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.relationship.technology";
    }

}