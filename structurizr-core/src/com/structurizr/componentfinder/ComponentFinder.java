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

    public Component foundComponent(String interfaceType, String implementationType, String description, String technology) {
        String type = interfaceType;
        if (type == null || type.equals("")) {
            // there is no interface type, so we'll just have to use the implementation type
            type = implementationType;
        }

        Component component = container.getComponentOfType(type);
        if (component != null) {
            mergeInformation(component, interfaceType, implementationType, description, technology);
        } else {
            String name = type.substring(type.lastIndexOf(".") + 1);
            component = container.getComponentWithName(name);
            if (component == null) {
                component = container.addComponentOfType(interfaceType, implementationType, description, technology);
            } else {
                mergeInformation(component, interfaceType, implementationType, description, technology);
            }
        }

        return component;
    }

    public Component enrichComponent(String interfaceType, String implementationType, String description, String technology) {
        String type = interfaceType;
        if (type == null || type.isEmpty()) {
            // there is no interface type, so we'll just have to use the implementation type
            type = implementationType;
        }

        Component component = container.getComponentOfType(type);
        if (component != null) {
            mergeInformation(component, interfaceType, implementationType, description, technology);
        }

        return component;
    }

    private void mergeInformation(Component component, String interfaceType, String implementationType, String description, String technology) {
        if (component.getInterfaceType() != null && component.getInterfaceType().isEmpty()) {
            component.setInterfaceType(interfaceType);
        }

        if (component.getImplementationType() != null && component.getImplementationType().isEmpty()) {
            component.setImplementationType(implementationType);
        }

        if (component.getDescription() != null && component.getDescription().isEmpty()) {
            component.setDescription(description);
        }

        if (component.getTechnology() != null && component.getTechnology().isEmpty()) {
            component.setTechnology(technology);
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
