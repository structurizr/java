package com.structurizr.dsl;

import java.util.ArrayList;
import java.util.List;

final class ComponentFinderStrategyForEachDslContext extends DslContext {

    private final List<String> dslLines = new ArrayList<>();

    ComponentFinderStrategyForEachDslContext(ComponentFinderStrategyDslContext dslContext, StructurizrDslParser dslParser) {
        dslContext.getComponentFinderStrategyBuilder().forEach(component -> {
            try {
                ContainerDslContext containerDslContext = dslContext.getComponentFinderDslContext().getContainerDslContext();
                if (containerDslContext.hasGroup()) {
                    component.setGroup(containerDslContext.getGroup().getName());
                    containerDslContext.getGroup().addElement(component);
                }

                dslParser.parse(dslLines, new ComponentDslContext(component));
            } catch (StructurizrDslParserException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void addLine(String line) {
        this.dslLines.add(line);
    }

    @Override
    protected String[] getPermittedTokens() {
        return new ComponentDslContext(null).getPermittedTokens();
    }

}