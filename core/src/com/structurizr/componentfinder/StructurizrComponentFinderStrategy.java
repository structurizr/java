package com.structurizr.componentfinder;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.annotation.ContainerDependency;
import com.structurizr.annotation.SoftwareSystemDependency;
import com.structurizr.annotation.UsedBy;
import com.structurizr.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * TODO: find dependencies from parent classes (e.g. TweetComponent extends AbstractComponent
 */
public class StructurizrComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public void findComponents() throws Exception {
        findAnnotatedInterfaces();
    }

    @Override
    public void findDependencies() throws Exception {
        super.findDependencies();

        findComponentDependencies();
        findSoftwareSystemDependencies();
        findContainerDependencies();
        findPeopleDependencies();
    }

    private void findAnnotatedInterfaces() {
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(com.structurizr.annotation.Component.class);
        for (Class<?> componentType : componentTypes) {
            String interfaceType = componentType.getCanonicalName();
            String implementationType = componentType.getCanonicalName();

            if (componentType.isInterface()) {
                // TODO: fix this untyped collection reference
                Set subtypes = reflections.getSubTypesOf(componentType);
                if (subtypes.size() > 0) {
                    // WARNING: this code chooses the first implementation that it finds
                    implementationType = ((Class<?>)(subtypes.iterator().next())).getCanonicalName();
                }
            }

            getComponentFinder().foundComponent(
                    interfaceType,
                    implementationType,
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).description(), "");
        }
    }

    private void findComponentDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getImplementationType() != null) {
                findComponentDependencies(component, component.getImplementationType());
            }
        }
    }

    private void findComponentDependencies(Component component, String implementationType) throws Exception {
        Class componentClass = Class.forName(implementationType);

        Field[] fields = componentClass.getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation.annotationType() == ComponentDependency.class) {
                    Component destination = getComponentWithType(field.getType().getCanonicalName());
                    String description = field.getAnnotation(ComponentDependency.class).description();
                    for (Relationship relationship : component.getRelationships()) {
                        if (relationship.getDestination() == destination) {
                            relationship.setDescription(description);
                        }
                    }
                }
            }
        }

        // and repeat for super-types
        if (componentClass.getSuperclass() != null) {
            findComponentDependencies(component, componentClass.getSuperclass().getCanonicalName());
        }
    }

    private void findSoftwareSystemDependencies() throws Exception {
        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getImplementationType() != null) {
                findSoftwareSystemDependencies(component, component.getImplementationType());
            }
        }
    }

    private void findSoftwareSystemDependencies(Component component, String implementationType) throws Exception {
//        System.out.println(implementationType);
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
            if (component.getImplementationType() != null) {
                findContainerDependencies(component, component.getImplementationType());
            }
        }
    }

    private void findContainerDependencies(Component component, String implementationType) throws Exception {
        Class<?> componentClass = Class.forName(implementationType);

        if (componentClass.getAnnotation(ContainerDependency.class) != null) {
            String target = componentClass.getAnnotation(ContainerDependency.class).target();
            String description = componentClass.getAnnotation(ContainerDependency.class).description();
            Container targetContainer = component.getParent().getParent().getContainerWithName(target);
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
            if (component.getImplementationType() != null) {
                findPeopleDependencies(component, component.getImplementationType());
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
