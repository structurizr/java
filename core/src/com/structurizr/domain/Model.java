package com.structurizr.domain;

import java.util.*;

public class Model {

    private long id = 1;

    private final Map<Long,Element> elements = new HashMap<>();

    private Set<Person> people = new HashSet<>();
    private Set<SoftwareSystem> softwareSystems = new HashSet<>();

    public SoftwareSystem createSoftwareSystem(String name, String description) {
        SoftwareSystem softwareSystem = new SoftwareSystem();
        softwareSystem.setName(name);
        softwareSystem.setDescription(description);
        softwareSystem.setId(getId());

        softwareSystems.add(softwareSystem);
        addElement(softwareSystem);

        return softwareSystem;
    }

    public Person createPerson(String name, String description) {
        Person person = new Person();
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
        addElement(container);

        return container;
    }

    public Component createComponentWithClass(Container parent, String fullyQualifiedClassName, String description) {
        Component component = new Component();
        component.setFullyQualifiedClassName(fullyQualifiedClassName);
        component.setDescription(description);
        component.setId(getId());

        parent.add(component);
        addElement(component);

        return component;
    }

    private synchronized long getId() {
        return id++;
    }

    private void addElement(Element element) {
        elements.put(element.getId(), element);
        element.setModel(this);
    }

    public Element getElement(long id) {
        return elements.get(id);
    }

    public Collection<Person> getPeople() {
        return new HashSet<>(people);
    }

    public Set<SoftwareSystem> getSoftwareSystems() {
        return softwareSystems;
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

}