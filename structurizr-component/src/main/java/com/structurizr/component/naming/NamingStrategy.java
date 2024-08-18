package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Provides a way to map a fully qualified type name to a component name.
 */
public interface NamingStrategy {

    String nameOf(Type type);

}