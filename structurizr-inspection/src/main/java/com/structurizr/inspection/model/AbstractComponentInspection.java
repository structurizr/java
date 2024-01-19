package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Component;
import com.structurizr.model.Element;

abstract class AbstractComponentInspection extends AbstractElementInspection {

    public AbstractComponentInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((Component)element);
    }

    protected abstract Violation inspect(Component component);

}