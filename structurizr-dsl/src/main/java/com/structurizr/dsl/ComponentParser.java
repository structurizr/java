package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.model.Container;

final class ComponentParser extends AbstractParser {

    private static final String GRAMMAR = "component <name> [description] [technology] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TECHNOLOGY_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    Component parse(ContainerDslContext context, Tokens tokens, Archetype archetype) {
        // component <name> [description] [technology] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        Container container = context.getContainer();
        Component component = null;
        String name = tokens.get(NAME_INDEX);

        if (context.isExtendingWorkspace()) {
            component = container.getComponentWithName(name);
        }

        if (component == null) {
            component = container.addComponent(name);
        }

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }
        component.setDescription(description);

        String technology = archetype.getTechnology();
        if (tokens.includes(TECHNOLOGY_INDEX)) {
            technology = tokens.get(TECHNOLOGY_INDEX);
        }
        component.setTechnology(technology);

        String[] tags = archetype.getTags().toArray(new String[0]);
        if (tokens.includes(TAGS_INDEX)) {
            tags = tokens.get(TAGS_INDEX).split(",");
        }
        component.addTags(tags);

        component.addProperties(archetype.getProperties());

        if (context.hasGroup()) {
            component.setGroup(context.getGroup().getName());
            context.getGroup().addElement(component);
        }

        return component;
    }

    void parseTechnology(ComponentDslContext context, Tokens tokens) {
        int index = 1;

        // technology <technology>
        if (tokens.hasMoreThan(index)) {
            throw new RuntimeException("Too many tokens, expected: technology <technology>");
        }

        if (!tokens.includes(index)) {
            throw new RuntimeException("Expected: technology <technology>");
        }

        String technology = tokens.get(index);
        context.getComponent().setTechnology(technology);
    }

}