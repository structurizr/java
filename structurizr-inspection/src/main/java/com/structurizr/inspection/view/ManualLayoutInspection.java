package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ModelView;

public class ManualLayoutInspection extends AbstractModelViewInspection {

    public ManualLayoutInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    public Violation inspect(ModelView view) {
        if (view.isGeneratedKey() && view.getAutomaticLayout() == null) {
            return violation("The view with key \"" + view.getKey() + "\" has an automatically generated view key, which may cause manual layout information to be lost in the future.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "views.view.layout";
    }

}