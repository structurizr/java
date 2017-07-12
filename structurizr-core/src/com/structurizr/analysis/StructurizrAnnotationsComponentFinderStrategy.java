package com.structurizr.analysis;

import com.structurizr.annotation.*;
import com.structurizr.model.Component;
import com.structurizr.model.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * This component finder strategy looks for the following Structurizr annotations.
 *
 *  - Definitions: @Component
 *  - Efferent dependencies: @UsesSoftwareSystem, @UsesContainer, @UsesComponent
 *  - Afferent dependencies: @UsedByPerson, @UsedBySoftwareSystem, @UsedByContainer
 */
public class StructurizrAnnotationsComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public StructurizrAnnotationsComponentFinderStrategy() {
        super(new FirstImplementationOfInterfaceSupportingTypesStrategy());
    }

    public StructurizrAnnotationsComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() throws Exception {
        Set<Component> components = new HashSet<>();

        // find all types that have been annotated @Component
        Set<Class<?>> componentTypes = findTypesAnnotatedWith(com.structurizr.annotation.Component.class);
        for (Class<?> componentType : componentTypes) {
            Component component = getComponentFinder().getContainer().addComponent(
                    componentType.getSimpleName(),
                    componentType,
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).description(),
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).technology()
            );
            components.add(component);
        }

        return components;
    }

    @Override
    public void postFindComponents() throws Exception {
        // this will find component dependencies, but the relationship descriptions
        // will be empty because we can't get that from the code
        super.postFindComponents();

        for (Component component : getComponentFinder().getContainer().getComponents()) {
            if (component.getType() != null) {

                // find the efferent dependencies
                for (CodeElement codeElement : component.getCode()) {
                    findUsesComponentAnnotations(component, codeElement.getType());
                }
                for (CodeElement codeElement : component.getCode()) {
                    findUsesSoftwareSystemsAnnotations(component, codeElement.getType());
                }
                for (CodeElement codeElement : component.getCode()) {
                    findUsesContainerAnnotations(component, codeElement.getType());
                }

                // and also the afferent dependencies
                for (CodeElement codeElement : component.getCode()) {
                    findUsedByPersonAnnotations(component, codeElement.getType());
                }
                for (CodeElement codeElement : component.getCode()) {
                    findUsedBySoftwareSystemAnnotations(component, codeElement.getType());
                }
                for (CodeElement codeElement : component.getCode()) {
                    findUsedByContainerAnnotations(component, codeElement.getType());
                }
            }
        }
    }

    /**
     * This will add a description to existing component dependencies, where they have
     * been annotated @UsesComponent.
     */
    private void findUsesComponentAnnotations(Component component, String typeName) throws Exception {
        Class type = ClassLoader.getSystemClassLoader().loadClass(typeName);
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
    }

    /**
     * Find the @UsesSoftwareSystem annotations.
     */
    private void findUsesSoftwareSystemsAnnotations(Component component, String typeName) throws Exception {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(typeName);
        UsesSoftwareSystem[] annotations = type.getAnnotationsByType(UsesSoftwareSystem.class);
        for (UsesSoftwareSystem annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
            if (softwareSystem != null) {
                component.uses(softwareSystem, description);
            }
        }
    }

    /**
     * Find the @UsesContainer annotations.
     */
    private void findUsesContainerAnnotations(Component component, String typeName) throws Exception {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(typeName);
        UsesContainer[] annotations = type.getAnnotationsByType(UsesContainer.class);
        for (UsesContainer annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Container container = component.getContainer().getSoftwareSystem().getContainerWithName(name);
            if (container != null) {
                component.uses(container, description);
            }
        }
    }

    /**
     * Finds @UsedByPerson annotations.
     */
    private void findUsedByPersonAnnotations(Component component, String typeName) throws Exception {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(typeName);
        UsedByPerson[] annotations = type.getAnnotationsByType(UsedByPerson.class);
        for (UsedByPerson annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Person person = component.getModel().getPersonWithName(name);
            if (person != null) {
                person.uses(component, description);
            }
        }
    }

    /**
     * Finds @UsedBySoftwareSystem annotations.
     */
    private void findUsedBySoftwareSystemAnnotations(Component component, String typeName) throws Exception {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(typeName);
        UsedBySoftwareSystem[] annotations = type.getAnnotationsByType(UsedBySoftwareSystem.class);
        for (UsedBySoftwareSystem annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
            if (softwareSystem != null) {
                softwareSystem.uses(component, description);
            }
        }
    }

    /**
     * Finds @UsedByContainer annotations.
     */
    private void findUsedByContainerAnnotations(Component component, String typeName) throws Exception {
        Class<?> type = ClassLoader.getSystemClassLoader().loadClass(typeName);
        UsedByContainer[] annotations = type.getAnnotationsByType(UsedByContainer.class);
        for (UsedByContainer annotation : annotations) {
            String name = annotation.name();
            String description = annotation.description();

            Container container = component.getContainer().getSoftwareSystem().getContainerWithName(name);
            if (container != null) {
                container.uses(component, description);
            }
        }
    }

}
