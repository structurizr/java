package com.structurizr.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final class ComponentFinderStrategyForEachDslContext extends DslContext {

    private final List<String> dslLines = new ArrayList<>();

    ComponentFinderStrategyForEachDslContext(ComponentFinderStrategyDslContext dslContext, StructurizrDslParser dslParser) {
        dslContext.getComponentFinderStrategyBuilder().forEach(component -> {
            try {
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
        return new String[] {};
    }

}