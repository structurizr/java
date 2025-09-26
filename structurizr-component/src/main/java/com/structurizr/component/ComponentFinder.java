package com.structurizr.component;

import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.provider.TypeProvider;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.util.StringUtils;
import org.apache.bcel.Repository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Allows you to find components in a Java codebase based upon a set of pluggable and customisable rules.
 * Use the {@link ComponentFinderBuilder} to create an instance of this class.
 */
public final class ComponentFinder {

    private static final Log log = LogFactory.getLog(ComponentFinder.class);

    private static final String COMPONENT_TYPE_PROPERTY_NAME = "component.type";
    private static final String COMPONENT_SOURCE_PROPERTY_NAME = "component.src";

    private final TypeRepository typeRepository = new TypeRepository();
    private final Container container;
    private final List<ComponentFinderStrategy> componentFinderStrategies = new ArrayList<>();

    ComponentFinder(Container container, TypeFilter typeFilter, Collection<TypeProvider> typeProviders, List<ComponentFinderStrategy> componentFinderStrategies) {
        this.container = container;
        this.componentFinderStrategies.addAll(componentFinderStrategies);

        log.debug("Initialising component finder:");
        log.debug(" - for: " + container.getCanonicalName());
        for (TypeProvider typeProvider : typeProviders) {
            log.debug(" - from: " + typeProvider);
        }
        log.debug(" - filtered by: " + typeFilter);
        for (ComponentFinderStrategy strategy : componentFinderStrategies) {
            log.debug(" - with strategy: " + strategy);
        }

        new TypeFinder().run(typeProviders, typeFilter, typeRepository);
        Repository.clearCache();
        for (Type type : typeRepository.getTypes()) {
            if (type.getJavaClass() != null) {
                Repository.addClass(type.getJavaClass());
                new TypeDependencyFinder().run(type, typeRepository);
            }
        }
    }

    /**
     * Find components, using all configured strategies, in the order they were added.
     */
    public Set<Component> run() {
        Set<DiscoveredComponent> discoveredComponents = new LinkedHashSet<>();
        Map<DiscoveredComponent, Component> componentMap = new HashMap<>();
        Set<Component> componentSet = new LinkedHashSet<>();

        for (ComponentFinderStrategy componentFinderStrategy : componentFinderStrategies) {
            Set<DiscoveredComponent> set = componentFinderStrategy.run(typeRepository);
            if (set.isEmpty()) {
                log.debug("No components were found by " + componentFinderStrategy);
            }
            discoveredComponents.addAll(set);
        }

        for (DiscoveredComponent discoveredComponent : discoveredComponents) {
            Component component = container.addComponent(discoveredComponent.getName());
            component.addProperty(COMPONENT_TYPE_PROPERTY_NAME, discoveredComponent.getPrimaryType().getFullyQualifiedName());
            if (!StringUtils.isNullOrEmpty(discoveredComponent.getPrimaryType().getSource())) {
                component.addProperty(COMPONENT_SOURCE_PROPERTY_NAME, discoveredComponent.getPrimaryType().getSource());
            }
            component.setDescription(discoveredComponent.getDescription());
            component.setTechnology(discoveredComponent.getTechnology());
            component.setUrl(discoveredComponent.getUrl());

            component.addTags(discoveredComponent.getTags().toArray(new String[0]));
            for (String name : discoveredComponent.getProperties().keySet()) {
                component.addProperty(name, discoveredComponent.getProperties().get(name));
            }

            componentMap.put(discoveredComponent, component);
            componentSet.add(component);
        }

        // find dependencies between all components
        for (DiscoveredComponent discoveredComponent : discoveredComponents) {
            Component component = componentMap.get(discoveredComponent);
            log.debug("Component dependencies for \"" + component.getName() + "\":");
            Set<com.structurizr.component.Type> typeDependencies = discoveredComponent.getAllDependencies();
            for (Type typeDependency : typeDependencies) {
                for (DiscoveredComponent c : discoveredComponents) {
                    if (c != discoveredComponent) {
                        if (c.getAllTypes().contains(typeDependency)) {
                            Component componentDependency = componentMap.get(c);
                            log.debug(" -> " + componentDependency.getName());
                            component.uses(componentDependency, "");
                        }
                    }
                }
            }
            if (component.getRelationships().isEmpty()) {
                log.debug(" - none");
            }
        }

        // now visit all components
        for (DiscoveredComponent discoveredComponent : componentMap.keySet()) {
            Component component = componentMap.get(discoveredComponent);
            log.debug("Visiting \"" + component.getName() + "\"");
            discoveredComponent.getComponentFinderStrategy().visit(component);
        }

        return componentSet;
    }

}