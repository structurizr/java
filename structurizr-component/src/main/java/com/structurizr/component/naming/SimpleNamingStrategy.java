package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Uses the simple/short name of the type (i.e. without the package name).
 */
public class SimpleNamingStrategy implements NamingStrategy {

    public String nameOf(Type type) {
        return type.getName();
    }

}
