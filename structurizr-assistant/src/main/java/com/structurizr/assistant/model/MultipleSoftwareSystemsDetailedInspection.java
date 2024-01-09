package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.SoftwareSystem;

public class MultipleSoftwareSystemsDetailedInspection extends ModelInspection {

    public MultipleSoftwareSystemsDetailedInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Workspace workspace) {
        int softwareSystemsWithDetails = 0;
        for (SoftwareSystem softwareSystem : workspace.getModel().getSoftwareSystems()) {
            if (softwareSystem.hasContainers() || !softwareSystem.getDocumentation().isEmpty()) {
                softwareSystemsWithDetails++;
            }
        }

        if (softwareSystemsWithDetails > 1) {
            return highPriorityRecommendation("This workspace describes the internal details of " + softwareSystemsWithDetails + " software systems. It is recommended that a workspace contains the model, views, and documentation for a single software system only.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}