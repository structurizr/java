package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * Represents a "container" in the C4 model.
 */
public final class Container extends StaticStructureElement {

    private SoftwareSystem parent;
    private String technology;

    private Set<Component> components = new LinkedHashSet<>();

    Container() {
    }

    /**
     * Gets the parent software system.
     *
     * @return  the parent SoftwareSystem instance
     */
    @Override
    @JsonIgnore
    public Element getParent() {
        return parent;
    }

    /**
     * Gets the parent software system.
     *
     * @return  the parent SoftwareSystem instance
     */
    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return parent;
    }

    void setParent(SoftwareSystem parent) {
        this.parent = parent;
    }

    /**
     * Gets the technology associated with this container (e.g. "Spring MVC application").
     *
     * @return  the technology, as a String,
     *          or null if no technology has been specified
     */
    public String getTechnology() {
        return technology;
    }

    /**
     * Sets the technology associated with this container.
     *
     * @param technology    the technology, as a String
     */
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Adds a component to this container.
     *
     * @param name          the name of the component
     * @param description   a description of the component
     * @return  the resulting Component instance
     * @throws  IllegalArgumentException    if the component name is null or empty, or a component with the same name already exists
     */
    public Component addComponent(String name, String description) {
        return this.addComponent(name, description, null);
    }

    /**
     * Adds a component to this container.
     *
     * @param name          the name of the component
     * @param description   a description of the component
     * @param technology    the technology of the component
     * @return  the resulting Component instance
     * @throws  IllegalArgumentException    if the component name is null or empty, or a component with the same name already exists
     */
    public Component addComponent(String name, String description, String technology) {
        return this.addComponent(name, (String)null, description, technology);
    }

    /**
     * Adds a component to this container.
     *
     * @param name          the name of the component
     * @param type          a Class instance representing the primary type of the component
     * @param description   a description of the component
     * @param technology    the technology of the component
     * @return  the resulting Component instance
     * @throws  IllegalArgumentException    if the component name is null or empty, or a component with the same name already exists
     */
    public Component addComponent(String name, Class type, String description, String technology) {
        return this.addComponent(name, type.getCanonicalName(), description, technology);
    }

    /**
     * Adds a component to this container.
     *
     * @param name          the name of the component
     * @param type          a String describing the fully qualified name of the primary type of the component
     * @param description   a description of the component
     * @param technology    the technology of the component
     * @return  the resulting Component instance
     * @throws  IllegalArgumentException    if the component name is null or empty, or a component with the same name already exists
     */
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
        return new HashSet<>(components);
    }

    void setComponents(Set<Component> components) {
        if (components != null) {
            this.components = new HashSet<>(components);
        }
    }

    /**
     * Gets the component with the specified name.
     *
     * @param name      the name of the component
     * @return  the Component instance, or null if a component with the specified name does not exist
     * @throws  IllegalArgumentException    if the name is null or empty
     */
    public Component getComponentWithName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A component name must be provided.");
        }

        Optional<Component> component = components.stream().filter(c -> name.equals(c.getName())).findFirst();
        return component.orElse(null);
    }

    /**
     * Gets the component of the specified type.
     *
     * @param type      the fully qualified type of the component
     * @return  the Component instance, or null if a component with the specified type does not exist
     * @throws  IllegalArgumentException    if the type is null or empty
     */
    public Component getComponentOfType(String type) {
        if (type == null || type.trim().length() == 0) {
            throw new IllegalArgumentException("A component type must be provided.");
        }

        Optional<Component> component = components.stream().filter(c -> type.equals(c.getType().getType())).findFirst();
        return component.orElse(null);
    }

    /**
     * Gets the canonical name of this container, in the form "/Software System/Container".
     *
     * @return  the canonical name, as a String
     */
    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.CONTAINER));
    }

}