package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public class RelationshipDescriptionInspection extends AbstractRelationshipInspection {

    public RelationshipDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (StringUtils.isNullOrEmpty(relationship.getDescription())) {
            if (source instanceof Component && destination instanceof Component) {
                return violation("Add a description to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            } else {
                return violation("Add a description to the relationship between the " + terminologyFor(source) + " named \"" + source.getName() + "\" and the " + terminologyFor(destination) + " named \"" + destination.getName() + "\".");
            }
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.relationship.description";
    }

}