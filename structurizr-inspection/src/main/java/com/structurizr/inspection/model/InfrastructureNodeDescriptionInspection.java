package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;

public class InfrastructureNodeDescriptionInspection extends ElementDescriptionInspection {

    public InfrastructureNodeDescriptionInspection(Inspector inspector) {
        super(inspector);
    }

    protected String getType() {
        return "model.infrastructurenode.description";
    }

}