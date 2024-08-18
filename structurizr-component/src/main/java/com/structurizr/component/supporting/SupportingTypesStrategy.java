package com.structurizr.component.supporting;

import com.structurizr.component.Type;

import java.util.Set;

/**
 * Provides a strategy to identify the types that support a component.
 */
public interface SupportingTypesStrategy {

    Set<Type> findSupportingTypes(Type type);

}