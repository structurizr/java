package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;

abstract class SoftwareSystemInspection extends ElementInspection {

    public SoftwareSystemInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected final Recommendation inspect(Element element) {
        return inspect((SoftwareSystem)element);
    }

    protected abstract Recommendation inspect(SoftwareSystem softwareSystem);

}