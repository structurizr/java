package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.util.StringUtils;

abstract class ElementDescriptionInspection extends ElementInspection {

    public ElementDescriptionInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Element element) {
        if (StringUtils.isNullOrEmpty(element.getDescription())) {
            return violation("Add a description to the " + terminologyFor(element).toLowerCase() + " named \"" + element.getName() + "\".");
        }

        return noViolation();
    }

}