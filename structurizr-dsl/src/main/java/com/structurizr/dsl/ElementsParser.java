package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.ModelItem;

import java.util.Set;
import java.util.stream.Collectors;

final class ElementsParser extends AbstractParser {

    private static final String GRAMMAR = "!elements <expression>";

    private final static int EXPRESSION_INDEX = 1;

    Set<Element> parse(DslContext context, Tokens tokens) {
        // !elements <expression>

        if (tokens.hasMoreThan(EXPRESSION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        String expression = tokens.get(1);
        Set<ModelItem> modelItems = new ExpressionParser().parseExpression(expression, context);

        return modelItems.stream().filter(mi -> mi instanceof Element).map(mi -> (Element)mi).collect(Collectors.toSet());
    }

}