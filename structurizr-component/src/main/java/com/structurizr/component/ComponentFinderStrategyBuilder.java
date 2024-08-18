package com.structurizr.component;

import com.structurizr.component.filter.DefaultTypeFilter;
import com.structurizr.component.filter.TypeFilter;
import com.structurizr.component.matcher.TypeMatcher;
import com.structurizr.component.naming.DefaultNamingStrategy;
import com.structurizr.component.naming.NamingStrategy;
import com.structurizr.component.supporting.DefaultSupportingTypesStrategy;
import com.structurizr.component.supporting.SupportingTypesStrategy;

/**
 * Provides a way to create a {@link ComponentFinderStrategy} instance.
 */
public final class ComponentFinderStrategyBuilder {

    private TypeMatcher typeMatcher;
    private TypeFilter typeFilter = new DefaultTypeFilter();
    private SupportingTypesStrategy supportingTypesStrategy = new DefaultSupportingTypesStrategy();
    private NamingStrategy namingStrategy = new DefaultNamingStrategy();

    public ComponentFinderStrategyBuilder() {
    }

    public ComponentFinderStrategyBuilder matchedBy(TypeMatcher typeMatcher) {
        this.typeMatcher = typeMatcher;

        return this;
    }

    public ComponentFinderStrategyBuilder filteredBy(TypeFilter typeFilter) {
        this.typeFilter = typeFilter;

        return this;
    }

    public ComponentFinderStrategyBuilder supportedBy(SupportingTypesStrategy supportingTypesStrategy) {
        this.supportingTypesStrategy = supportingTypesStrategy;

        return this;
    }

    public ComponentFinderStrategyBuilder namedBy(NamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;

        return this;
    }

    public ComponentFinderStrategy build() {
        if (typeMatcher == null) {
            throw new RuntimeException("A type matcher must be specified");
        }

        return new ComponentFinderStrategy(typeMatcher, typeFilter, supportingTypesStrategy, namingStrategy);
    }

}