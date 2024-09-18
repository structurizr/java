package com.structurizr.component;

import com.structurizr.component.description.DescriptionStrategy;
import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.matcher.TypeMatcher;
import com.structurizr.component.naming.NamingStrategy;
import com.structurizr.component.supporting.SupportingTypesStrategy;
import com.structurizr.component.url.UrlStrategy;
import com.structurizr.component.visitor.ComponentVisitor;
import com.structurizr.model.Component;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedHashSet;
import java.util.Map;
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
public final class ComponentFinderStrategy {

    private static final Log log = LogFactory.getLog(ComponentFinderStrategy.class);

    private final String technology;
    private final TypeMatcher typeMatcher;
    private final TypeFilter typeFilter;
    private final SupportingTypesStrategy supportingTypesStrategy;
    private final NamingStrategy namingStrategy;
    private final DescriptionStrategy descriptionStrategy;
    private final UrlStrategy urlStrategy;
    private final ComponentVisitor componentVisitor;

    ComponentFinderStrategy(String technology, TypeMatcher typeMatcher, TypeFilter typeFilter, SupportingTypesStrategy supportingTypesStrategy, NamingStrategy namingStrategy, DescriptionStrategy descriptionStrategy, UrlStrategy urlStrategy, ComponentVisitor componentVisitor) {
        this.technology = technology;
        this.typeMatcher = typeMatcher;
        this.typeFilter = typeFilter;
        this.supportingTypesStrategy = supportingTypesStrategy;
        this.namingStrategy = namingStrategy;
        this.descriptionStrategy = descriptionStrategy;
        this.urlStrategy = urlStrategy;
        this.componentVisitor = componentVisitor;
    }

    Set<DiscoveredComponent> run(TypeRepository typeRepository) {
        Set<DiscoveredComponent> components = new LinkedHashSet<>();
        log.debug("Running " + this.toString());

        Set<Type> types = typeRepository.getTypes();
        for (Type type : types) {

            boolean matched = typeMatcher.matches(type);
            boolean accepted = typeFilter.accept(type);

            if (matched) {
                if (accepted) {
                    log.debug(" + " + type.getFullyQualifiedName() + " (matched=true, accepted=true)");
                } else {
                    log.debug(" - " + type.getFullyQualifiedName() + " (matched=true, accepted=false)");
                }
            } else {
                log.debug(" - " + type.getFullyQualifiedName() + " (matched=false)");
            }

            if (matched && accepted) {
                DiscoveredComponent component = new DiscoveredComponent(namingStrategy.nameOf(type), type);
                component.setDescription(descriptionStrategy.descriptionOf(type));
                component.setTechnology(this.technology);
                component.setUrl(urlStrategy.urlOf(type));
                component.addTags(type.getTags());
                Map<String, String> properties = type.getProperties();
                for (String name : properties.keySet()) {
                    component.addProperty(name, properties.get(name));
                }
                component.setComponentFinderStrategy(this);
                components.add(component);

                // now find supporting types
                Set<Type> supportingTypes = supportingTypesStrategy.findSupportingTypes(type, typeRepository);
                if (supportingTypes.isEmpty()) {
                    log.debug("   - none");
                } else {
                    for (Type supportingType : supportingTypes) {
                        log.debug("   + supporting type: " + supportingType.getFullyQualifiedName());
                    }
                    component.addSupportingTypes(supportingTypes);
                }
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
                "technology=" + (technology == null ? null : "'" + technology + "'") +
                ", typeMatcher=" + typeMatcher +
                ", typeFilter=" + typeFilter +
                ", supportingTypesStrategy=" + supportingTypesStrategy +
                ", namingStrategy=" + namingStrategy +
                ", descriptionStrategy=" + descriptionStrategy +
                ", urlStrategy=" + urlStrategy +
                ", componentVisitor=" + componentVisitor +
                '}';
    }

}