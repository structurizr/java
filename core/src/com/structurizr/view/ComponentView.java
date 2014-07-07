package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.SoftwareSystem;

import java.util.Collection;

public class ComponentView extends View {

    private Container container;
    private int containerId;

    public ComponentView() {
    }

    public ComponentView(SoftwareSystem softwareSystem, Container container) {
        super(softwareSystem);

        this.container = container;
    }

    public int getContainerId() {
        if (this.container != null) {
            return container.getId();
        } else {
            return this.containerId;
        }
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
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

    public void add(Container container) {
        addElement(container);
    }

    public void add(Component component) {
        addElement(component);
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

    public void remove(Collection<Component> components) {
        components.forEach(this::removeElement);
    }

    @Override
    public final ViewType getType() {
        return ViewType.Component;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - " + getContainer().getName() + " - Components";
    }

}