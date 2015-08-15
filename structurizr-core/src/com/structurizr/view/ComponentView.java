package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.ElementType;

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

    void setContainerId(String containerId) {
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

    /**
     * Adds all components in the container to this view.
     */
    public void addAllComponents() {
        container.getComponents().forEach(this::addElement);
    }

    /**
     * Adds an individual container to this view.
     *
     * @param container     the Container to add
     */
    public void add(Container container) {
        addElement(container);
    }

    /**
     * Adds an individual component to this view.
     *
     * @param component     the Component to add
     */
    public void add(Component component) {
        addElement(component);
    }

    /**
     * Removes an individual container from this view.
     *
     * @param container     the Container to remove
     */
    public void remove(Container container) {
        removeElement(container);
    }

    /**
     * Removes an individual component from this view.
     *
     * @param component     the Component to remove
     */
    public void remove(Component component) {
        removeElement(component);
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
    }

    @Override
    public void addNearestNeighbours(Element element) {
        super.addNearestNeighbours(element, ElementType.SoftwareSystem);
        super.addNearestNeighbours(element, ElementType.Person);
        super.addNearestNeighbours(element, ElementType.Container);
        super.addNearestNeighbours(element, ElementType.Component);
    }

}