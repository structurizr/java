package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.model.Element;

abstract class ComponentInspection extends ElementInspection {

    public ComponentInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((Component)element);
    }

    protected abstract Violation inspect(Component component);

}