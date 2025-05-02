package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class DeploymentNodeParser extends AbstractParser {

    private static final String GRAMMAR = "deploymentNode <name> [description] [technology] [tags] [instances] {";

    private static final int NAME_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int TECHNOLOGY_INDEX = 3;
    private static final int TAGS_INDEX = 4;
    private static final int INSTANCES_INDEX = 5;

    DeploymentNode parse(DeploymentEnvironmentDslContext context, Tokens tokens, Archetype archetype) {
        return parse(context, null, tokens, archetype);
    }

    DeploymentNode parse(DeploymentNodeDslContext context, Tokens tokens, Archetype archetype) {
        return parse(null, context, tokens, archetype);
    }

    DeploymentNode parse(DeploymentEnvironmentDslContext deploymentEnvironmentDslContext, DeploymentNodeDslContext deploymentNodeDslContext, Tokens tokens, Archetype archetype) {
        // deploymentNode <name> [description] [technology] [tags] [instances]

        if (tokens.hasMoreThan(INSTANCES_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        DeploymentNode deploymentNode = null;
        String name = tokens.get(NAME_INDEX);

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        String technology = archetype.getTechnology();
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }

        if (deploymentEnvironmentDslContext != null) {
            // add a root deployment node
            deploymentNode = deploymentEnvironmentDslContext.getWorkspace().getModel().addDeploymentNode(deploymentEnvironmentDslContext.getEnvironment().getName(), name, description, technology);

            if (deploymentEnvironmentDslContext.hasGroup()) {
                deploymentNode.setGroup(deploymentEnvironmentDslContext.getGroup().getName());
                deploymentEnvironmentDslContext.getGroup().addElement(deploymentNode);
            }
        } else {
            deploymentNode = deploymentNodeDslContext.getDeploymentNode().addDeploymentNode(name, description, technology);

            if (deploymentNodeDslContext.hasGroup()) {
                deploymentNode.setGroup(deploymentNodeDslContext.getGroup().getName());
                deploymentNodeDslContext.getGroup().addElement(deploymentNode);
            }
        }

        List<String> tags = new ArrayList<>(archetype.getTags());
        if (tokens.includes(TAGS_INDEX)) {
            tags.addAll(Arrays.asList(tokens.get(TAGS_INDEX).split(",")));
        }
        deploymentNode.addTags(tags.toArray(new String[0]));

        deploymentNode.addProperties(archetype.getProperties());
        deploymentNode.addPerspectives(archetype.getPerspectives());

        String instances = "1";
        if (tokens.includes(INSTANCES_INDEX)) {
            instances = tokens.get(INSTANCES_INDEX);
            deploymentNode.setInstances(instances);
        }

        return deploymentNode;
    }

    void parseTechnology(DeploymentNodeDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getDeploymentNode().setTechnology(technology);
    }

    void parseInstances(DeploymentNodeDslContext context, Tokens tokens) {
        int index = 1;

        // instances <number|range>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: instances <number|range>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: instances <number|range>");
        }

        String instances = tokens.get(index);
        context.getDeploymentNode().setInstances(instances);
    }

}