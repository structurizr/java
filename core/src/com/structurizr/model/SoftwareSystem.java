package com.structurizr.model;

import java.util.HashSet;
import java.util.Set;

public class SoftwareSystem extends Element {

    private Set<Container> containers = new HashSet<>();

    void add(Container container) {
        containers.add(container);
    }

    public Set<Container> getContainers() {
        return new HashSet<>(containers);
    }

    public void uses(SoftwareSystem destination, String description) {
        addRelationship(new Relationship(this, destination, description));
    }

    // todo: think of a better name for an outgoing relationship between a software system and a person :-)
    public void sendsSomethingTo(Person destination, String description) {
        addRelationship(new Relationship(this, destination, description));
    }

    public Container addContainer(String name, String description, String technology) {
        return getModel().addContainer(this, name, description, technology);
    }

    public Container getContainerWithName(String name) {
         for (Container container : getContainers()) {
             if (container.getName().equals(name)) {
                 return container;
             }
         }

         return null;
     }

    public Container getContainerWithId(int id) {
         for (Container container : getContainers()) {
             if (container.getId() == id) {
                 return container;
             }
         }

         return null;
     }

    @Override
    public ElementType getType() {
        return ElementType.SoftwareSystem;
    }

}
