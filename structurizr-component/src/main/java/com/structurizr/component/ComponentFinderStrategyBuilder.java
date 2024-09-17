package com.structurizr.component;

import com.structurizr.component.description.DefaultDescriptionStrategy;
import com.structurizr.component.description.DescriptionStrategy;
import com.structurizr.component.filter.DefaultTypeFilter;
import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.matcher.TypeMatcher;
import com.structurizr.component.naming.DefaultNamingStrategy;
import com.structurizr.component.naming.NamingStrategy;
import com.structurizr.component.supporting.DefaultSupportingTypesStrategy;
import com.structurizr.component.supporting.SupportingTypesStrategy;
import com.structurizr.component.visitor.ComponentVisitor;
import com.structurizr.component.visitor.DefaultComponentVisitor;
import com.structurizr.util.StringUtils;

/**
 * Provides a way to create a {@link ComponentFinderStrategy} instance.
 */
public final class ComponentFinderStrategyBuilder {

    private String technology;
    private TypeMatcher typeMatcher;
    private TypeFilter typeFilter;
    private SupportingTypesStrategy supportingTypesStrategy;
    private NamingStrategy namingStrategy;
    private DescriptionStrategy descriptionStrategy;
    private ComponentVisitor componentVisitor;

    public ComponentFinderStrategyBuilder() {
    }

    public ComponentFinderStrategyBuilder matchedBy(TypeMatcher typeMatcher) {
        if (typeMatcher == null) {
            throw new IllegalArgumentException("A type matcher must be provided");
        }

        if (this.typeMatcher != null) {
            throw new IllegalArgumentException("A type matcher has already been configured");
        }

        this.typeMatcher = typeMatcher;

        return this;
    }

    public ComponentFinderStrategyBuilder filteredBy(TypeFilter typeFilter) {
        if (typeFilter == null) {
            throw new IllegalArgumentException("A type filter must be provided");
        }

        if (this.typeFilter != null) {
            throw new IllegalArgumentException("A type filter has already been configured");
        }

        this.typeFilter = typeFilter;

        return this;
    }

    public ComponentFinderStrategyBuilder supportedBy(SupportingTypesStrategy supportingTypesStrategy) {
        if (supportingTypesStrategy == null) {
            throw new IllegalArgumentException("A supporting types strategy must be provided");
        }

        if (this.supportingTypesStrategy != null) {
            throw new IllegalArgumentException("A supporting types strategy has already been configured");
        }

        this.supportingTypesStrategy = supportingTypesStrategy;

        return this;
    }

    public ComponentFinderStrategyBuilder withName(NamingStrategy namingStrategy) {
        if (namingStrategy == null) {
            throw new IllegalArgumentException("A naming strategy must be provided");
        }

        if (this.namingStrategy != null) {
            throw new IllegalArgumentException("A naming strategy has already been configured");
        }

        this.namingStrategy = namingStrategy;

        return this;
    }

    public ComponentFinderStrategyBuilder withDescription(DescriptionStrategy descriptionStrategy) {
        if (descriptionStrategy == null) {
            throw new IllegalArgumentException("A description strategy must be provided");
        }

        if (this.descriptionStrategy != null) {
            throw new IllegalArgumentException("A description strategy has already been configured");
        }

        this.descriptionStrategy = descriptionStrategy;

        return this;
    }

    public ComponentFinderStrategyBuilder forTechnology(String technology) {
        if (StringUtils.isNullOrEmpty(technology)) {
            throw new IllegalArgumentException("A technology must be provided");
        }

        if (!StringUtils.isNullOrEmpty(this.technology)) {
            throw new IllegalArgumentException("A technology has already been configured");
        }

        this.technology = technology;

        return this;
    }

    public ComponentFinderStrategyBuilder forEach(ComponentVisitor componentVisitor) {
        if (componentVisitor == null) {
            throw new IllegalArgumentException("A component visitor must be provided");
        }

        if (this.componentVisitor != null) {
            throw new IllegalArgumentException("A component visitor has already been configured");
        }

        this.componentVisitor = componentVisitor;

        return this;
    }

    public ComponentFinderStrategy build() {
        if (typeMatcher == null) {
            throw new RuntimeException("A type matcher must be provided");
        }

        if (typeFilter == null) {
            typeFilter = new DefaultTypeFilter();
        }

        if (supportingTypesStrategy == null) {
            supportingTypesStrategy = new DefaultSupportingTypesStrategy();
        }

        if (namingStrategy == null) {
            namingStrategy = new DefaultNamingStrategy();
        }

        if (descriptionStrategy == null) {
            descriptionStrategy = new DefaultDescriptionStrategy();
        }

        if (componentVisitor == null) {
            componentVisitor = new DefaultComponentVisitor();
        }

        return new ComponentFinderStrategy(technology, typeMatcher, typeFilter, supportingTypesStrategy, namingStrategy, descriptionStrategy, componentVisitor);
    }

    @Override
    public String toString() {
        return "ComponentFinderStrategyBuilder{" +
                "technology=" + (technology == null ? null : "'" + technology + "'") +
                ", typeMatcher=" + typeMatcher +
                ", typeFilter=" + typeFilter +
                ", supportingTypesStrategy=" + supportingTypesStrategy +
                ", namingStrategy=" + namingStrategy +
                ", descriptionStrategy=" + descriptionStrategy +
                ", componentVisitor=" + componentVisitor +
                '}';
    }

}