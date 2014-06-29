package com.structurizr.componentfinder;

import com.structurizr.annotation.ComponentDependency;
import com.structurizr.annotation.ContainerDependency;
import com.structurizr.annotation.SoftwareSystemDependency;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * TODO: find dependencies from parent classes (e.g. TweetComponent extends AbstractComponent)
 */
public class StructurizrComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public void findComponents() throws Exception {
        findClassesAnnotated(com.structurizr.annotation.Component.class);
    }

    @Override
    public void findDependencies() throws Exception {
        super.findDependencies();

        populateComponentDependencyDescriptions();
        findSoftwareSystemDependencies();
        findContainerDependencies();
    }

    private void findClassesAnnotated(Class<? extends Annotation> type) {
        Set<Class<?>> componentTypes = getTypesAnnotatedWith(type);
        for (Class<?> componentType : componentTypes) {
            // create a component, based upon the name of the type (i.e. class or interface)
            getComponentFinder().foundComponent(componentType.getCanonicalName(),
                    componentType.getAnnotation(com.structurizr.annotation.Component.class).description(), "");
        }
    }

    private void populateComponentDependencyDescriptions() {
        Set<Field> fields = getFieldsAnnotatedWith(ComponentDependency.class);
        for (Field field : fields) {
            Component destination = getComponentWithType(field.getType().getCanonicalName());
            if (destination != null) {
                String description = field.getAnnotation(ComponentDependency.class).description();
                System.out.println(description);

                AnnotatedType[] annotatedTypes = field.getDeclaringClass().getAnnotatedInterfaces();
                for (AnnotatedType annotatedType : annotatedTypes) {
                    Component source = getComponentWithType(annotatedType.getType().getTypeName());
                    if (source != null) {
                        for (Relationship relationship : source.getRelationships()) {
                            if (relationship.getDestination() == destination) {
                                relationship.setDescription(description);
                            }
                        }
                    }
                }
            }
        }
    }

    private void findSoftwareSystemDependencies() {
        Set<Class<?>> componentImplementationTypes = getTypesAnnotatedWith(SoftwareSystemDependency.class);
        for (Class<?> componentImplementationType : componentImplementationTypes) {

            Component component = getComponentWithType(componentImplementationType.getCanonicalName());
            if (component != null) {
                // find the software system with a given name
                String target = componentImplementationType.getAnnotation(SoftwareSystemDependency.class).target();
                String description = componentImplementationType.getAnnotation(SoftwareSystemDependency.class).description();
                SoftwareSystem targetSoftwareSystem = component.getModel().getSoftwareSystemWithName(target);
                if (targetSoftwareSystem != null) {
                    component.uses(targetSoftwareSystem, description);
                }
            }
        }
    }

    private void findContainerDependencies() {
        Set<Class<?>> componentImplementationTypes = getTypesAnnotatedWith(ContainerDependency.class);
        for (Class<?> componentImplementationType : componentImplementationTypes) {

            Component component = getComponentFinder().getContainer().getComponentWithType(componentImplementationType.getCanonicalName());
            if (component != null) {
                // find the software system with a given name
                String target = componentImplementationType.getAnnotation(ContainerDependency.class).target();
                String description = componentImplementationType.getAnnotation(ContainerDependency.class).description();
                Container targetContainer = component.getParent().getParent().getContainerWithName(target);
                if (targetContainer != null) {
                    component.uses(targetContainer, description);
                }
            }
        }
    }

}
