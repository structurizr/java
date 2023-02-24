package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Documentation;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represents a "container" in the C4 model.
 */
public final class Container extends StaticStructureElement implements Documentable {

    private SoftwareSystem parent;
    private String technology;

    private Set<Component> components = new LinkedHashSet<>();

    private Documentation documentation = new Documentation();

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
     * @return  the resulting Component instance
     * @throws  IllegalArgumentException    if the component name is null or empty, or a component with the same name already exists
     */
    public Component addComponent(String name) {
        return this.addComponent(name, "");
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
        return getModel().addComponent(this, name, description, technology);
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
     * Gets the canonical name of this container, in the form "/Software System/Container".
     *
     * @return  the canonical name, as a String
     */
    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    @Override
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.CONTAINER));
    }

    /**
     * Gets the documentation associated with this container.
     *
     * @return  a Documentation object
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Sets the documentation associated with this container.
     *
     * @param documentation     a Documentation object
     */
    void setDocumentation(@Nonnull Documentation documentation) {
        this.documentation = documentation;
    }

}