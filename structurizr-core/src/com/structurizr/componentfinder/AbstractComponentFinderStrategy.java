package com.structurizr.componentfinder;

public abstract class AbstractComponentFinderStrategy implements ComponentFinderStrategy {

    protected ComponentFinder componentFinder;

    @Override
    public void setComponentFinder(ComponentFinder componentFinder) {
        this.componentFinder = componentFinder;
    }

    protected ComponentFinder getComponentFinder() {
        return componentFinder;
    }

}
