package com.structurizr.inspection.view;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ModelView;
import com.structurizr.view.View;

public class GeneratedKeyInspection extends AbstractViewInspection {

    public GeneratedKeyInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(View view) {
        if (view.isGeneratedKey()) {
            return violation("The view with key \"" + view.getKey() + "\" has an automatically generated view key and this is not guaranteed to be stable over time.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "views.view.key";
    }

}