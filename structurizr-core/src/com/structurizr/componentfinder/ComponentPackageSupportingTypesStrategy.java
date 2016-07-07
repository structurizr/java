package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This strategy finds all types in the same package as the component type,
 * and is useful if each component resides in its own Java package.
 */
public class ComponentPackageSupportingTypesStrategy extends SupportingTypesStrategy {

    @Override
    public Set<String> getSupportingTypes(Component component) {
        return componentFinderStrategy.getAllTypes().stream()
                .filter(c -> c.getCanonicalName() != null && c.getCanonicalName().startsWith(component.getPackage()))
                .map(c -> c.getCanonicalName())
                .collect(Collectors.toSet());
    }

}
