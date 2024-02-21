package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;

public class SoftwareSystemDecisionsInspection extends AbstractSoftwareSystemInspection {

    public SoftwareSystemDecisionsInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(SoftwareSystem softwareSystem) {
        if (softwareSystem.hasContainers() && softwareSystem.getDocumentation().getDecisions().isEmpty()) {
            return violation("The " + terminologyFor(softwareSystem).toLowerCase() + " \"" + nameOf(softwareSystem) + "\" has containers, but is missing decisions.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.decisions";
    }

}