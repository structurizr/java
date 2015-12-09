package com.structurizr.view;

import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

public class ContainerView extends StaticView {

    ContainerView() {
    }

    ContainerView(SoftwareSystem softwareSystem, String description) {
        super(softwareSystem, description);
    }

    /**
     * Adds all software systems in the model to this view.
     */
    @Override
    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().stream()
                .filter(ss -> ss != getSoftwareSystem())
                .forEach(this::add);
    }

    /**
     * Adds all containers in the software system to this view.
     */
    public void addAllContainers() {
        getSoftwareSystem().getContainers().forEach(this::add);
    }

    /**
     * Adds an individual container to this view.
     *
     * @param container     the Container to add
     */
    public void add(Container container) {
        addElement(container, true);
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Containers";
    }

    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
    }

    @Override
    public void addNearestNeighbours(Element element) {
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, Container.class);
    }

}