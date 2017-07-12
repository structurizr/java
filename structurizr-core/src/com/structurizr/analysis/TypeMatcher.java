package com.structurizr.analysis;

/**
 * Determines whether a given type implements the rules for being identified as a component.
 */
public interface TypeMatcher {

    boolean matches(Class type);

    String getDescription();

    String getTechnology();

}
