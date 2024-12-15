package com.structurizr.dsl;

final class ArchetypeParser extends AbstractParser {

    private final static int NAME_INDEX = 1;
    private final static int VALUE_INDEX = 1;

    void parseTag(ArchetypeDslContext context, Tokens tokens) {
        // tag <tag>
        if (tokens.hasMoreThan(VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: tag <tag>");
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: tag <tag>");
        }

        String tag = tokens.get(VALUE_INDEX);
        context.getArchetype().addTags(tag);
    }

    void parseTags(ArchetypeDslContext context, Tokens tokens) {
        // tags <tags> [tags]
        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: tags <tags> [tags]");
        }

        for (int i = NAME_INDEX; i < tokens.size(); i++) {
            String tags = tokens.get(i);
            context.getArchetype().addTags(tags.split(","));
        }
    }

    void parseDescription(ArchetypeDslContext context, Tokens tokens) {
        // description <description>
        if (tokens.hasMoreThan(VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <description>");
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: description <description>");
        }

        String description = tokens.get(VALUE_INDEX);
        context.getArchetype().setDescription(description);
    }

    void parseTechnology(ArchetypeDslContext context, Tokens tokens) {
        // technology <technology>
        if (tokens.hasMoreThan(VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(VALUE_INDEX);
        context.getArchetype().setTechnology(technology);
    }

}