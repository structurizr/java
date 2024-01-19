package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public class RelationshipTechnologyInspection extends AbstractRelationshipInspection {

    public RelationshipTechnologyInspection(Inspector inspector) {
        super(inspector);
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