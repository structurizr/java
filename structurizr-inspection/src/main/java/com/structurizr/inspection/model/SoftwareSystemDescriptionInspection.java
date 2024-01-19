package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class SoftwareSystemDescriptionInspection extends ElementDescriptionInspection {

    public SoftwareSystemDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected String getType() {
        return "model.softwaresystem.description";
    }

}