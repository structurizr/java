package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.ModelView;

public class EmptyViewInspection extends ModelViewInspection {

    public EmptyViewInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    public Recommendation inspect(ModelView view) {
        if (view.getElements().isEmpty()) {
            return highPriorityRecommendation("The view with key \"" + view.getKey() + "\" is empty - add some elements.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "views.view.empty";
    }

}