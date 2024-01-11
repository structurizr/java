package com.structurizr.assistant.view;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;
import com.structurizr.view.ModelView;

abstract class ModelViewInspection extends Inspection {

    public ModelViewInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run(ModelView view) {
        if (isEnabled(getType(), getWorkspace(), getWorkspace().getViews().getConfiguration(), view)) {
            return inspect(view);
        }

        return noRecommendation();
    }

    protected abstract Recommendation inspect(ModelView view);

}