package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.ComponentFinderStrategyBuilder;

final class ComponentFinderStrategyDslContext extends DslContext {

    private final ComponentFinderBuilder componentFinderBuilder;
    private final ComponentFinderStrategyBuilder componentFinderStrategyBuilder = new ComponentFinderStrategyBuilder();

    ComponentFinderStrategyDslContext(ComponentFinderBuilder componentFinderBuilder) {
        this.componentFinderBuilder = componentFinderBuilder;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_TECHNOLOGY_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_MATCHER_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_FILTER_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_SUPPORTING_TYPES_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_NAMING_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_FOREACH_TOKEN
        };
    }

    ComponentFinderStrategyBuilder getComponentFinderStrategyBuilder() {
        return this.componentFinderStrategyBuilder;
    }

    @Override
    void end() {
        componentFinderBuilder.withStrategy(componentFinderStrategyBuilder.build());
    }

}