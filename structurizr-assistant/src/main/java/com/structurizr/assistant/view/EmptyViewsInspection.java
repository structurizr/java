package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;

public class EmptyViewsInspection extends ViewsInspection {

    public EmptyViewsInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(Workspace workspace) {
        if (workspace.getViews().isEmpty()) {
            return highPriorityRecommendation("Add some views to the workspace.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "views.empty";
    }

}