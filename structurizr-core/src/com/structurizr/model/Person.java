package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A person who uses a software system.
 */
public class Person extends Element {

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

    public void setLocation(Location location) {
        if (location != null) {
            this.location = location;
        } else {
            this.location = Location.Unspecified;
        }
    }

    @Override
    public String getCanonicalName() {
        return CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

    @Override
    protected Set<String> getRequiredTags() {
        return new LinkedHashSet<>(Arrays.asList(Tags.ELEMENT, Tags.PERSON));
    }

}