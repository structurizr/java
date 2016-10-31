package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A container represents something that hosts code or data. A container is
 * something that needs to be running in order for the overall software system
 * to work. In real terms, a container is something like a server-side web application,
 * a client-side web application, client-side desktop application, a mobile app,
 * a microservice, a database scheme, a file system, etc.
 *
 * A container is essentially a context or boundary inside which some code is executed
 * or some data is stored. And each container is a separately deployable thing.
 *
 * See <a href="https://structurizr.com/help/model#Container">Model - Container</a>
 * on the Structurizr website for more information.
 */
public class Container extends Element {

    private SoftwareSystem parent;
    private String technology;

    private Set<Component> components = new LinkedHashSet<>();

    Container() {
    }

    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return parent;
    }

    void setParent(SoftwareSystem parent) {
        this.parent = parent;
    }

    /**
     * Gets the technology associated with thie container (e.g. Apache Tomcat).
     *
     * @return  the technology, as a String,
     *          or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Component addComponent(String name, String description) {
        return getModel().addComponent(this, name, description);
    }

    public Component addComponent(String name, String description, String technology) {
        Component c = getModel().addComponent(this, name, description);
        c.setTechnology(technology);
        return c;
    }

    public Component addComponent(String name, Class type, String description, String technology) {
        return this.addComponent(name, type.getCanonicalName(), description, technology);
    }

    public Component addComponent(String name, String type, String description, String technology) {
        return getModel().addComponentOfType(this, name, type, description, technology);
    }

    void add(Component component) {
        if (getComponentWithName(component.getName()) == null) {
            components.add(component);
        }
    }

    /**
     * Gets the set of components within this software system.
     *
     * @return  a Set of Component objects
     */
    public Set<Component> getComponents() {
        return components;
    }

    public Component getComponentWithName(String name) {
        if (name == null) {
            return null;
        }

        Optional<Component> component = components.stream().filter(c -> name.equals(c.getName())).findFirst();

        if (component.isPresent()) {
            return component.get();
        } else {
            return null;
        }
    }

    public Component getComponentOfType(String type) {
        if (type == null) {
            return null;
        }

        Optional<Component> component = components.stream().filter(c -> type.equals(c.getType())).findFirst();

        if (component.isPresent()) {
            return component.get();
        } else {
            return null;
        }
    }

    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.CONTAINER));
    }

}
