package com.structurizr.model;

public class Person extends Element {

    public void uses(SoftwareSystem destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    public void uses(Container destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    public void uses(Component destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    @Override
    public ElementType getType() {
        return ElementType.Person;
    }

}