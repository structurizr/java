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

    public void uses(SoftwareSystem destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public void uses(Container destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    /**
     * Adds a unidirectional "uses" style relationship between this person
     * and a component (within a container).
     *
     * @param destination   the target of the relationship
     * @param description   a description of the relationship (e.g. "uses", "gets data from", "sends data to")
     */
    public void uses(Component destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
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