package com.structurizr.dsl;

import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.SoftwareSystemInstance;

import java.util.HashSet;
import java.util.Set;

final class SoftwareSystemInstanceParser extends StaticStructureInstanceParser {

    private static final String GRAMMAR = "softwareSystemInstance <identifier> [deploymentGroups] [tags]";

    private static final int IDENTIFIER_INDEX = 1;
    private static final int DEPLOYMENT_GROUPS_TOKEN = 2;
    private static final int TAGS_INDEX = 3;

    SoftwareSystemInstance parse(DeploymentNodeDslContext context, Tokens tokens) {
        // softwareSystemInstance <identifier> [tags]
        // softwareSystemInstance <identifier> [deploymentGroup] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(IDENTIFIER_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String softwareSystemIdentifier = tokens.get(IDENTIFIER_INDEX);

        Element element = context.getElement(softwareSystemIdentifier, SoftwareSystem.class);
        if (element == null) {
            throw new RuntimeException("The software system \"" + softwareSystemIdentifier + "\" does not exist");
        }

        DeploymentNode deploymentNode = context.getDeploymentNode();

        Set<String> deploymentGroups = new HashSet<>();
        if (tokens.includes(DEPLOYMENT_GROUPS_TOKEN)) {
            deploymentGroups = getDeploymentGroups(context, tokens.get(DEPLOYMENT_GROUPS_TOKEN));
        }

        SoftwareSystemInstance softwareSystemInstance = deploymentNode.add((SoftwareSystem)element, deploymentGroups.toArray(new String[]{}));

        if (tokens.includes(TAGS_INDEX)) {
            String tags = tokens.get(TAGS_INDEX);
            softwareSystemInstance.addTags(tags.split(","));
        }

        if (context.hasGroup()) {
            softwareSystemInstance.setGroup(context.getGroup().getName());
            context.getGroup().addElement(softwareSystemInstance);
        }

        return softwareSystemInstance;
    }

}