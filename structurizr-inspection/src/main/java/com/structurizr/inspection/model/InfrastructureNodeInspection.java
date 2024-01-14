package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.InfrastructureNode;

abstract class InfrastructureNodeInspection extends ElementInspection {

    public InfrastructureNodeInspection(Workspace workspace) {
        super(workspace);
    }

    @Override
    protected Violation inspect(Element element) {
        return inspect((InfrastructureNode)element);
    }

    protected abstract Violation inspect(InfrastructureNode infrastructureNode);

}