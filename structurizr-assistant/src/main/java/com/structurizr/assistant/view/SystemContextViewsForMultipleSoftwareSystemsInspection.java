package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.SystemContextView;

import java.util.HashSet;
import java.util.Set;

public class SystemContextViewsForMultipleSoftwareSystemsInspection extends ViewsInspection {

    public SystemContextViewsForMultipleSoftwareSystemsInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(Workspace workspace) {
        Set<String> softwareSystemsWithSystemContextViews = new HashSet<>();
        for (SystemContextView view : workspace.getViews().getSystemContextViews()) {
            softwareSystemsWithSystemContextViews.add(view.getSoftwareSystemId());
        }
        if (softwareSystemsWithSystemContextViews.size() > 1) {
            return highPriorityRecommendation("System context views exist for " + softwareSystemsWithSystemContextViews.size() + " software systems. It is recommended that a workspace includes system context views for a single software system only.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}