package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.model.Component;
import com.structurizr.model.Container;

import java.util.Set;

final class ComponentFinderDslContext extends DslContext {

    private final ComponentFinderBuilder componentFinderBuilder = new ComponentFinderBuilder();

    private final StructurizrDslParser dslParser;

    ComponentFinderDslContext(StructurizrDslParser dslParser, Container container) {
        this.dslParser = dslParser;
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
        Set<Component> components = componentFinderBuilder.build().findComponents();
        for (Component component : components) {
            dslParser.registerIdentifier(IdentifiersRegister.toIdentifier(component.getName()), component);
        }
    }

}