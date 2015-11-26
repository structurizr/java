package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractNamingConventionComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    @Override
    public Collection<Component> findComponents() throws Exception {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class<?>> types = getAllTypes();
        for (Class<?> type : types) {
            if (matches(type)) {
                Component component;
                if (type.isInterface()) {
                    component = getComponentFinder().foundComponent(type.getCanonicalName(), null, null, "", "");
                } else {
                    component = getComponentFinder().foundComponent(null, type.getCanonicalName(), null, "", "");
                }
                componentsFound.add(component);
            }
        }

        return componentsFound;
    }

    protected abstract boolean matches(Class<?> type);

}