package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentable;
import com.structurizr.documentation.Documentation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a "software system" in the C4 model.
 */
public final class SoftwareSystem extends StaticStructureElement implements Documentable {

    private Location location = Location.Unspecified;

    private Set<Container> containers = new LinkedHashSet<>();

    private Documentation documentation = new Documentation();

    /**
     * Gets the parent of this software system.
     *
     * @return  null, as software systems don't have a parent element
     */
    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    SoftwareSystem() {
    }

    /**
     * Gets the location of this software system.
     *
     * @return a Location instance
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of this software system.
     *
     * @param location  a Location instance
     */
    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    void add(Container container) {
        containers.add(container);
    }

    /**
     * Gets the set of containers within this software system.
     *
     * @return a Set of Container objects
     */
    @Nonnull
    public Set<Container> getContainers() {
        return new HashSet<>(containers);
    }

    void setContainers(Set<Container> containers) {
        if (containers != null) {
            this.containers = new HashSet<>(containers);
        }
    }

    /**
     * Adds a container with the specified name.
     *
     * @param name        the name of the container (e.g. "Web Application")
     * @return the newly created Container instance added to the model (or null)
     * @throws IllegalArgumentException     if a container with the same name exists already
     */
    @Nonnull
    public Container addContainer(@Nonnull String name) {
        return addContainer(name, "");
    }

    /**
     * Adds a container with the specified name and description.
     *
     * @param name        the name of the container (e.g. "Web Application")
     * @param description a short description/list of responsibilities
     * @return the newly created Container instance added to the model (or null)
     * @throws IllegalArgumentException     if a container with the same name exists already
     */
    @Nonnull
    public Container addContainer(@Nonnull String name, String description) {
        return addContainer(name, description, "");
    }

    /**
     * Adds a container with the specified name, description and technology.
     *
     * @param name        the name of the container (e.g. "Web Application")
     * @param description a short description/list of responsibilities
     * @param technology  the technology choice (e.g. "Spring MVC", "Java EE", etc)
     * @return the newly created Container instance added to the model (or null)
     * @throws IllegalArgumentException     if a container with the same name exists already
     */
    @Nonnull
    public Container addContainer(@Nonnull String name, String description, String technology) {
        return getModel().addContainer(this, name, description, technology);
    }

    /**
     * Gets the container with the specified name.
     *
     * @param name  the name of the {@link Container}
     * @return the Container instance with the specified name, or null if it doesn't exist
     * @throws IllegalArgumentException     if the name is null or empty
     */
    @Nullable
    public Container getContainerWithName(@Nonnull String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A container name must be provided.");
        }

        for (Container container : getContainers()) {
            if (container.getName().equals(name)) {
                return container;
            }
        }

        return null;
    }

    /**
     * Gets the container with the specified ID.
     *
     * @param id    the {@link Container#getId()} of the container
     * @return  the Container instance with the specified ID, or null if it doesn't exist
     * @throws IllegalArgumentException     if the ID is null or empty
     */
    @Nullable
    public Container getContainerWithId(@Nonnull String id) {
        if (id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("A container ID must be provided.");
        }

        for (Container container : getContainers()) {
            if (container.getId().equals(id)) {
                return container;
            }
        }

        return null;
    }

    /**
     * Gets the canonical name of this software system, in the form "/Software System".
     *
     * @return  the canonical name, as a String
     */
    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    @Override
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.SOFTWARE_SYSTEM));
    }

    /**
     * Gets the documentation associated with this software system.
     *
     * @return  a Documentation object
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    /**
     * Sets the documentation associated with this software system.
     *
     * @param documentation     a Documentation object
     */
    void setDocumentation(@Nonnull Documentation documentation) {
        this.documentation = documentation;
    }

}