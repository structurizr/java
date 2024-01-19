package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ModelView;

public class EmptyViewInspection extends AbstractModelViewInspection {

    public EmptyViewInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    public Violation inspect(ModelView view) {
        if (view.getElements().isEmpty()) {
            return violation("The view with key \"" + view.getKey() + "\" is empty - add some elements.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "views.view.empty";
    }

}