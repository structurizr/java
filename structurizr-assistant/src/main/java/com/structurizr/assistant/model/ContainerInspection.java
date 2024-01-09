package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;

abstract class ContainerInspection extends ElementInspection {

    public ContainerInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Element element) {
        return inspect((Container)element);
    }

    protected abstract Recommendation inspect(Container container);

}