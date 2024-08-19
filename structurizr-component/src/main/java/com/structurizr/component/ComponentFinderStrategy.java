package com.structurizr.component;

import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.matcher.TypeMatcher;
import com.structurizr.component.naming.NamingStrategy;
import com.structurizr.component.supporting.SupportingTypesStrategy;
import com.structurizr.component.visitor.ComponentVisitor;
import com.structurizr.model.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A component finder strategy is a wrapper for a combination of the following:
 * - {@link TypeMatcher}
 * - {@link TypeFilter}
 * - {@link SupportingTypesStrategy}
 * - {@link NamingStrategy}
 *
 * Use the {@link ComponentFinderStrategyBuilder} to create an instance of this class.
 */
class ComponentFinderStrategy {

    private final TypeMatcher typeMatcher;
    private final TypeFilter typeFilter;
    private final SupportingTypesStrategy supportingTypesStrategy;
    private final NamingStrategy namingStrategy;
    private final ComponentVisitor componentVisitor;

    ComponentFinderStrategy(TypeMatcher typeMatcher, TypeFilter typeFilter, SupportingTypesStrategy supportingTypesStrategy, NamingStrategy namingStrategy, ComponentVisitor componentVisitor) {
        this.typeMatcher = typeMatcher;
        this.typeFilter = typeFilter;
        this.supportingTypesStrategy = supportingTypesStrategy;
        this.namingStrategy = namingStrategy;
        this.componentVisitor = componentVisitor;
    }

    Set<DiscoveredComponent> findComponents(TypeRepository typeRepository) {
        Set<DiscoveredComponent> components = new LinkedHashSet<>();

        Set<Type> types = typeRepository.getTypes();
        for (Type type : types) {
            if (typeMatcher.matches(type) && typeFilter.accept(type)) {
                DiscoveredComponent component = new DiscoveredComponent(namingStrategy.nameOf(type), type);
                component.setDescription(type.getDescription());
                component.setTechnology(typeMatcher.getTechnology());
                component.setComponentFinderStrategy(this);
                components.add(component);

                // now find supporting types
                Set<Type> supportingTypes = supportingTypesStrategy.findSupportingTypes(type, typeRepository);
                component.addSupportingTypes(supportingTypes);
            }
        }

        return components;
    }

    void visit(Component component) {
        this.componentVisitor.visit(component);
    }

    @Override
    public String toString() {
        return "ComponentFinderStrategy{" +
                "typeMatcher=" + typeMatcher +
                ", typeFilter=" + typeFilter +
                '}';
    }

}