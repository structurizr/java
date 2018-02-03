package com.structurizr.analysis;

import com.structurizr.model.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * If the component type is an interface, this strategy finds the first implementation of that interface.
 */
public class FirstImplementationOfInterfaceSupportingTypesStrategy extends SupportingTypesStrategy {

    private static final Log log = LogFactory.getLog(FirstImplementationOfInterfaceSupportingTypesStrategy.class);

    @Override
    public Set<String> findSupportingTypes(Component component) {
        Set<String> set = new HashSet<>();

        try {
            Class componentType = getTypeRepository().loadClass(component.getType());
            if (componentType.isInterface()) {
                Class type = TypeUtils.findFirstImplementationOfInterface(componentType, getTypeRepository().getAllTypes());
                if (type != null) {
                    set.add(type.getCanonicalName());
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + component.getType());
        }

        return set;
    }

}
