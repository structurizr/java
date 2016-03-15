package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.Collection;

/**
 * The base class for all component finder strategies.
 */
public abstract class ComponentFinderStrategy {

    // a reference to the parent component finder
    protected ComponentFinder componentFinder;

    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    public abstract Collection<Component> findComponents() throws Exception;

    public abstract void findDependencies() throws Exception;

}
