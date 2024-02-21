package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.util.StringUtils;

public class ContainerTechnologyInspection extends AbstractContainerInspection {

    public ContainerTechnologyInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Container container) {
        if (StringUtils.isNullOrEmpty(container.getTechnology())) {
            return violation("The " + terminologyFor(container).toLowerCase() + " \"" + nameOf(container) + "\" is missing a technology.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.container.technology";
    }

}