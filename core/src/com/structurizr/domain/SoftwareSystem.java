package com.structurizr.domain;

import java.util.HashSet;
import java.util.Set;

public class SoftwareSystem extends Element {

    private Set<Container> containers = new HashSet<>();

    @Override
    public ElementType getType() {
        return ElementType.SoftwareSystem;
    }

    public void add(Container container) {
        containers.add(container);
    }

    public Set<Container> getContainers() {
        return new HashSet<>(containers);
    }

    public void uses(SoftwareSystem destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }

    public void addRelationshipTo(Person destination, String description) {
        Relationship relationship = new Relationship(this, destination, description);
        relationships.add(relationship);
    }


    public Container createContainer(String name, String description, String technology) {
        return getModel().createContainer(this, name, description, technology);
    }

}
