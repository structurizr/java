package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.InfrastructureNode;

abstract class AbstractInfrastructureNodeInspection extends AbstractElementInspection {

    public AbstractInfrastructureNodeInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Element element) {
        return inspect((InfrastructureNode)element);
    }

    protected abstract Violation inspect(InfrastructureNode infrastructureNode);

}