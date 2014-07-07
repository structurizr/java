package com.structurizr.view;

import com.structurizr.model.SoftwareSystem;

public class SystemContextView extends View {

    public SystemContextView() {
    }

    public SystemContextView(SoftwareSystem softwareSystem) {
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
        return ViewType.SystemContext;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - System Context";
    }

}