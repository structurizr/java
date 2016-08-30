package com.structurizr.componentfinder.func;

import com.google.common.collect.Lists;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DependenciesValidator {
    private final Container container;

    public DependenciesValidator(Container container) {
        this.container = container;
    }

    void validateComponentsAreAllContainerComponent(Collection<Component> components) {
        final Set<Component> containerComponents = container.getComponents();
        assertThat(containerComponents).hasSameSizeAs(components);
        assertThat(containerComponents).containsAll(components);
    }

    void validateRelations(Component source, Element... destinationElements) {
        final List<Element> destinations = Lists.newArrayList(destinationElements);
        assertEquals(destinations.size(), source.getRelationships().size());
        final List<Element> elements = source.getRelationships().stream().map(Relationship::getDestination).collect(Collectors.toList());
        assertTrue(elements.containsAll(destinations));
    }

    Component loadExpectedComponent(Class<?> expectClassComponent) {
        final Component component = container.getComponentWithName(expectClassComponent.getSimpleName());
        validateComponent(expectClassComponent, component);
        return component;
    }

    private void validateComponent(Class<?> expectClassComponent, Component component) {
        assertThat(component).isNotNull();
        assertThat(expectClassComponent.getSimpleName()).isEqualTo(component.getName());
        assertThat(expectClassComponent.getName()).isEqualTo(component.getType());
        assertThat(component.getDescription()).isEmpty();
        assertThat(component.getTechnology()).isEmpty();
    }

}
