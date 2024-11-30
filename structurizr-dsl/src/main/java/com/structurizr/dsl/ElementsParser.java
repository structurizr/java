package com.structurizr.dsl;

import com.structurizr.model.*;

final class ElementsParser extends AbstractParser {

    private final static int DESCRIPTION_INDEX = 1;
    private final static int TECHNOLOGY_INDEX = 1;

    void parseDescription(ElementsDslContext context, Tokens tokens) {
        // description <description>
        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <description>");
        }

        if (!tokens.includes(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Expected: description <description>");
        }

        String description = tokens.get(DESCRIPTION_INDEX);
        for (Element element : context.getElements()) {
            element.setDescription(description);
        }
    }

    void parseTechnology(ElementsDslContext context, Tokens tokens) {
        // technology <technology>
        if (tokens.hasMoreThan(TECHNOLOGY_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(TECHNOLOGY_INDEX)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(TECHNOLOGY_INDEX);
        for (Element element : context.getElements()) {
            if (element instanceof Container) {
                ((Container)element).setTechnology(technology);
            } else if (element instanceof Component) {
                ((Component)element).setTechnology(technology);
            } else if (element instanceof DeploymentNode) {
                ((DeploymentNode)element).setTechnology(technology);
            } else if (element instanceof InfrastructureNode) {
                ((InfrastructureNode)element).setTechnology(technology);
            }
        }
    }

}