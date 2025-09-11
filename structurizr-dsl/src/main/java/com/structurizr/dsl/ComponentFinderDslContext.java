package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.model.Component;

import java.util.Set;

final class ComponentFinderDslContext extends DslContext {

    private final ComponentFinderBuilder componentFinderBuilder = new ComponentFinderBuilder();

    private final StructurizrDslParser dslParser;
    private final ContainerDslContext containerDslContext;

    ComponentFinderDslContext(StructurizrDslParser dslParser, ContainerDslContext containerDslContext) {
        this.dslParser = dslParser;
        this.containerDslContext = containerDslContext;
        componentFinderBuilder.forContainer(containerDslContext.getContainer());
        setDslPortable(false);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
            StructurizrDslTokens.COMPONENT_FINDER_CLASSES_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_SOURCE_TOKEN,
            StructurizrDslTokens.COMPONENT_FINDER_STRATEGY_TOKEN
        };
    }

    ContainerDslContext getContainerDslContext() {
        return containerDslContext;
    }

    ComponentFinderBuilder getComponentFinderBuilder() {
        return this.componentFinderBuilder;
    }

    @Override
    void end() {
        Set<Component> components = componentFinderBuilder.build().run();
        for (Component component : components) {
            dslParser.registerIdentifier(IdentifiersRegister.toIdentifier(component.getName()), component);
        }
    }

}