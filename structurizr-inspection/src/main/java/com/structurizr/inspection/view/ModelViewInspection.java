package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ModelView;

abstract class ModelViewInspection extends Inspection {

    public ModelViewInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run(ModelView view) {
        Severity severity = getSeverity(getWorkspace(), getWorkspace().getViews().getConfiguration(), view);
        Violation violation = inspect(view);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected abstract Violation inspect(ModelView view);

}