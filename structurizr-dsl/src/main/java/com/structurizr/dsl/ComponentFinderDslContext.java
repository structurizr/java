package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.model.Container;

final class ComponentFinderDslContext extends DslContext {

    private final ComponentFinderBuilder componentFinderBuilder = new ComponentFinderBuilder();

    ComponentFinderDslContext(Container container) {
        componentFinderBuilder.forContainer(container);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.COMPONENT_FINDER_CLASSES_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_SOURCE_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_TOKEN
        };
    }

    ComponentFinderBuilder getComponentFinderBuilder() {
        return this.componentFinderBuilder;
    }

    @Override
    void end() {
        componentFinderBuilder.build().findComponents();
    }

}