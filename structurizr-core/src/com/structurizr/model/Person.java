package com.structurizr.model;

public class Person extends Element {

    private Location location = Location.Unspecified;

    Person() {
        addTags(Tags.PERSON);
    }

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
    public ElementType getType() {
        return ElementType.Person;
    }

    @Override
    public String getCanonicalName() {
        return CANONICAL_NAME_SEPARATOR + getName();
    }

}