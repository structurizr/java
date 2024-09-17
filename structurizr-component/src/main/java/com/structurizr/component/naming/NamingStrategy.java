package com.structurizr.component.naming;

import com.structurizr.component.Type;

/**
 * Provides a way customise how component names are generated.
 */
public interface NamingStrategy {

    String nameOf(Type type);

}