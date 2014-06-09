package com.structurizr;

public class Component {

    private String fullyQualifiedClassName;
    private String responsibility;

    public Component(String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
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

}
