package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ComponentFinder {

    private Container container;
    private String packageToScan;

    private List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    public ComponentFinder(Container container, String packageToScan, ComponentFinderStrategy... componentFinderStrategies) {
        this.container = container;
        this.packageToScan = packageToScan;

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            this.componentFinderStrategies.add(componentFinderStrategy);
            componentFinderStrategy.setComponentFinder(this);
        }
    }

    public Collection<Component> findComponents() throws Exception {
        Collection<Component> componentsFound = new LinkedList<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findComponents();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findDependencies();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentsFound.addAll(componentFinderStrategy.getComponents());
        }

        return componentsFound;
    }

    public Container getContainer() {
        return this.container;
    }

    public String getPackageToScan() {
        return packageToScan;
    }

}
