package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.Set;

/**
 * Interface for strategies used to find the types that support a component.
 */
public abstract class SupportingTypesStrategy {

    protected AbstractReflectionsComponentFinderStrategy componentFinderStrategy;

    void setComponentFinderStrategy(AbstractReflectionsComponentFinderStrategy componentFinderStrategy) {
        this.componentFinderStrategy = componentFinderStrategy;
    }

    public abstract Set<String> getSupportingTypes(Component component) throws Exception;

}
