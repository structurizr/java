package com.structurizr.componentfinder;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.annotation.ContainerDependency;
import com.structurizr.annotation.SoftwareSystemDependency;
import com.structurizr.annotation.UsedBy;
import com.structurizr.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

public class StructurizrAnnotationsComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public Collection<Component> findComponents() throws Exception {
        return findAnnotatedInterfaces();
    }

    @Override
    public void findDependencies() throws Exception {
        super.findDependencies();

        findComponentDependencies();
        findSoftwareSystemDependencies();
        findContainerDependencies();
        findPeopleDependencies();
    }

    private Collection<Component> findAnnotatedInterfaces() {
        Collection<Component> componentsFound = new LinkedList<>();
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(com.structurizr.annotation.Component.class);
        for (Class<?> componentType : componentTypes) {
            if (componentType.isInterface()) {
                Component component = getComponentFinder().foundComponent(
                        componentType.getSimpleName(),
                        componentType.getCanonicalName(),
                        componentType.getAnnotation(com.structurizr.annotation.Component.class).description(), "", "");
                componentsFound.add(component);
            }
        }

        return componentsFound;
    }

    private void findComponentDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {
                Class interfaceType = Class.forName(component.getType());
                Class implementationType = getFirstImplementationOfInterface(interfaceType);
                if (implementationType != null) {
                    findComponentDependencies(component, implementationType.getCanonicalName());
                }
            }
        }
    }

    private void findComponentDependencies(Component component, String type) throws Exception {
        Class<?> clazz = Class.forName(type);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == ComponentDependency.class) {
                    Component destination = componentFinder.getContainer().getComponentOfType(field.getType().getCanonicalName());
                    String description = field.getAnnotation(ComponentDependency.class).description();
                    for (Relationship relationship : component.getRelationships()) {
                        if (relationship.getDestination() == destination) {
                            relationship.setDescription(description);
                        }
                    }
                }
            }
        }

        // repeat for super-types
        if (clazz.getSuperclass() != null) {
            findComponentDependencies(component, clazz.getSuperclass().getCanonicalName());
        }
    }

    private void findSoftwareSystemDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {
                findSoftwareSystemDependencies(component, component.getType());
            }
        }
    }

    private void findSoftwareSystemDependencies(Component component, String implementationType) throws Exception {
        Class<?> componentClass = Class.forName(implementationType);

        if (componentClass.getAnnotation(SoftwareSystemDependency.class) != null) {
            String target = componentClass.getAnnotation(SoftwareSystemDependency.class).target();
            String description = componentClass.getAnnotation(SoftwareSystemDependency.class).description();
            SoftwareSystem targetSoftwareSystem = component.getModel().getSoftwareSystemWithName(target);
            if (targetSoftwareSystem != null) {
                component.uses(targetSoftwareSystem, description);
            }
        }

        // and repeat for super-types
        if (componentClass.getSuperclass() != null) {
            findSoftwareSystemDependencies(component, componentClass.getSuperclass().getCanonicalName());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : componentClass.getInterfaces()) {
            findSoftwareSystemDependencies(component, interfaceType.getCanonicalName());
        }
    }

    private void findContainerDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {
                findContainerDependencies(component, component.getType());
            }
        }
    }

    private void findContainerDependencies(Component component, String implementationType) throws Exception {
        Class<?> componentClass = Class.forName(implementationType);

        if (componentClass.getAnnotation(ContainerDependency.class) != null) {
            String target = componentClass.getAnnotation(ContainerDependency.class).target();
            String description = componentClass.getAnnotation(ContainerDependency.class).description();
            Container targetContainer = component.getContainer().getSoftwareSystem().getContainerWithName(target);
            if (targetContainer != null) {
                component.uses(targetContainer, description);
            }
        }

        // and repeat for super-types
        if (componentClass.getSuperclass() != null) {
            findContainerDependencies(component, componentClass.getSuperclass().getCanonicalName());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : componentClass.getInterfaces()) {
            findContainerDependencies(component, interfaceType.getCanonicalName());
        }
    }

    private void findPeopleDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {
                findPeopleDependencies(component, component.getType());
            }
        }
    }

    private void findPeopleDependencies(Component component, String implementationType) throws Exception {
        Class<?> componentClass = Class.forName(implementationType);

        if (componentClass.getAnnotation(UsedBy.class) != null) {
            String name = componentClass.getAnnotation(UsedBy.class).person();
            String description = componentClass.getAnnotation(UsedBy.class).description();
            Person person = component.getModel().getPersonWithName(name);
            if (person != null) {
                person.uses(component, description);
            }
        }

        // and repeat for super-types
        if (componentClass.getSuperclass() != null) {
            findPeopleDependencies(component, componentClass.getSuperclass().getCanonicalName());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : componentClass.getInterfaces()) {
            findPeopleDependencies(component, interfaceType.getCanonicalName());
        }
    }

}
