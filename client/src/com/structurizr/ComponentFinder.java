package com.structurizr;

import com.structurizr.element.ComponentDependency;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Relationship;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComponentFinder {

    private String packageToScan;

    private Map<String,Component> componentsByType = new HashMap<>();

    public ComponentFinder(String packageToScan) {
        this.packageToScan = packageToScan;
    }

    public void findComponents(Container container) throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                  .filterInputsBy(new FilterBuilder().includePackage(packageToScan))
                  .setUrls(ClasspathHelper.forPackage(packageToScan))
                  .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new FieldAnnotationsScanner()));

        Set<Class<?>> componentTypes = reflections.getTypesAnnotatedWith(com.structurizr.element.Component.class);

        for (Class<?> componentType : componentTypes) {
            // create a component, based upon the interface name
            Component component = container.createComponentWithClass(componentType.getCanonicalName(), componentType.getAnnotation(com.structurizr.element.Component.class).description());
            componentsByType.put(component.getFullyQualifiedClassName(), component);
        }

        for (Component component : componentsByType.values()) {
            // find the implementations of the component
            Set<String> componentImplementations = reflections.getStore().getSubTypesOf(component.getFullyQualifiedClassName());
            for (String componentImplementation : componentImplementations) {
                addEfferentDependencies(component, componentImplementation, 1);
            }
        }

        // and finally populate the dependency descriptions
        Set<Field> fields = reflections.getFieldsAnnotatedWith(ComponentDependency.class);
        for (Field field : fields) {
            if (componentsByType.containsKey(field.getType().getCanonicalName())) {
                Component destination = componentsByType.get(field.getType().getCanonicalName());
                String description = field.getAnnotation(ComponentDependency.class).description();

                AnnotatedType[] annotatedTypes = field.getDeclaringClass().getAnnotatedInterfaces();
                for (AnnotatedType annotatedType : annotatedTypes) {
                    if (componentsByType.containsKey(annotatedType.getType().getTypeName())) {
                        Component source = componentsByType.get(annotatedType.getType().getTypeName());

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

    private void addEfferentDependencies(Component component, String implementationType, int depth) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(implementationType);
            for (Object referencedType : cc.getRefClasses()) {
                String referencedTypeName = (String)referencedType;
                if (referencedTypeName.startsWith(packageToScan)) {

                    if (componentsByType.containsKey(referencedTypeName)) {
                        Component destinationComponent = componentsByType.get(referencedTypeName);
                        if (component != destinationComponent) {
                            component.uses(destinationComponent, "");
                        }
                    } else if (!referencedTypeName.equals(implementationType) && depth < 10) {
                        addEfferentDependencies(component, referencedTypeName, ++depth);
                    }
                }
            }
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
    }

}
