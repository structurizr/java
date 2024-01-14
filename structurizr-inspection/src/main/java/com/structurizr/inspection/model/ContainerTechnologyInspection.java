package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.util.StringUtils;

public class ContainerTechnologyInspection extends ContainerInspection {

    public ContainerTechnologyInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Container container) {
        if (StringUtils.isNullOrEmpty(container.getTechnology())) {
            return violation("Add a technology to the " + terminologyFor(container).toLowerCase() + " named \"" + container.getName() + "\".");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.container.technology";
    }

}