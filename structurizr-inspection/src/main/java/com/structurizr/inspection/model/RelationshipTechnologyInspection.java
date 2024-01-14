package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public class RelationshipTechnologyInspection extends RelationshipInspection {

    public RelationshipTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (StringUtils.isNullOrEmpty(relationship.getTechnology())) {
            if (source instanceof Container && destination instanceof Container) {
                return violation("Add a technology to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            } else {
                return violation("Add a technology to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            }
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.relationship.technology";
    }

}