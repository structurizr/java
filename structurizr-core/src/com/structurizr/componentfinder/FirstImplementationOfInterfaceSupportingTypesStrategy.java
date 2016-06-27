package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * If the component type is an interface, this strategy finds the first implementation of that interface.
 */
public class FirstImplementationOfInterfaceSupportingTypesStrategy extends SupportingTypesStrategy {

    @Override
    public Set<String> getSupportingTypes(Component component) throws Exception {
        Set<String> set = new HashSet<>();
        Class type = componentFinderStrategy.getFirstImplementationOfInterface(component.getType());
        if (type != null) {
            set.add(type.getCanonicalName());
        }

        return set;
    }

}
