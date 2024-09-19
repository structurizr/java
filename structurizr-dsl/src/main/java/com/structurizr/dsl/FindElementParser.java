package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.StaticStructureElement;

final class FindElementParser extends AbstractParser {

    private static final String GRAMMAR = "!element <identifier|canonical name>";

    private final static int IDENTIFIER_INDEX = 1;

    Element parse(DslContext context, Tokens tokens) {
        // !element <identifier|canonical name>

        if (tokens.hasMoreThan(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Element element;

        String s = tokens.get(IDENTIFIER_INDEX);
        if (s.contains("://")) {
            element = context.getWorkspace().getModel().getElementWithCanonicalName(s);
        } else {
            element = context.getElement(s);
        }

        if (element == null) {
            throw new RuntimeException("An element identified by \"" + s + "\" could not be found");
        }

        if (context instanceof GroupableDslContext && element instanceof StaticStructureElement) {
            GroupableDslContext groupableDslContext = (GroupableDslContext)context;
            StaticStructureElement staticStructureElement = (StaticStructureElement)element;
            if (groupableDslContext.hasGroup()) {
                staticStructureElement.setGroup(groupableDslContext.getGroup().getName());
            }
        }

        return element;
    }

}