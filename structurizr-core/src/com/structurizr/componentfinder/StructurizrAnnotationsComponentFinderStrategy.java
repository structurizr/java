package com.structurizr.componentfinder;

import com.structurizr.annotation.*;
import com.structurizr.model.Component;
import com.structurizr.model.*;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * This component finder strategy looks for the following Structurizr annotations.
 *
 *  - Definitions: @Component
 *  - Efferent dependencies: @UsesSoftwareSystem, @UsesContainer, @UsesComponent
 *  - Afferent dependencies: @UsedByPerson, @UsedBySoftwareSystem, @UsedByContainer
 */
public class StructurizrAnnotationsComponentFinderStrategy extends AbstractReflectionsComponentFinderStrategy {

    public StructurizrAnnotationsComponentFinderStrategy() {
        super(new FirstImplementationOfInterfaceSupportingTypesStrategy());
    }

    public StructurizrAnnotationsComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    /**
     * This finds all types that have been annotated @Component.
     */
    @Override
    public void findComponents() throws Exception {
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(com.structurizr.annotation.Component.class);
        for (Class<?> componentType : componentTypes) {
            Set<Class<?>> classes = findSuperTypesAnnotatedWith(componentType, com.structurizr.annotation.Component.class);
            classes.remove(componentType);

            // if there are super types with the @Component annotation, ignore this type
            if (classes.isEmpty()) {
                Component component = getComponentFinder().getContainer().addComponent(
                        componentType.getSimpleName(),
                        componentType,
                        componentType.getAnnotation(com.structurizr.annotation.Component.class).description(),
                        componentType.getAnnotation(com.structurizr.annotation.Component.class).technology()
                );
                add(component);
            }
        }
    }

    @Override
    public void findDependencies() throws Exception {
        // this will find component dependencies, but the relationship descriptions
        // will be empty because we can't get that from the code
        super.findDependencies();

        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {
                Class type = Class.forName(component.getType());

                // find the efferent dependencies
                findUsesComponentAnnotations(component, type);
                findUsesSoftwareSystemsAnnotations(component, type);
                findUsesContainerAnnotations(component, type);

                // and also the afferent dependencies
                findUsedByPersonAnnotations(component, type);
                findUsedBySoftwareSystemAnnotations(component, type);
                findUsedByContainerAnnotations(component, type);
            }
        }
    }

    /**
     * This will add a description to existing component dependencies, where they have
     * been annotated @UsesComponent.
     */
    private void findUsesComponentAnnotations(Component component, Class type) throws Exception {
        for (Field field : type.getDeclaredFields()) {
            UsesComponent annotation = field.getAnnotation(UsesComponent.class);
            if (annotation != null) {
                String name = field.getType().getCanonicalName();
                String description = field.getAnnotation(UsesComponent.class).description();

                Component destination = componentFinder.getContainer().getComponentOfType(name);
                for (Relationship relationship : component.getRelationships()) {
                    if (relationship.getDestination() == destination) {
                        relationship.setDescription(description);
                    }
                }
            }
        }

        // repeat for super-types
        if (type.getSuperclass() != null) {
            findUsesComponentAnnotations(component, type.getSuperclass());
        }

        // and the implementation class (if appropriate)
        if (type.isInterface()) {
            findUsesComponentAnnotations(component, getFirstImplementationOfInterface(type));
        }
    }

    /**
     * Find the @UsesSoftwareSystem annotations.
     */
    private void findUsesSoftwareSystemsAnnotations(Component component, Class<?> type) throws Exception {
        UsesSoftwareSystem[] annotations = type.getAnnotationsByType(UsesSoftwareSystem.class);
        for (UsesSoftwareSystem annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
            if (softwareSystem != null) {
                component.uses(softwareSystem, description);
            }
        }

        // and repeat for super-types
        if (type.getSuperclass() != null) {
            findUsesSoftwareSystemsAnnotations(component, type.getSuperclass());
        }

        // and the implementation class (if appropriate)
        if (type.isInterface()) {
            findUsesSoftwareSystemsAnnotations(component, getFirstImplementationOfInterface(type));
        }
    }

    /**
     * Find the @UsesContainer annotations.
     */
    private void findUsesContainerAnnotations(Component component, Class<?> type) throws Exception {
        UsesContainer[] annotations = type.getAnnotationsByType(UsesContainer.class);
        for (UsesContainer annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Container container = component.getContainer().getSoftwareSystem().getContainerWithName(name);
            if (container != null) {
                component.uses(container, description);
            }
        }

        // and repeat for super-types
        if (type.getSuperclass() != null) {
            findUsesContainerAnnotations(component, type.getSuperclass());
        }

        // and the implementation class (if appropriate)
        if (type.isInterface()) {
            findUsesContainerAnnotations(component, getFirstImplementationOfInterface(type));
        }
    }

    /**
     * Finds @UsedByPerson annotations.
     */
    private void findUsedByPersonAnnotations(Component component, Class<?> type) throws Exception {
        UsedByPerson[] annotations = type.getAnnotationsByType(UsedByPerson.class);
        for (UsedByPerson annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Person person = component.getModel().getPersonWithName(name);
            if (person != null) {
                person.uses(component, description);
            }
        }

        // and repeat for super-types
        if (type.getSuperclass() != null) {
            findUsedByPersonAnnotations(component, type.getSuperclass());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : type.getInterfaces()) {
            findUsedByPersonAnnotations(component, interfaceType);
        }
    }

    /**
     * Finds @UsedBySoftwareSystem annotations.
     */
    private void findUsedBySoftwareSystemAnnotations(Component component, Class<?> type) throws Exception {
        UsedBySoftwareSystem[] annotations = type.getAnnotationsByType(UsedBySoftwareSystem.class);
        for (UsedBySoftwareSystem annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
            if (softwareSystem != null) {
                softwareSystem.uses(component, description);
            }
        }

        // and repeat for super-types
        if (type.getSuperclass() != null) {
            findUsedBySoftwareSystemAnnotations(component, type.getSuperclass());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : type.getInterfaces()) {
            findUsedBySoftwareSystemAnnotations(component, interfaceType);
        }
    }

    /**
     * Finds @UsedByContainer annotations.
     */
    private void findUsedByContainerAnnotations(Component component, Class<?> type) throws Exception {
        UsedByContainer[] annotations = type.getAnnotationsByType(UsedByContainer.class);
        for (UsedByContainer annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Container container = component.getContainer().getSoftwareSystem().getContainerWithName(name);
            if (container != null) {
                container.uses(component, description);
            }
        }

        // and repeat for super-types
        if (type.getSuperclass() != null) {
            findUsedByContainerAnnotations(component, type.getSuperclass());
        }

        // and all interfaces implemented by this class
        for (Class<?> interfaceType : type.getInterfaces()) {
            findUsedByContainerAnnotations(component, interfaceType);
        }
    }

}
