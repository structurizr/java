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

    public void foundComponent(String fullyQualifiedClassName, String description, String technology) {
        if (fullyQualifiedClassName == null || fullyQualifiedClassName.equals("")) {
            return;
        }

        Component component = container.getComponentWithType(fullyQualifiedClassName);
        if (component != null) {
            // this is a duplicate, so do nothing
        } else {
            String name = fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf(".") + 1);
            component = container.getComponentWithName(name);
            if (component == null) {
                container.addComponentWithType(fullyQualifiedClassName, description, technology);
            } else {
                component.setFullyQualifiedClassName(fullyQualifiedClassName);
                component.setTechnology(technology);
            }
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
