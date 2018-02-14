package com.structurizr.analysis;

/**
 * Matches types where the type extends the specified class.
 */
public class ExtendsClassTypeMatcher extends AbstractTypeMatcher {

    private Class classType;

    public ExtendsClassTypeMatcher(Class classType, String description, String technology) {
        super(description, technology);

        if (classType.isInterface() || classType.isEnum()) {
            throw new IllegalArgumentException(classType + " is not a class type");
        }

        this.classType = classType;
    }

    @Override
    public boolean matches(Class type) {
        return classType.isAssignableFrom(type);
    }

}