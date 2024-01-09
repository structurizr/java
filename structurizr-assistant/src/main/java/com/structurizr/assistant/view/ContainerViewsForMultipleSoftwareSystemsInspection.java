package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.ContainerView;

import java.util.HashSet;
import java.util.Set;

public class ContainerViewsForMultipleSoftwareSystemsInspection extends ViewsInspection {

    public ContainerViewsForMultipleSoftwareSystemsInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(Workspace workspace) {
        Set<String> softwareSystemsWithContainerViews = new HashSet<>();
        for (ContainerView view : workspace.getViews().getContainerViews()) {
            softwareSystemsWithContainerViews.add(view.getSoftwareSystemId());
        }
        if (softwareSystemsWithContainerViews.size() > 1) {
            return highPriorityRecommendation("Container views exist for " + softwareSystemsWithContainerViews.size() + " software systems. It is recommended that a workspace includes container views for a single software system only.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}