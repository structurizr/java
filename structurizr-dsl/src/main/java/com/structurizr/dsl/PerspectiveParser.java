package com.structurizr.dsl;

import com.structurizr.PerspectivesHolder;

final class PerspectiveParser extends AbstractParser {

    private final static int PERSPECTIVE_NAME_INDEX = 0;
    private final static int PERSPECTIVE_DESCRIPTION_INDEX = 1;
    private final static int PERSPECTIVE_VALUE_INDEX = 2;

    void parse(PerspectivesDslContext context, Tokens tokens) {
        // <name> <description> [value]

        if (tokens.hasMoreThan(PERSPECTIVE_VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: <name> <description> [value]");
        }

        if (!tokens.includes(PERSPECTIVE_DESCRIPTION_INDEX)) {
            throw new RuntimeException("Expected: <name> <description> [value]");
        }

        String name = tokens.get(PERSPECTIVE_NAME_INDEX);
        String description = tokens.get(PERSPECTIVE_DESCRIPTION_INDEX);
        String value = "";

        if (tokens.includes(PERSPECTIVE_VALUE_INDEX)) {
            value = tokens.get(PERSPECTIVE_VALUE_INDEX);
        }

        for (PerspectivesHolder perspectivesHolder : context.getPerspectivesHolders()) {
            perspectivesHolder.addPerspective(name, description, value);
        }
    }

}