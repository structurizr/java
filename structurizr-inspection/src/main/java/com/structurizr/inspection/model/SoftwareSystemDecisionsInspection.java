package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;

public class SoftwareSystemDecisionsInspection extends SoftwareSystemInspection {

    public SoftwareSystemDecisionsInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(SoftwareSystem softwareSystem) {
        if (softwareSystem.hasContainers() && softwareSystem.getDocumentation().getDecisions().isEmpty()) {
            return violation("The " + terminologyFor(softwareSystem).toLowerCase() + " named \"" + softwareSystem.getName() + "\" has containers, but is missing decisions.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.decisions";
    }

}