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

    public Component foundComponent(String name, String type, String description, String technology, String sourcePath) {
        Component component = container.getComponentOfType(type);
        if (component != null) {
            mergeInformation(component, type, description, technology, sourcePath);
        } else {
            component = container.getComponentWithName(name);
            if (component == null) {
                component = container.addComponent(name, type, description, technology);
            } else {
                mergeInformation(component, type, description, technology, sourcePath);
            }
        }

        return component;
    }

    private void mergeInformation(Component component, String type, String description, String technology, String sourcePath) {
        if (component.getType() == null || component.getType().isEmpty()) {
            component.setType(type);
        }

        if (component.getDescription() == null || component.getDescription().isEmpty()) {
            component.setDescription(description);
        }

        if (component.getTechnology() == null || component.getTechnology().isEmpty()) {
            component.setTechnology(technology);
        }

        if (component.getSourcePath() == null || component.getSourcePath().isEmpty()) {
            component.setSourcePath(sourcePath);
        }
    }

    public Collection<Component> findComponents() throws Exception {
        Collection<Component> componentsFound = new LinkedList<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentsFound.addAll(componentFinderStrategy.findComponents());
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findDependencies();
        }

        return componentsFound;
    }

    Container getContainer() {
        return this.container;
    }

    String getPackageToScan() {
        return packageToScan;
    }

}
