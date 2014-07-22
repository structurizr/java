package com.structurizr.model;

import com.structurizr.view.*;

import java.util.*;

public class Model {

    private final Map<Integer,Element> elementsById = new HashMap<>();

    private Set<Person> people = new HashSet<>();
    private Set<SoftwareSystem> softwareSystems = new HashSet<>();

    private Set<SystemContextView> systemContextViews = new TreeSet<>();
    private Set<ContainerView> containerViews = new TreeSet<>();
    private Set<ComponentView> componentViews = new TreeSet<>();

    private long id;
    private String name;
    private String description;

    public Model() {
    }

    public Model (String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SoftwareSystem addSoftwareSystem(Location location, String name, String description) {
        SoftwareSystem softwareSystem = new SoftwareSystem();
        softwareSystem.setLocation(location);
        softwareSystem.setName(name);
        softwareSystem.setDescription(description);
        softwareSystem.setId(generateId());

        softwareSystems.add(softwareSystem);
        addElement(softwareSystem);

        return softwareSystem;
    }

    public Person addPerson(Location location, String name, String description) {
        Person person = new Person();
        person.setLocation(location);
        person.setName(name);
        person.setDescription(description);
        person.setId(generateId());

        people.add(person);
        addElement(person);

        return person;
    }

    Container addContainer(SoftwareSystem parent, String name, String description, String technology) {
        Container container = new Container();
        container.setName(name);
        container.setDescription(description);
        container.setTechnology(technology);
        container.setId(generateId());

        parent.add(container);
        container.setParent(parent);
        addElement(container);

        return container;
    }

    Component addComponentOfType(Container parent, String interfaceType, String implementationType, String description) {
        Component component = new Component();
        component.setInterfaceType(interfaceType);
        component.setImplementationType(implementationType);
        component.setDescription(description);
        component.setId(generateId());

        parent.add(component);
        component.setParent(parent);
        addElement(component);

        return component;
    }

    Component addComponent(Container parent, String name, String description) {
        Component component = new Component();
        component.setName(name);
        component.setDescription(description);

        parent.add(component);
        component.setParent(parent);
        addElement(component);

        return component;
    }

    private synchronized int generateId() {
        int id = elementsById.keySet().stream().reduce(0, Integer::max);

        return ++id;
    }

    private void addElement(Element element) {
        elementsById.put(element.getId(), element);
        element.setModel(this);
    }

    public Element getElement(int id) {
        return elementsById.get(id);
    }

    public Collection<Person> getPeople() {
        return new HashSet<>(people);
    }

    public Set<SoftwareSystem> getSoftwareSystems() {
        return new HashSet<>(softwareSystems);
    }

    public void hydrate() {
        // add all of the elements to the model
        people.forEach(this::addElement);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            addElement(softwareSystem);
            for (Container container : softwareSystem.getContainers()) {
                softwareSystem.add(container);
                addElement(container);
                container.setParent(softwareSystem);
                for (Component component : container.getComponents()) {
                    container.add(component);
                    addElement(component);
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

        systemContextViews.forEach(this::hydrateView);
        containerViews.forEach(this::hydrateView);
        componentViews.forEach(this::hydrateView);
        for (ComponentView view : componentViews) {
            hydrateView(view);
            view.setContainer(view.getSoftwareSystem().getContainerWithId(view.getContainerId()));
        }
    }

    private void hydrateRelationships(Element element) {
        for (Relationship relationship : element.getRelationships()) {
            relationship.setSource(getElement(relationship.getSourceId()));
            relationship.setDestination(getElement(relationship.getDestinationId()));
        }
    }

    private void hydrateView(View view) {
        view.setSoftwareSystem(getSoftwareSystemWithId(view.getSoftwareSystemId()));

        for (ElementView elementView : view.getElements()) {
            elementView.setElement(getElement(elementView.getId()));
        }
    }

    public boolean contains(Element element) {
        return elementsById.values().contains(element);
    }

    public SystemContextView createContextView(SoftwareSystem softwareSystem) {
        SystemContextView view = new SystemContextView(softwareSystem);
        systemContextViews.add(view);

        return view;
    }

    public ContainerView createContainerView(SoftwareSystem softwareSystem) {
        ContainerView view = new ContainerView(softwareSystem);
        containerViews.add(view);

        return view;
    }

    public ComponentView createComponentView(SoftwareSystem softwareSystem, Container container) {
        ComponentView view = new ComponentView(softwareSystem, container);
        componentViews.add(view);

        return view;
    }

    public Set<SystemContextView> getSystemContextViews() {
        return new TreeSet<>(systemContextViews);
    }

    public Set<ContainerView> getContainerViews() {
        return new TreeSet<>(containerViews);
    }

    public Set<ComponentView> getComponentViews() {
        return new TreeSet<>(componentViews);
    }

    public SoftwareSystem getSoftwareSystemWithName(String name) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getName().equals(name)) {
                return softwareSystem;
            }
        }

        return null;
    }

    public SoftwareSystem getSoftwareSystemWithId(int id) {
        for (SoftwareSystem softwareSystem : getSoftwareSystems()) {
            if (softwareSystem.getId() == id) {
                return softwareSystem;
            }
        }

        return null;
    }

    public Person getPersonWithName(String name) {
        for (Person person : getPeople()) {
            if (person.getName().equals(name)) {
                return person;
            }
        }

        return null;
    }

}