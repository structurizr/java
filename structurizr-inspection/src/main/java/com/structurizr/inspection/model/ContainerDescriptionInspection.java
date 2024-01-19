package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class ContainerDescriptionInspection extends ElementDescriptionInspection {

    public ContainerDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected String getType() {
        return "model.container.description";
    }

}