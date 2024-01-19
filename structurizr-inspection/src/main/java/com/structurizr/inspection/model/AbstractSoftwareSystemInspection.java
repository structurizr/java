package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;

abstract class AbstractSoftwareSystemInspection extends AbstractElementInspection {

    public AbstractSoftwareSystemInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected final Violation inspect(Element element) {
        return inspect((SoftwareSystem)element);
    }

    protected abstract Violation inspect(SoftwareSystem softwareSystem);

}