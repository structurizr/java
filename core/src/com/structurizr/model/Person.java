package com.structurizr.model;

public class Person extends Element {

    Person() {
    }

    public void uses(SoftwareSystem destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public void uses(Container destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    public void uses(Component destination, String description) {
        getModel().addRelationship(new Relationship(this, destination, description));
    }

    @Override
    public ElementType getType() {
        return ElementType.Person;
    }

}