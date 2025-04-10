package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.InfrastructureNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class InfrastructureNodeParser extends AbstractParser {

    private static final String GRAMMAR = "infrastructureNode <name> [description] [technology] [tags]";

    private static final int NAME_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int TECHNOLOGY_INDEX = 3;
    private static final int TAGS_INDEX = 4;

    InfrastructureNode parse(DeploymentNodeDslContext context, Tokens tokens, Archetype archetype) {
        // infrastructureNode <name> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        DeploymentNode deploymentNode = context.getDeploymentNode();
        InfrastructureNode infrastructureNode;
        String name = tokens.get(NAME_INDEX);

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = archetype.getTechnology();
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        infrastructureNode = deploymentNode.addInfrastructureNode(name, description, technology);

        List<String> tags = new ArrayList<>(archetype.getTags());
        if (tokens.includes(TAGS_INDEX)) {
            tags.addAll(Arrays.asList(tokens.get(TAGS_INDEX).split(",")));
        }
        infrastructureNode.addTags(tags.toArray(new String[0]));

        infrastructureNode.addProperties(archetype.getProperties());
        infrastructureNode.addPerspectives(archetype.getPerspectives());

        if (context.hasGroup()) {
            infrastructureNode.setGroup(context.getGroup().getName());
            context.getGroup().addElement(infrastructureNode);
        }

        return infrastructureNode;
    }

    void parseTechnology(InfrastructureNodeDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getInfrastructureNode().setTechnology(technology);
    }

}