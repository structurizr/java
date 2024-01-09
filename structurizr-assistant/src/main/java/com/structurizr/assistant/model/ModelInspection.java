package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;

abstract class ModelInspection extends Inspection {

    public ModelInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run() {
        if (isEnabled(getType(), getWorkspace(), getWorkspace().getModel())) {
            return inspect(getWorkspace());
        }

        return noRecommendation();
    }

    protected abstract Recommendation inspect(Workspace workspace);

}