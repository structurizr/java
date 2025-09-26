package com.structurizr.dsl;

import com.structurizr.model.*;

final class InstanceOfParser {

    private static final String GRAMMAR = "instanceOf <identifier> [deploymentGroups] [tags]";

    private static final int IDENTIFIER_INDEX = 1;
    private static final int TAGS_INDEX = 3;

    StaticStructureElementInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // instanceOf <identifier> [tags]
        // instanceOf <identifier> [deploymentGroup] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String elementIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(elementIdentifier);
        if (element == null) {
            throw new RuntimeException("The element \"" + elementIdentifier + "\" does not exist");
        }

        if (element instanceof SoftwareSystem) {
            return new SoftwareSystemInstanceParser().parse(context, tokens);
        } else if (element instanceof Container) {
            return new ContainerInstanceParser().parse(context, tokens);
        } else {
            throw new RuntimeException("The element \"" + elementIdentifier + "\" must be a software system or a container");
        }
    }

}