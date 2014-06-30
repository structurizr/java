package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Component extends Element {

    private Container parent;

    private String technology;
    private String interfaceType;
    private String implementationType;

    @JsonIgnore
    public Container getParent() {
        return parent;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(String implementationType) {
        this.implementationType = implementationType;
    }

    @Override
    public String getName() {
        if (this.interfaceType == null) {
            return super.getName();
        } else {
            return interfaceType.substring(interfaceType.lastIndexOf(".") + 1);
        }
    }

    @JsonIgnore
    public String getPackage() {
        return interfaceType.substring(0, interfaceType.lastIndexOf("."));
    }

    public void uses(SoftwareSystem destination, String description) {
        addRelationship(new Relationship(this, destination, description));
    }

    public void uses(Container destination, String description) {
        addRelationship(new Relationship(this, destination, description));
    }

    public void uses(Component destination, String description) {
        addRelationship(new Relationship(this, destination, description));
    }

    @Override
    public ElementType getType() {
        return ElementType.Component;
    }

}