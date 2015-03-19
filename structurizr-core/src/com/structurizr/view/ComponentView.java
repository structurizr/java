package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.Collection;

public class ComponentView extends View {

    private Container container;
    private String containerId;

    ComponentView() {
    }

    ComponentView(Container container, String description) {
        super(container.getParent(), description);

        this.container = container;
    }

    public String getContainerId() {
        if (this.container != null) {
            return container.getId();
        } else {
            return this.containerId;
        }
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    /**
     * Adds all software systems in the model to this view.
     */
    @Override
    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().stream()
                .filter(ss -> ss != getSoftwareSystem())
                .forEach(this::addElement);
    }

    /**
     * Adds all containers in the software system to this view.
     */
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

    /**
     * Adds all components in the container to this view.
     */
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

    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
        addAllComponents();

        // Components of the current container should always be included in the ComponentView
        removeElementsThatCantBeReachedFrom(this.container, this.container.getComponents());
    }
}