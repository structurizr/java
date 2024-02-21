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
            return violation("The relationship between the " + terminologyFor(source) + " \"" + nameOf(source) + "\" and the " + terminologyFor(destination) + " \"" + nameOf(destination) + "\" is missing a description.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.relationship.description";
    }

}