package com.structurizr.component.supporting;

import com.structurizr.component.Type;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A strategy that finds all referenced types in the same package as the component type.
 */
public class AllReferencedTypesInSamePackageSupportingTypesStrategy implements SupportingTypesStrategy {

    @Override
    public Set<Type> findSupportingTypes(Type type) {
        String packageName = type.getPackageName();

        return type.getDependencies().stream()
                    .filter(dependency -> dependency.getPackageName().startsWith(packageName))
                    .collect(Collectors.toSet());
    }

}