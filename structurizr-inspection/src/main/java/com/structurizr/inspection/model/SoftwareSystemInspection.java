package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;

abstract class SoftwareSystemInspection extends ElementInspection {

    public SoftwareSystemInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((SoftwareSystem)element);
    }

    protected abstract Violation inspect(SoftwareSystem softwareSystem);

}