package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.LinkedHashSet;
import java.util.Set;

final class ImplicitRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "-> <identifier> [description] [technology] [tags]";

    private static final int DESTINATION_IDENTIFIER_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TECHNOLOGY_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    Relationship parse(ElementDslContext context, Tokens tokens, Archetype archetype) {
        // -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);

        Element sourceElement = context.getElement();

        Element destinationElement = context.getElement(destinationId);
        if (destinationElement == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        }

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = archetype.getTechnology();
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        String[] tags = archetype.getTags().toArray(new String[0]);
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX).split(",");
        }

        Relationship relationship = createRelationship(sourceElement, description, technology, tags, destinationElement);
        relationship.addProperties(archetype.getProperties());

        return relationship;
    }

    Set<Relationship> parse(ElementsDslContext context, Tokens tokens, Archetype archetype) {
        // -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Set<Element> sourceElements = context.getElements();

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);
        Element destinationElement = context.getElement(destinationId);
        if (destinationElement == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        }

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = archetype.getTechnology();
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        String[] tags = archetype.getTags().toArray(new String[0]);
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX).split(",");
        }

        Set<Relationship> relationships = new LinkedHashSet<>();
        for (Element sourceElement : sourceElements) {
            Relationship relationship = createRelationship(sourceElement, description, technology, tags, destinationElement);
            relationship.addProperties(archetype.getProperties());

            relationships.add(relationship);
        }

        return relationships;
    }

}