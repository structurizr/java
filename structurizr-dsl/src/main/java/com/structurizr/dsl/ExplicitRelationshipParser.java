package com.structurizr.dsl;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import javax.lang.model.util.Elements;
import java.util.LinkedHashSet;
import java.util.Set;

final class ExplicitRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "<identifier> -> <identifier> [description] [technology] [tags]";

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private final static int DESCRIPTION_INDEX = 3;
    private final static int TECHNOLOGY_INDEX = 4;
    private final static int TAGS_INDEX = 5;

    Relationship parse(DslContext context, Tokens tokens, Archetype archetype) {
        // <identifier> -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        Element sourceElement = findElement(sourceId, context);
        if (sourceElement == null) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        }

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);
        Element destinationElement = findElement(destinationId, context);
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

        return createRelationship(sourceElement, description, technology, tags, destinationElement);
    }

    Set<Relationship> parse(ElementsDslContext context, Tokens tokens, Archetype archetype) {
        // <identifier> -> <identifier> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        Set<Element> sourceElements = findElements(sourceId, context);
        if (sourceElements.isEmpty()) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        }

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);
        Set<Element> destinationElements = findElements(destinationId, context);
        if (destinationElements.isEmpty()) {
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
            for (Element destinationElement : destinationElements) {
                relationships.add(createRelationship(sourceElement, description, technology, tags, destinationElement));
            }
        }

        return relationships;
   }

    private Element findElement(String identifier, DslContext context) {
        Element element = context.getElement(identifier);

        if (element == null && StructurizrDslTokens.THIS_TOKEN.equalsIgnoreCase(identifier) && context instanceof ElementDslContext) {
            element = ((ElementDslContext)context).getElement();
        }

        return element;
    }

    private Set<Element> findElements(String identifier, ElementsDslContext context) {
        Element element = context.getElement(identifier);
        Set<Element> elements = new LinkedHashSet<>();

        if (element == null) {
            if (StructurizrDslTokens.THIS_TOKEN.equalsIgnoreCase(identifier)) {
                elements.addAll(context.getElements());
            }
        } else {
            elements.add(element);
        }

        return elements;
    }

}