package com.structurizr.component.supporting;

import com.structurizr.component.Type;

import java.util.Collections;
import java.util.Set;

/**
 * A strategy that returns an empty set of supporting types.
 */
public class DefaultSupportingTypesStrategy implements SupportingTypesStrategy {

    @Override
    public Set<Type> findSupportingTypes(Type type) {
        return Collections.emptySet();
    }

}