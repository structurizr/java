package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;

public class SoftwareSystemDocumentationInspection extends SoftwareSystemInspection {

    public SoftwareSystemDocumentationInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(SoftwareSystem softwareSystem) {
        if (softwareSystem.hasContainers() && softwareSystem.getDocumentation().getSections().isEmpty()) {
            return highPriorityRecommendation("The " + terminologyFor(softwareSystem).toLowerCase() + " named \"" + softwareSystem.getName() + "\" has containers, but is missing documentation.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.documentation";
    }

}