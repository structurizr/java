package com.structurizr.dsl;

final class ComponentFinderParser extends AbstractParser {

    private static final String CLASSES_GRAMMAR = "classes <path>";
    private static final String SOURCE_GRAMMAR = "source <path>";

    void parseClasses(ComponentFinderDslContext context, Tokens tokens) {
        // classes <path>

        if (tokens.hasMoreThan(1)) {
            throw new RuntimeException("Too many tokens, expected: " + CLASSES_GRAMMAR);
        }

        context.getComponentFinderBuilder().fromClasses(tokens.get(1));
    }

    void parseSource(ComponentFinderDslContext context, Tokens tokens) {
        // source <path>

        if (tokens.hasMoreThan(1)) {
            throw new RuntimeException("Too many tokens, expected: " + SOURCE_GRAMMAR);
        }

        context.getComponentFinderBuilder().fromSource(tokens.get(1));
    }

}