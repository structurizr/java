package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.util.StringUtils;

public class ComponentTechnologyInspection extends AbstractComponentInspection {

    public ComponentTechnologyInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Component component) {
        if (StringUtils.isNullOrEmpty(component.getDescription())) {
            return violation("Add a technology to the " + terminologyFor(component).toLowerCase() + " named \"" + component.getName() + "\".");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.component.technology";
    }

}