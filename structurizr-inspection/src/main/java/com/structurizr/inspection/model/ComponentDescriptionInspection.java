package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class ComponentDescriptionInspection extends ElementDescriptionInspection {

    public ComponentDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected String getType() {
        return "model.component.description";
    }

}