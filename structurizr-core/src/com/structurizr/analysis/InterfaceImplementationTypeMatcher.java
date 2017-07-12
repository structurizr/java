package com.structurizr.analysis;

/**
 * Matches types where the type implements the specified interface.
 */
public class InterfaceImplementationTypeMatcher implements TypeMatcher {

    private Class interfaceType;
    private String description;
    private String technology;

    public InterfaceImplementationTypeMatcher(Class interfaceType, String description, String technology) {
        this.interfaceType = interfaceType;
        this.description = description;
        this.technology = technology;


        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException(interfaceType + " is not an interface type");
        }
    }

    @Override
    public boolean matches(Class type) {
        return interfaceType.isAssignableFrom(type);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTechnology() {
        return technology;
    }

}
