package com.structurizr.view;

import com.structurizr.model.SoftwareSystem;

public class ContextView extends View {

    public ContextView() {
    }

    public ContextView(SoftwareSystem softwareSystem) {
        super(softwareSystem);
        addElement(softwareSystem);
    }

    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().forEach(this::addElement);
    }

    public void addAllPeople() {
        getModel().getPeople().forEach(this::addElement);
    }

    @Override
    public final ViewType getType() {
        return ViewType.Context;
    }

}