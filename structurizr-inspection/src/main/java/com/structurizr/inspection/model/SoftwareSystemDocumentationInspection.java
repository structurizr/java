package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.SoftwareSystem;

public class SoftwareSystemDocumentationInspection extends SoftwareSystemInspection {

    public SoftwareSystemDocumentationInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(SoftwareSystem softwareSystem) {
        if (softwareSystem.hasContainers() && softwareSystem.getDocumentation().getSections().isEmpty()) {
            return violation("The " + terminologyFor(softwareSystem).toLowerCase() + " named \"" + softwareSystem.getName() + "\" has containers, but is missing documentation.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.documentation";
    }

}