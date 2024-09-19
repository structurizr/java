package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

final class FindRelationshipParser extends AbstractParser {

    private static final String GRAMMAR = "!relationship <identifier|canonical name>";

    private final static int IDENTIFIER_INDEX = 1;

    Relationship parse(DslContext context, Tokens tokens) {
        // !relationship <identifier|canonical name>

        if (tokens.hasMoreThan(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Relationship relationship;

        String s = tokens.get(IDENTIFIER_INDEX);
        if (s.startsWith("Relationship://")) {
            relationship = context.getWorkspace().getModel().getRelationshipWithCanonicalName(s);
        } else {
            relationship = context.getRelationship(s);
        }

        if (relationship == null) {
            throw new RuntimeException("A relationship identified by \"" + s + "\" could not be found");
        }

        return relationship;
    }

}