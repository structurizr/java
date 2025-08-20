package com.structurizr.dsl;

import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.util.*;

final class ImplicitRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "-> <identifier> [description] [technology] [tags]";

    private static final int DESTINATION_IDENTIFIER_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TECHNOLOGY_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    Set<Relationship> parse(ElementDslContext context, Tokens tokens, Archetype archetype) {
        // -> <identifier> [description] [technology] [tags]

        Set<Relationship> relationships = new HashSet<>();

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

        List<String> tags = new ArrayList<>(archetype.getTags());
        if (tokens.includes(TAGS_INDEX)) {
            tags.addAll(Arrays.asList(tokens.get(TAGS_INDEX).split(",")));
        }

        Set<Element> sourceElements = new HashSet<>();
        Set<Element> destinationElements = new HashSet<>();

        if (context instanceof InfrastructureNodeDslContext) {
            String deploymentEnvironment = ((InfrastructureNodeDslContext)context).getInfrastructureNode().getEnvironment();

            sourceElements.add(sourceElement);

            if (destinationElement instanceof SoftwareSystem) {
                // find the software system instances in the deployment environment
                destinationElements = findSoftwareSystemInstances((SoftwareSystem)destinationElement, deploymentEnvironment);
            } else if (destinationElement instanceof Container) {
                // find the container instances in the deployment environment
                destinationElements = findContainerInstances((Container)destinationElement, deploymentEnvironment);
            } else {
                destinationElements.add(destinationElement);
            }

            for (Element se : sourceElements) {
                for (Element de : destinationElements) {
                    Relationship relationship = createRelationship(se, description, technology, tags.toArray(new String[0]), de);
                    relationship.addProperties(archetype.getProperties());
                    relationship.addPerspectives(archetype.getPerspectives());

                    relationships.add(relationship);
                }
            }
        } else {
            Relationship relationship = createRelationship(sourceElement, description, technology, tags.toArray(new String[0]), destinationElement);
            relationship.addProperties(archetype.getProperties());
            relationship.addPerspectives(archetype.getPerspectives());

            relationships.add(relationship);
        }

        return relationships;
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
            relationship.addPerspectives(archetype.getPerspectives());

            relationships.add(relationship);
        }

        return relationships;
    }

}