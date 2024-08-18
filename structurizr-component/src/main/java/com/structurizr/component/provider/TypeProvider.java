package com.structurizr.component.provider;

import com.structurizr.component.Type;

import java.util.Set;

/**
 * Provides a way to load Java types.
 */
public interface TypeProvider {

    Set<Type> getTypes();

}