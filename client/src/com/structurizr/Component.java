package com.structurizr;

import java.util.HashSet;
import java.util.Set;

public class Component {

    private String fullyQualifiedClassName;
    private String responsibility;

    private Set<Component> dependencies = new HashSet<>();

    public Component(String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    public String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    public String getName() {
        return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf(".") + 1);
    }

    public String getPackage() {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf("."));
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public void addDependency(Component component) {
        this.dependencies.add(component);
    }

    public Set<Component> getDependencies() {
        return dependencies;
    }

}
