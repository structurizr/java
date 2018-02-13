package com.structurizr.analysis;

import com.structurizr.model.CodeElement;
import com.structurizr.model.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * If the component type is an interface, this strategy finds the first implementation of that interface.
 */
public class FirstImplementationOfInterfaceSupportingTypesStrategy extends SupportingTypesStrategy {

    private static final Log log = LogFactory.getLog(FirstImplementationOfInterfaceSupportingTypesStrategy.class);

    @Override
    public Set<Class<?>> findSupportingTypes(Component component) {
        CodeElement code = component.getPrimaryCode();

        if (code == null) {
            return emptySet();
        }

        Set<Class<?>> set = new HashSet<>();

        try {
            Class<?> componentType = getTypeRepository().loadClass(code.getType());
            if (componentType.isInterface()) {
                Class<?> type = TypeUtils.findFirstImplementationOfInterface(componentType, getTypeRepository().getAllTypes());
                if (type != null) {
                    set.add(type);
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + code.getType());
        }

        return set;
    }

}
