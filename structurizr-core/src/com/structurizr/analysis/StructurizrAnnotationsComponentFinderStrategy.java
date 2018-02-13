package com.structurizr.analysis;

import com.structurizr.annotation.*;
import com.structurizr.model.*;
import com.structurizr.model.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static final Log log = LogFactory.getLog(StructurizrAnnotationsComponentFinderStrategy.class);

    public StructurizrAnnotationsComponentFinderStrategy() {
        super(new FirstImplementationOfInterfaceSupportingTypesStrategy());
    }

    public StructurizrAnnotationsComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    @Override
    protected Set<Component> doFindComponents() {
        Set<Component> components = new HashSet<>();

        // find all types that have been annotated @Component
        Set<Class<?>> componentTypes = findTypesAnnotatedWith(com.structurizr.annotation.Component.class);
        for (Class<?> componentType : componentTypes) {
            Component component = getComponentFinder().getContainer().addComponentAndCode(
                    componentType,
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).description(),
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).technology()
            );
            components.add(component);
        }

        return components;
    }

    @Override
    public void afterFindComponents() {
        // this will find component dependencies, but the relationship descriptions
        // will be empty because we can't get that from the code
        super.afterFindComponents();

        for (Component component : getComponentFinder().getContainer().getComponents()) {

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

    /**
     * This will add a description to existing component dependencies, where they have
     * been annotated @UsesComponent.
     */
    private void findUsesComponentAnnotations(Component component, String typeName) {
        try {
            Class type = getTypeRepository().loadClass(typeName);
            for (Field field : type.getDeclaredFields()) {
                UsesComponent annotation = field.getAnnotation(UsesComponent.class);
                if (annotation != null) {
                    String name = field.getType().getCanonicalName();
                    String description = field.getAnnotation(UsesComponent.class).description();
                    String technology = annotation.technology();

                    Component destination = componentFinder.getContainer().getComponentWithCode(field.getType());
                    if (destination != null) {
                        for (Relationship relationship : component.getRelationships()) {
                            if (relationship.getDestination() == destination) {
                                relationship.setDescription(description);
                                relationship.setTechnology(technology);
                            }
                        }
                    } else {
                        log.warn("A component of type \"" + name + "\" could not be found.");
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    /**
     * Find the @UsesSoftwareSystem annotations.
     */
    private void findUsesSoftwareSystemsAnnotations(Component component, String typeName) {
        try {
            Class<?> type = getTypeRepository().loadClass(typeName);
            UsesSoftwareSystem[] annotations = type.getAnnotationsByType(UsesSoftwareSystem.class);
            for (UsesSoftwareSystem annotation : annotations) {
                String name = annotation.name();
                String description = annotation.description();
                String technology = annotation.technology();

                SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
                if (softwareSystem != null) {
                    component.uses(softwareSystem, description, technology);
                } else {
                    log.warn("A software system named \"" + name + "\" could not be found.");
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    /**
     * Find the @UsesContainer annotations.
     */
    private void findUsesContainerAnnotations(Component component, String typeName) {
        try {
            Class<?> type = getTypeRepository().loadClass(typeName);
            UsesContainer[] annotations = type.getAnnotationsByType(UsesContainer.class);
            for (UsesContainer annotation : annotations) {
                String name = annotation.name();
                String description = annotation.description();
                String technology = annotation.technology();

                Container container = findContainerByNameOrCanonicalName(component, name);
                if (container != null) {
                    component.uses(container, description, technology);
                } else {
                    log.warn("A container named \"" + name + "\" could not be found.");
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    /**
     * Finds @UsedByPerson annotations.
     */
    private void findUsedByPersonAnnotations(Component component, String typeName) {
        try {
            Class<?> type = getTypeRepository().loadClass(typeName);
            UsedByPerson[] annotations = type.getAnnotationsByType(UsedByPerson.class);
            for (UsedByPerson annotation : annotations) {
                String name = annotation.name();
                String description = annotation.description();
                String technology = annotation.technology();

                Person person = component.getModel().getPersonWithName(name);
                if (person != null) {
                    person.uses(component, description, technology);
                } else {
                    log.warn("A person named \"" + name + "\" could not be found.");
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    /**
     * Finds @UsedBySoftwareSystem annotations.
     */
    private void findUsedBySoftwareSystemAnnotations(Component component, String typeName) {
        try {
            Class<?> type = getTypeRepository().loadClass(typeName);
            UsedBySoftwareSystem[] annotations = type.getAnnotationsByType(UsedBySoftwareSystem.class);
            for (UsedBySoftwareSystem annotation : annotations) {
                String name = annotation.name();
                String description = annotation.description();
                String technology = annotation.technology();

                SoftwareSystem softwareSystem = component.getModel().getSoftwareSystemWithName(name);
                if (softwareSystem != null) {
                    softwareSystem.uses(component, description, technology);
                } else {
                    log.warn("A software system named \"" + name + "\" could not be found.");
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    /**
     * Finds @UsedByContainer annotations.
     */
    private void findUsedByContainerAnnotations(Component component, String typeName) {
        try {
            Class<?> type = getTypeRepository().loadClass(typeName);
            UsedByContainer[] annotations = type.getAnnotationsByType(UsedByContainer.class);
            for (UsedByContainer annotation : annotations) {
                String name = annotation.name();
                String description = annotation.description();
                String technology = annotation.technology();

                Container container = findContainerByNameOrCanonicalName(component, name);
                if (container != null) {
                    container.uses(component, description, technology);
                } else {
                    log.warn("A container named \"" + name + "\" could not be found.");
                }
            }
        } catch (ClassNotFoundException e) {
            log.warn("Could not load type " + typeName);
        }
    }

    private Container findContainerByNameOrCanonicalName(Component component, String name) {
        // assume that the container resides in the same software system
        Container container = component.getContainer().getSoftwareSystem().getContainerWithName(name);
        if (container == null) {
            // perhaps a canonical name has been specified
            Element element = component.getModel().getElementWithCanonicalName(name);
            if (element != null && element instanceof Container) {
                container = (Container)element;
            }
        }

        return container;
    }

}