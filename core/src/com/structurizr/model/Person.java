package com.structurizr.model;

public class Person extends Element {

    @Override
    public ElementType getType() {
        return ElementType.Person;
    }

    public void uses(SoftwareSystem destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    public void uses(Container destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

}