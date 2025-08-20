package com.structurizr.dsl;

import com.structurizr.model.*;

import java.util.*;

final class NoRelationshipParser extends AbstractRelationshipParser {

    private static final String GRAMMAR = "<identifier> -/> <identifier> [description]";

    private static final int SOURCE_IDENTIFIER_INDEX = 0;
    private static final int DESTINATION_IDENTIFIER_INDEX = 2;
    private static final int DESCRIPTION_IDENTIFIER_INDEX = 3;

    Set<Relationship> parse(DeploymentEnvironmentDslContext context, Tokens tokens) {
        // <identifier> -/> <identifier> [description]

        Set<Relationship> relationships = new HashSet<>();

        if (tokens.hasMoreThan(DESCRIPTION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(DESTINATION_IDENTIFIER_INDEX)) {
            throw new RuntimeException("Not enough tokens, expected: " + GRAMMAR);
        }

        String sourceId = tokens.get(SOURCE_IDENTIFIER_INDEX);
        Element sourceElement = context.getElement(sourceId);
        Set<StaticStructureElementInstance> sourceElements = new HashSet<>();

        if (sourceElement == null) {
            throw new RuntimeException("The source element \"" + sourceId + "\" does not exist");
        } else if (sourceElement instanceof SoftwareSystem) {
            sourceElements = findSoftwareSystemInstances((SoftwareSystem)sourceElement, context.getEnvironment().getName());
        } else if (sourceElement instanceof Container) {
            sourceElements = findContainerInstances((Container)sourceElement, context.getEnvironment().getName());
        } else if (sourceElement instanceof StaticStructureElementInstance) {
            sourceElements.add((StaticStructureElementInstance)sourceElement);
        } else {
            throw new RuntimeException("The source element \"" + sourceId + "\" is not valid - expecting a software system, software system instance, container, or container instance");
        }

        String destinationId = tokens.get(DESTINATION_IDENTIFIER_INDEX);
        Element destinationElement = context.getElement(destinationId);
        Set<StaticStructureElementInstance> destinationElements = new HashSet<>();

        if (destinationElement == null) {
            throw new RuntimeException("The destination element \"" + destinationId + "\" does not exist");
        } else if (destinationElement instanceof SoftwareSystem) {
            destinationElements = findSoftwareSystemInstances((SoftwareSystem)destinationElement, context.getEnvironment().getName());
        } else if (destinationElement instanceof Container) {
            destinationElements = findContainerInstances((Container)destinationElement, context.getEnvironment().getName());
        } else if (destinationElement instanceof StaticStructureElementInstance) {
            destinationElements.add((StaticStructureElementInstance)destinationElement);
        } else {
            throw new RuntimeException("The destination element \"" + destinationId + "\" is not valid - expecting a software system, software system instance, container, or container instance");
        }

        String description = null;

        if (tokens.includes(DESCRIPTION_IDENTIFIER_INDEX)) {
            description = tokens.get(DESCRIPTION_IDENTIFIER_INDEX);
        }

        int count = 0;
        for (Element se : sourceElements) {
            for (Element de : destinationElements) {
                Relationship relationship;

                do {
                    if (description != null) {
                        relationship = se.getEfferentRelationshipWith(de, description);
                    } else {
                        relationship = se.getEfferentRelationshipWith(de);
                    }

                    if (relationship != null && relationship.getLinkedRelationshipId() != null) {
                        context.getWorkspace().remove(relationship);
                        relationships.add(relationship);
                        count++;
                    }
                } while (relationship != null);
            }
        }

        if (count == 0) {
            if (description != null) {
                throw new RuntimeException("A relationship between \"" + sourceId + "\" and \"" + destinationId + "\" with description \"" + description + "\" does not exist");
            } else {
                throw new RuntimeException("A relationship between \"" + sourceId + "\" and \"" + destinationId + "\" does not exist");
            }
        }

        return relationships;
    }

}