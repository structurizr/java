package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderStrategyBuilder;

final class ComponentFinderStrategyDslContext extends DslContext {

    private final ComponentFinderDslContext componentFinderDslContext;
    private final ComponentFinderStrategyBuilder componentFinderStrategyBuilder = new ComponentFinderStrategyBuilder();

    ComponentFinderStrategyDslContext(ComponentFinderDslContext componentFinderDslContext) {
        this.componentFinderDslContext = componentFinderDslContext;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_TECHNOLOGY_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_MATCHER_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_FILTER_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_SUPPORTING_TYPES_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_NAME_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_FOREACH_TOKEN
        };
    }

    ComponentFinderStrategyBuilder getComponentFinderStrategyBuilder() {
        return this.componentFinderStrategyBuilder;
    }

    ComponentFinderDslContext getComponentFinderDslContext() {
        return this.componentFinderDslContext;
    }

    @Override
    void end() {
        componentFinderDslContext.getComponentFinderBuilder().withStrategy(componentFinderStrategyBuilder.build());
    }

}