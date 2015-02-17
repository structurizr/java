package com.structurizr.model;

import java.util.*;

/**
 * This is the starting point for creating a software architecture
 * model - everything is attached to an instance of this.
 */
public class Model {

    private SequentialIntegerIdGeneratorStrategy idGenerator = new SequentialIntegerIdGeneratorStrategy();

    private final Map<String,Element> elementsById = new HashMap<>();

    private Set<Person> people = new LinkedHashSet<>();
    private Set<SoftwareSystem> softwareSystems = new LinkedHashSet<>();

    public Model() {
    }

    /**
     * Creates a software system (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name          the name of the software system
     * @param description   a short description of the software system
     * @return  the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(String name, String description) {
        return addSoftwareSystem(name, description);
    }

    /**
     * Creates a software system and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location      the location of the software system (e.g. internal, external, etc)
     * @param name          the name of the software system
     * @param description   a short description of the software system
     * @return  the SoftwareSystem instance created and added to the model (or null)
     */
    public SoftwareSystem addSoftwareSystem(Location location, String name, String description) {
        if (getSoftwareSystemWithName(name) == null) {
            SoftwareSystem softwareSystem = new SoftwareSystem();
            softwareSystem.setLocation(location);
            softwareSystem.setName(name);
            softwareSystem.setDescription(description);

            softwareSystems.add(softwareSystem);

            softwareSystem.setId(idGenerator.generateId(softwareSystem));
            addElementToInternalStructures(softwareSystem);

            return softwareSystem;
        } else {
            return null;
        }
    }

    /**
     * Creates a person (location is unspecified) and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param name          the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description   a short description of the person
     * @return  the Person instance created and added to the model (or null)
     */
    public Person addPerson(String name, String description) {
        return addPerson(Location.Unspecified, name, description);
    }

    /**
     * Creates a person and adds it to the model
     * (unless one exists with the same name already).
     *
     * @param location      the location of the person (e.g. internal, external, etc)
     * @param name          the name of the person (e.g. "Admin User" or "Bob the Business User")
     * @param description   a short description of the person
     * @return  the Person instance created and added to the model (or null)
     */
    public Person addPerson(Location location, String name, String description) {
        if (getPersonWithName(name) == null) {
            Person person = new Person();
            person.setLocation(location);
            person.setName(name);
            person.setDescription(description);

            people.add(person);

            person.setId(idGenerator.generateId(person));
            addElementToInternalStructures(person);

            return person;
        } else {
            return null;
        }
    }

    Container addContainer(SoftwareSystem parent, String name, String description, String technology) {
        if (parent.getContainerWithName(name) == null) {
            Container container = new Container();
            container.setName(name);
            container.setDescription(description);
            container.setTechnology(technology);

            container.setParent(parent);
            parent.add(container);

            container.setId(idGenerator.generateId(container));
            addElementToInternalStructures(container);

            return container;
        } else {
            return null;
        }
    }

    Component addComponentOfType(Container parent, String interfaceType, String implementationType, String description) {
        Component component = new Component();
        component.setInterfaceType(interfaceType);
        component.setImplementationType(implementationType);
        component.setDescription(description);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        addElementToInternalStructures(component);

        return component;
    }

    Component addComponent(Container parent, String name, String description) {
        Component component = new Component();
        component.setName(name);
        component.setDescription(description);

        component.setParent(parent);
        parent.add(component);

        component.setId(idGenerator.generateId(component));
        addElementToInternalStructures(component);

        return component;
    }

    void addRelationship(Relationship relationship) {
        if (!relationship.getSource().has(relationship)) {
            relationship.setId(idGenerator.generateId(relationship));
            relationship.getSource().addRelationship(relationship);
        }
    }

    private void addElementToInternalStructures(Element element) {
        elementsById.put(element.getId(), element);
        element.setModel(this);
        idGenerator.found(element.getId());
    }

    private void addRelationshipToInternalStructures(Relationship relationship) {
        idGenerator.found(relationship.getId());
    }

    /**
     * Returns the element in this model with the specified ID
     * (or null if it doesn't exist).
     */
    public Element getElement(String id) {
        return elementsById.get(id);
    }

    /**
     * Gets a collection containing all of the Person instances in this model.
     */
    public Collection<Person> getPeople() {
        return new LinkedHashSet<>(people);
    }

    /**
     * Gets a collection containing all of the SoftwareSystem instances in this model.
     */
    public Set<SoftwareSystem> getSoftwareSystems() {
        return new LinkedHashSet<>(softwareSystems);
    }

    public void hydrate() {
        // add all of the elements to the model
        people.forEach(this::addElementToInternalStructures);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            addElementToInternalStructures(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                softwareSystem.add(container);
                addElementToInternalStructures(container);
                container.setParent(softwareSystem);
                for (Component component : container.getComponents()) {
                    container.add(component);
                    addElementToInternalStructures(component);
                    component.setParent(container);
                }
            }
        }

        // now hydrate the relationships
        people.forEach(this::hydrateRelationships);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            hydrateRelationships(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                hydrateRelationships(container);
                for (Component component : container.getComponents()) {
                    hydrateRelationships(component);
                }
            }
        }
    }

    private void hydrateRelationships(Element element) {
        for (Relationship relationship : element.getRelationships()) {
            relationship.setSource(getElement(relationship.getSourceId()));
            relationship.setDestination(getElement(relationship.getDestinationId()));
            addRelationshipToInternalStructures(relationship);
        }
    }

    /**
     * Determines whether this model contains the specified element.
     */
    public boolean contains(Element element) {
        return elementsById.values().contains(element);
    }

    /**
     * Gets the SoftwareSystem instance with the specified name
     * (or null if it doesn't exist).
     */
    public SoftwareSystem getSoftwareSystemWithName(String name) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * Gets the SoftwareSystem instance with the specified ID
     * (or null if it doesn't exist).
     */
    public SoftwareSystem getSoftwareSystemWithId(String id) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getId().equals(id)) {
                return softwareSystem;
            }
        }

        return null;
    }

    /**
     * Gets the Person instance with the specified name
     * (or null if it doesn't exist).
     */
    public Person getPersonWithName(String name) {
        for (Person person : getPeople()) {
            if (person.getName().equals(name)) {
                return person;
            }
        }

        return null;
    }

}