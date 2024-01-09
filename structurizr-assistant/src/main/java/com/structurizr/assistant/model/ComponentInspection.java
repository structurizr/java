package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Component;
import com.structurizr.model.Element;

abstract class ComponentInspection extends ElementInspection {

    public ComponentInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Element element) {
        return inspect((Component)element);
    }

    protected abstract Recommendation inspect(Component component);

}