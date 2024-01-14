package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;

abstract class ContainerInspection extends ElementInspection {

    public ContainerInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((Container)element);
    }

    protected abstract Violation inspect(Container container);

}