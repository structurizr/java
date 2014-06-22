package com.structurizr.view;

import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

public class ComponentView extends View {

    private Container container;

    public ComponentView(SoftwareSystem softwareSystem, Container container) {
        super(softwareSystem);

        this.container = container;
    }

    public long getContainerId() {
        return container.getId();
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
        getSoftwareSystem().getContainers().stream()
                .filter(c -> c != container)
                .forEach(this::addElement);
    }

    public void addAllComponents() {
        container.getComponents().forEach(this::addElement);
    }

    public void remove(Container container) {
        removeElement(container);
    }

    public void remove(Component component) {
        removeElement(component);
    }

    @Override
    public final ViewType getType() {
        return ViewType.Component;
    }

}