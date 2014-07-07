package com.structurizr.view;

import com.structurizr.model.SoftwareSystem;

public class ContainerView extends View {

    public ContainerView() {
    }

    public ContainerView(SoftwareSystem softwareSystem) {
        super(softwareSystem);
    }

    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().stream()
                .filter(ss -> ss != getSoftwareSystem())
                .forEach(this::addElement);
    }

    public void addAllPeople() {
        getModel().getPeople().forEach(this::addElement);
    }

    public void addAllContainers() {
        getSoftwareSystem().getContainers().forEach(this::addElement);
    }

    @Override
    public final ViewType getType() {
        return ViewType.Container;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Containers";
    }

}