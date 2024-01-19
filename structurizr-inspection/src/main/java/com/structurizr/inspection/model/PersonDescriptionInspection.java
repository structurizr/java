package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class PersonDescriptionInspection extends ElementDescriptionInspection {

    public PersonDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected String getType() {
        return "model.person.description";
    }

}