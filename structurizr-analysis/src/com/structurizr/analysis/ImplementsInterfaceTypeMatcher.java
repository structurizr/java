package com.structurizr.analysis;

/**
 * Matches types where the type implements the specified interface.
 */
public class ImplementsInterfaceTypeMatcher extends AbstractTypeMatcher {

    private Class interfaceType;

    public ImplementsInterfaceTypeMatcher(Class interfaceType, String description, String technology) {
        super(description, technology);

        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException(interfaceType + " is not an interface type");
        }

        this.interfaceType = interfaceType;
    }

    @Override
    public boolean matches(Class type) {
        return interfaceType.isAssignableFrom(type);
    }

}