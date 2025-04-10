package com.structurizr.dsl;

import com.structurizr.model.SoftwareSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class SoftwareSystemParser extends AbstractParser {

    private static final String GRAMMAR = "softwareSystem <name> [description] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int DESCRIPTION_INDEX = 2;
    private final static int TAGS_INDEX = 3;

    SoftwareSystem parse(ModelDslContext context, Tokens tokens, Archetype archetype) {
        // softwareSystem <name> [description] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        SoftwareSystem softwareSystem = null;
        String name = tokens.get(NAME_INDEX);

        if (context.isExtendingWorkspace()) {
            softwareSystem = context.getWorkspace().getModel().getSoftwareSystemWithName(name);
        }

        if (softwareSystem == null) {
            softwareSystem = context.getWorkspace().getModel().addSoftwareSystem(name);
        }

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }
        softwareSystem.setDescription(description);

        List<String> tags = new ArrayList<>(archetype.getTags());
        if (tokens.includes(TAGS_INDEX)) {
            tags.addAll(Arrays.asList(tokens.get(TAGS_INDEX).split(",")));
        }
        softwareSystem.addTags(tags.toArray(new String[0]));

        softwareSystem.addProperties(archetype.getProperties());
        softwareSystem.addPerspectives(archetype.getPerspectives());

        if (context.hasGroup()) {
            softwareSystem.setGroup(context.getGroup().getName());
            context.getGroup().addElement(softwareSystem);
        }

        return softwareSystem;
    }

}