package com.structurizr.component.filter;

import com.structurizr.component.Type;

/**
 * Determines whether a given type should be accepted or not.
 */
public interface TypeFilter {

    boolean accept(Type type);

}