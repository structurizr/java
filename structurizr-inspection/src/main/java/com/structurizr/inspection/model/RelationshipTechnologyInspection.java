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
            return violation("The relationship between the " + terminologyFor(source) + " \"" + nameOf(source) + "\" and the " + terminologyFor(destination) + " \"" + nameOf(destination) + "\" is missing a technology.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.relationship.technology";
    }

}