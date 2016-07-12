package com.structurizr.componentfinder;

import com.structurizr.model.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The base class for all component finder strategies.
 */
public abstract class ComponentFinderStrategy {

    // a reference to the parent component finder
    protected ComponentFinder componentFinder;

    private Collection<Component> components = new ArrayList<>();

    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

    public abstract void findComponents() throws Exception;

    public abstract void findDependencies() throws Exception;

    /**
     * Gets the components found by this component finder strategy.
     *
     * @return  a Collection of Component objects, or an empty collection if none were found
     */
    public Collection<Component> getComponents() {
        return new ArrayList<>(components);
    }

    protected void add(Component component) {
        this.components.add(component);
    }

    protected void addAll(Collection<Component> components) {
        this.components.addAll(components);
    }

}
