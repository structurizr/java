package com.structurizr.dsl;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Location;
import com.structurizr.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class CustomElementParser extends AbstractParser {

    private static final String GRAMMAR = "element <name> [metadata] [description] [tags]";

    private final static int NAME_INDEX = 1;
    private final static int METADATA_INDEX = 2;
    private final static int DESCRIPTION_INDEX = 3;
    private final static int TAGS_INDEX = 4;

    CustomElement parse(ModelDslContext context, Tokens tokens, Archetype archetype) {
        // element <name> [metadata] [description] [tags]

        if (tokens.hasMoreThan(TAGS_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        String name = tokens.get(NAME_INDEX);

        String metadata = archetype.getMetadata();
        if (tokens.includes(METADATA_INDEX)) {
            metadata = tokens.get(METADATA_INDEX);
        }

        String description = archetype.getDescription();
        if (tokens.includes(DESCRIPTION_INDEX)) {
            description = tokens.get(DESCRIPTION_INDEX);
        }

        CustomElement customElement = context.getWorkspace().getModel().addCustomElement(name, metadata, description);

        List<String> tags = new ArrayList<>(archetype.getTags());
        if (tokens.includes(TAGS_INDEX)) {
            tags.addAll(Arrays.asList(tokens.get(TAGS_INDEX).split(",")));
        }
        customElement.addTags(tags.toArray(new String[0]));

        if (context.hasGroup()) {
            customElement.setGroup(context.getGroup().getName());
        }

        return customElement;
    }

}