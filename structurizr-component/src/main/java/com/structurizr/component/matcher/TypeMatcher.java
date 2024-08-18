package com.structurizr.component.matcher;

import com.structurizr.component.Type;

/**
 * Determines whether a given type matches the rules for being identified as a component.
 */
public interface TypeMatcher {

    boolean matches(Type type);

    String getTechnology();

}