package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;
import com.structurizr.model.Relationship;

import java.util.Set;
import java.util.stream.Collectors;

final class RelationshipsParser extends AbstractParser {

    private static final String GRAMMAR = "!relationships <expression>";

    private final static int EXPRESSION_INDEX = 1;

    Set<Relationship> parse(DslContext context, Tokens tokens) {
        // !relationships <expression>

        if (tokens.hasMoreThan(EXPRESSION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        String expression = tokens.get(1);
        Set<ModelItem> modelItems = new ExpressionParser().parseExpression(expression, context);

        Set<Relationship> relationships = modelItems.stream().filter(mi -> mi instanceof Relationship).map(mi -> (Relationship)mi).collect(Collectors.toSet());


        if (relationships.isEmpty()) {
            throw new RuntimeException("No relationships found for expression \"" + expression + "\"");
        }

        return relationships;
    }

}