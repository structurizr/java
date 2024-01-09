package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;

public class EmptyModelInspection extends ModelInspection {

    public EmptyModelInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Workspace workspace) {
        if (workspace.getModel().isEmpty()) {
            return highPriorityRecommendation("Add some elements to the model.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.empty";
    }

}