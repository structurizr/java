package com.structurizr.component.matcher;

/**
 * A superclass for TypeMatcher implementations.
 */
public abstract class AbstractTypeMatcher implements TypeMatcher {

    private final String technology;

    public AbstractTypeMatcher(String technology) {
        this.technology = technology;
    }

    @Override
    public String getTechnology() {
        return technology;
    }

}