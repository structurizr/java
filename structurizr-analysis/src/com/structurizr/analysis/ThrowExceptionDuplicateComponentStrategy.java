package com.structurizr.analysis;

import com.structurizr.model.Component;

/**
 * Throws an exception when a duplicate component is found.
 */
public class ThrowExceptionDuplicateComponentStrategy implements DuplicateComponentStrategy {

    @Override
    public Component duplicateComponentFound(Component component, String name, String type, String description, String technology) {
        throw new DuplicateComponentException(String.format(
                "A component named \"%s\" already exists in the container named \"%s\".",
                component.getName(),
                component.getContainer().getName()));
    }

}