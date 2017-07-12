package com.structurizr.analysis;

import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * If the component type is an interface, this strategy finds the first implementation of that interface.
 */
public class FirstImplementationOfInterfaceSupportingTypesStrategy extends SupportingTypesStrategy {

    @Override
    public Set<String> findSupportingTypes(Component component) throws Exception {
        Set<String> set = new HashSet<>();

        Class componentType = ClassLoader.getSystemClassLoader().loadClass(component.getType());
        if (componentType.isInterface()) {
            Class type = TypeUtils.findFirstImplementationOfInterface(componentType, getTypeRepository().getAllTypes());
            if (type != null) {
                set.add(type.getCanonicalName());
            }
        }

        return set;
    }

}
