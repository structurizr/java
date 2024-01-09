package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;
import com.structurizr.model.InfrastructureNode;

abstract class InfrastructureNodeInspection extends ElementInspection {

    public InfrastructureNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Recommendation inspect(Element element) {
        return inspect((InfrastructureNode)element);
    }

    protected abstract Recommendation inspect(InfrastructureNode infrastructureNode);

}