package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Component extends Element {

    private Container parent;

    private String technology = "";
    private String interfaceType;
    private String implementationType;

    public Component() {
        addTags(Tags.COMPONENT);
    }

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
        if (this.name != null) {
            return super.getName();
        } else if (this.interfaceType != null) {
            return interfaceType.substring(interfaceType.lastIndexOf(".") + 1);
        } else if (this.implementationType != null) {
            return implementationType.substring(implementationType.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    @JsonIgnore
    public String getPackage() {
        return interfaceType.substring(0, interfaceType.lastIndexOf("."));
    }

    @Override
    public ElementType getType() {
        return ElementType.Component;
    }

    @Override
    public String getCanonicalName() {
        return getParent().getCanonicalName() + CANONICAL_NAME_SEPARATOR + formatForCanonicalName(getName());
    }

}