package com.structurizr.component.supporting;

import com.structurizr.component.Type;

import java.util.Set;

/**
 * A strategy that finds all referenced types, irrespective of package.
 */
public class AllReferencedTypesSupportingTypesStrategy implements SupportingTypesStrategy {

    @Override
    public Set<Type> findSupportingTypes(Type type) {
        return type.getDependencies();
    }

}