package com.structurizr.analysis;

import com.structurizr.model.Component;

/**
 * Defines a strategy that should be called when a duplicate component is found.
 */
public interface DuplicateComponentStrategy {

    /**
     * Called when a duplicate component is found.
     *
     * @param component     the existing Component object
     * @param name          the new component name
     * @param type          the new component type
     * @param description   the new description
     * @param technology    the new technology
     *
     * @return  a Component instance, or null if a new component should not be created
     */
    Component duplicateComponentFound(Component component, String name, String type, String description, String technology);

}