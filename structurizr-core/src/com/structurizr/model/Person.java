package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a "person" in the C4 model.
 */
public final class Person extends StaticStructureElement {

    private Location location = Location.Unspecified;

    @Override
    @JsonIgnore
    public Element getParent() {
        return null;
    }

    Person() {
    }

    /**
     * Gets the location of this person.
     *
     * @return  a Location
     */
    public Location getLocation() {
        return location;
    }

    @Deprecated
    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    @Override
    public String getCanonicalName() {
        return new CanonicalNameGenerator().generate(this);
    }

    @Nonnull
    @Override
    public Set<String> getDefaultTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.PERSON));
    }

    @Override
    public Relationship delivers(@Nonnull Person destination, String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Person destination, String description, String technology) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Relationship delivers(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds an interaction between this person and another.
     *
     * @param destination       the Person being interacted with
     * @param description       a description of the interaction
     * @return                  the resulting Relationship
     */
    public Relationship interactsWith(Person destination, String description) {
        return interactsWith(destination, description, null);
    }

    /**
     * Adds an interaction between this person and another.
     *
     * @param destination       the Person being interacted with
     * @param description       a description of the interaction
     * @param technology        the technology of the interaction (e.g. Telephone)
     * @return                  the resulting Relationship
     */
    public Relationship interactsWith(Person destination, String description, String technology) {
        return interactsWith(destination, description, technology, null);
    }

    /**
     * Adds an interaction between this person and another.
     *
     * @param destination       the Person being interacted with
     * @param description       a description of the interaction
     * @param technology        the technology of the interaction (e.g. Telephone)
     * @param interactionStyle  the interaction style (e.g. Synchronous or Asynchronous)
     * @return                  the resulting Relationship
     */
    public Relationship interactsWith(Person destination, String description, String technology, InteractionStyle interactionStyle) {
        return getModel().addRelationship(this, destination, description, technology, interactionStyle);
    }

}
