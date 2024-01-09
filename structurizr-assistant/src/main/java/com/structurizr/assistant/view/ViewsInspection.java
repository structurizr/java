package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;

abstract class ViewsInspection extends Inspection {

    public ViewsInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run() {
        if (isEnabled(getType(), getWorkspace(), getWorkspace().getViews().getConfiguration())) {
            return inspect(getWorkspace());
        }

        return noRecommendation();
    }

    protected abstract Recommendation inspect(Workspace workspace);

}