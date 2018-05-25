package com.structurizr.analysis;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSpringComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public static final String SPRING_MVC_CONTROLLER = "Spring MVC Controller";
    public static final String SPRING_SERVICE = "Spring Service";
    public static final String SPRING_REPOSITORY = "Spring Repository";
    public static final String SPRING_COMPONENT = "Spring Component";
    public static final String SPRING_REST_CONTROLLER = "Spring REST Controller";
    public static final String SPRING_WEB_SERVICE_ENDPOINT = "Spring Web Service";

    protected boolean includePublicTypesOnly = true;

    public AbstractSpringComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    protected Set<Component> findInterfacesForImplementationClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        Set<Component> components = new HashSet<>();

        Container container = getComponentFinder().getContainer();
        Set<Class<?>> annotatedTypes = findTypesAnnotatedWith(type);
        for (Class<?> annotatedType : annotatedTypes) {
            if (container.getComponentWithName(annotatedType.getSimpleName()) != null) {
              continue;
            } else if (annotatedType.isInterface()) {
                // the annotated type is an interface, so we're done
                components.add(container.addComponent(
                    annotatedType.getSimpleName(), annotatedType.getCanonicalName(), "", technology));
            } else {
                // The Spring @Component, @Service and @Repository annotations are typically used to annotate implementation
                // classes, but we really want to find the interface type and use that to represent the component. Why?
                // Well, for example, a Spring MVC controller may have a dependency on a "SomeRepository" interface, but
                // it's the "JdbcSomeRepositoryImpl" implementation class that gets annotated with @Repository.
                //
                // This next bit of code tries to find the "SomeRepository" interface...
                String componentName = annotatedType.getSimpleName(); // e.g. JdbcSomeRepositoryImpl
                Class<?> componentType = annotatedType;
                boolean foundInterface = false;

                if (annotatedType.getInterfaces().length > 0) {
                    for (Class interfaceType : annotatedType.getInterfaces()) {
                        String interfaceName = interfaceType.getSimpleName();
                        if (componentName.startsWith(interfaceName) || // <InterfaceName><***>
                                componentName.endsWith(interfaceName) ||   // <***><InterfaceName>
                                componentName.contains(interfaceName)) {   // <***><InterfaceName><***>
                            componentName = interfaceName;
                            componentType = interfaceType;
                            foundInterface = true;
                            break;
                        }
                    }
                }

                if (!includePublicTypesOnly || Modifier.isPublic(componentType.getModifiers())) {
                  Component component = container.addComponent(componentName, componentType, "", technology);
                  components.add(component);

                    if (foundInterface) {
                        // the primary component type is now an interface, so add the type we originally found as a supporting type
                        component.addSupportingType(annotatedType.getCanonicalName());
                    }
                }
            }
        }

        return components;
    }

    /**
     * Sets whether this component finder strategy only finds components that are based upon public types.
     *
     * @param includePublicTypesOnly    true for public types only, false otherwise
     */
    public void setIncludePublicTypesOnly(boolean includePublicTypesOnly) {
        this.includePublicTypesOnly = includePublicTypesOnly;
    }

}