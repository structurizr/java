package com.structurizr.domain;

public class Person extends Element {

    @Override
    public ElementType getType() {
        return ElementType.Person;
    }

    public void addRelationshipTo(SoftwareSystem destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    public void uses(Container destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

}