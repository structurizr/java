package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Uses the fully qualified type name.
 */
public class FullyQualifiedNamingStrategy implements NamingStrategy {

    @Override
    public String nameOf(Type type) {
        return type.getFullyQualifiedName();
    }

}