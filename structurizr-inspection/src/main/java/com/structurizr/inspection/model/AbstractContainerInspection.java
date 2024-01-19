package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Container;
import com.structurizr.model.Element;

abstract class AbstractContainerInspection extends AbstractElementInspection {

    public AbstractContainerInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((Container)element);
    }

    protected abstract Violation inspect(Container container);

}