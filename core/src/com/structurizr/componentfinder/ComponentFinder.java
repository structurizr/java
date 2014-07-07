package com.structurizr.componentfinder;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.ArrayList;
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

    public void foundComponent(String interfaceType, String implementationType, String description, String technology) {
        if (interfaceType == null || interfaceType.equals("")) {
            // there is no interface type, so we'll just have to use the implementation type
            interfaceType = implementationType;
        }

        Component component = container.getComponentOfType(interfaceType);
        if (component != null) {
            mergeInformation(component, interfaceType, implementationType, description, technology);
        } else {
            String name = interfaceType.substring(interfaceType.lastIndexOf(".") + 1);
            component = container.getComponentWithName(name);
            if (component == null) {
                container.addComponentOfType(interfaceType, implementationType, description, technology);
            } else {
                mergeInformation(component, interfaceType, implementationType, description, technology);
            }
        }
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

    public void findComponents() throws Exception {
        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findComponents();
        }

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            componentFinderStrategy.findDependencies();
        }
    }

    Container getContainer() {
        return this.container;
    }

    String getPackageToScan() {
        return packageToScan;
    }

}
