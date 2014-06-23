package com.structurizr.model;

import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.ContextView;
import com.structurizr.view.View;

import java.util.*;

public class Model {

    private long id = 1;

    private final Map<Long,Element> elementsById = new HashMap<>();

    private Set<Person> people = new HashSet<>();
    private Set<SoftwareSystem> softwareSystems = new HashSet<>();

    private Set<View> views = new HashSet<>();

    public SoftwareSystem createSoftwareSystem(Location location, String name, String description) {
        SoftwareSystem softwareSystem = new SoftwareSystem();
        softwareSystem.setLocation(location);
        softwareSystem.setName(name);
        softwareSystem.setDescription(description);
        softwareSystem.setId(getId());

        softwareSystems.add(softwareSystem);
        addElement(softwareSystem);

        return softwareSystem;
    }

    public Person createPerson(Location location, String name, String description) {
        Person person = new Person();
        person.setLocation(location);
        person.setName(name);
        person.setDescription(description);
        person.setId(getId());

        people.add(person);
        addElement(person);

        return person;
    }

    public Container createContainer(SoftwareSystem parent, String name, String description, String technology) {
        Container container = new Container();
        container.setName(name);
        container.setDescription(description);
        container.setTechnology(technology);
        container.setId(getId());

        parent.add(container);
        container.setParent(parent);
        addElement(container);

        return container;
    }

    public Component createComponentWithClass(Container parent, String fullyQualifiedClassName, String description) {
        Component component = new Component();
        component.setFullyQualifiedClassName(fullyQualifiedClassName);
        component.setDescription(description);
        component.setId(getId());

        parent.add(component);
        component.setParent(parent);
        addElement(component);

        return component;
    }

    private synchronized long getId() {
        return id++;
    }

    private void addElement(Element element) {
        elementsById.put(element.getId(), element);
        element.setModel(this);
    }

    public Element getElement(long id) {
        return elementsById.get(id);
    }

    public Collection<Person> getPeople() {
        return new HashSet<>(people);
    }

    public Set<SoftwareSystem> getSoftwareSystems() {
        return new HashSet<>(softwareSystems);
    }

    public void enrich() {
        people.forEach(this::addElement);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            addElement(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                addElement(container);
            }
        }

        people.forEach(this::enrichRelationships);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            enrichRelationships(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                enrichRelationships(container);
            }
        }

        for (Person person : people) {
            enrichRelationships(person);
        }
        for (SoftwareSystem softwareSystem : softwareSystems) {
            enrichRelationships(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                enrichRelationships(container);
            }
        }
    }

    private void enrichRelationships(Element element) {
        for (Relationship relationship : element.getRelationships()) {
            relationship.setSource(getElement(relationship.getSourceId()));
            relationship.setDestination(getElement(relationship.getDestinationId()));
        }
    }

    public boolean contains(Element element) {
        return elementsById.values().contains(element);
    }

    public ContextView createContextView(SoftwareSystem softwareSystem) {
        ContextView view = new ContextView(softwareSystem);
        views.add(view);

        return view;
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem) {
        ContainerView view = new ContainerView(softwareSystem);
        views.add(view);

        return view;
    }

    public ComponentView createComponentView(SoftwareSystem softwareSystem, Container container) {
        ComponentView view = new ComponentView(softwareSystem, container);
        views.add(view);

        return view;
    }

    public Set<View> getViews() {
        return new HashSet<>(views);
    }

    public SoftwareSystem getSoftwareSystemWithName(String name) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

}