package com.structurizr.component.supporting;

import com.structurizr.component.Type;
import com.structurizr.component.TypeRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A strategy that finds all referenced types in the same package as the component type.
 */
public class AllReferencedTypesInPackageSupportingTypesStrategy implements SupportingTypesStrategy {

    @Override
    public Set<Type> findSupportingTypes(Type type, TypeRepository typeRepository) {
        String packageName = type.getPackageName();

        return type.getDependencies().stream()
                    .filter(dependency -> dependency.getPackageName().startsWith(packageName))
                    .collect(Collectors.toSet());
    }

}