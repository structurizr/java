package com.structurizr.analysis;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractSpringComponentFinderStrategy extends AbstractComponentFinderStrategy {

    public static final String SPRING_MVC_CONTROLLER = "Spring MVC Controller";
    public static final String SPRING_SERVICE = "Spring Service";
    public static final String SPRING_REPOSITORY = "Spring Repository";
    public static final String SPRING_COMPONENT = "Spring Component";
    public static final String SPRING_REST_CONTROLLER = "Spring REST Controller";
    public static final String SPRING_WEB_SERVICE_ENDPOINT = "Spring Web Service";

    protected boolean includePublicTypesOnly = true;
    private boolean collapseAlternates = false;

    public AbstractSpringComponentFinderStrategy(SupportingTypesStrategy... strategies) {
        super(strategies);
    }

    protected Set<Component> findInterfacesForImplementationClassesWithAnnotation(Class<? extends Annotation> type, String technology) {
        Set<Component> components = new HashSet<>();

        Container container = getComponentFinder().getContainer();
        for (Class<?> annotatedType : findTypesAnnotatedWith(type)) {

            // if we can identify a primary type
            primaryType(annotatedType).ifPresent(primaryType -> {

                // find an existing component or create a fresh one
                final Component component = Optional
                        .ofNullable(container.getComponentWithName(primaryType.getSimpleName()))
                        .orElseGet(() ->
                            container.addComponent(
                                    primaryType.getSimpleName(),
                                    primaryType.getCanonicalName(),
                                    "",
                                    technology)
                        );

                // ensure the component is included
                components.add(component);

                // if the component is not already supported by this type,
                // and the component is backed by more than just a single interface
                // and we'd prefer separate components for alternate implementations
                if (!collapseAlternates &&
                        !isSupportingType(component, annotatedType) &&
                        component.getCode().size() > 1) {
                    // then explode because we don't have a good plan for that yet
                    throw new IllegalStateException("A component named '" + primaryType.getSimpleName() + "' " +
                            "already exists for this container, when adding '" + annotatedType.getSimpleName() + "'");
                }

                // if annotatedType isn't the primaryType, and we didn't explode
                if (primaryType != annotatedType) {
                    // then it's supporting code
                    component.addSupportingType(annotatedType.getCanonicalName());
                }
            });
        }

        return components;
    }

    /**
     * Check whether the component is already supported by this type.
     * @param component the component
     * @param type the type
     * @return true iff the component has code with this type
     */
    private boolean isSupportingType(Component component, Class<?> type) {
        return component.getCode().stream()
                .anyMatch(code -> code.getType().equals(type.getCanonicalName()));
    }


    /**
     * Identify the related originalType that best provides a component name.
     * @param originalType the original originalType to check from
     * @return the original originalType, one of it's interfaces, or empty
     */
    private Optional<Class<?>> primaryType(final Class<?> originalType) {

        // if we have an interface then that's the primary type
        if (originalType.isInterface()) {
            return ifVisibleEnough(originalType);
        }

        // The Spring @Component, @Service and @Repository annotations are typically used to annotate implementation
        // classes, but we really want to find the interface originalType and use that to represent the component. Why?
        // Well, for example, a Spring MVC controller may have a dependency on a "SomeRepository" interface, but
        // it's the "JdbcSomeRepositoryImpl" implementation class that gets annotated with @Repository.
        //
        // This next bit of code tries to find the "SomeRepository" interface...

        for (final Class<?> interfaceType : originalType.getInterfaces()) {

            // if the interface name is in the original type and it's visible enough
            if (originalType.getSimpleName().contains(interfaceType.getSimpleName()) &&
                    isVisibleEnough(interfaceType)) {
                return Optional.of(interfaceType);
            }
        }

        // fall back to the original type
        return ifVisibleEnough(originalType);
    }

    private Optional<Class<?>> ifVisibleEnough(Class<?> originalType) {
        return isVisibleEnough(originalType) ? Optional.of(originalType) : Optional.empty();
    }

    private boolean isVisibleEnough(Class<?> type) {
        return !includePublicTypesOnly || Modifier.isPublic(type.getModifiers());
    }

    /**
     * Sets whether this component finder strategy only finds components that are based upon public types.
     *
     * @param includePublicTypesOnly    true for public types only, false otherwise
     */
    public void setIncludePublicTypesOnly(boolean includePublicTypesOnly) {
        this.includePublicTypesOnly = includePublicTypesOnly;
    }

    /**
     * Sets whether this component finder strategy combines alternate implementations of
     * an interface into a single component.
     *
     * @param collapseAlternates true for a single collapsed component, false otherwise
     */
    public void setCollapseAlternates(boolean collapseAlternates) {
        this.collapseAlternates = collapseAlternates;
    }
}