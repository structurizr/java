package com.structurizr.dsl;

final class ModelItemParser extends AbstractParser {

    private final static int DESCRIPTION_INDEX = 1;

    private final static int TAGS_INDEX = 1;

    private final static int URL_INDEX = 1;

    void parseTags(ModelItemDslContext context, Tokens tokens) {
        // tags <tags> [tags]
        if (!tokens.includes(TAGS_INDEX)) {
            throw new RuntimeException("Expected: tags <tags> [tags]");
        }

        for (int i = TAGS_INDEX; i < tokens.size(); i++) {
            String tags = tokens.get(i);
            context.getModelItem().addTags(tags.split(","));
        }
    }

    void parseDescription(ElementDslContext context, Tokens tokens) {
        // description <description>
        if (tokens.hasMoreThan(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: description <description>");
        }

        if (!tokens.includes(DESCRIPTION_INDEX)) {
            throw new RuntimeException("Expected: description <description>");
        }

        String description = tokens.get(DESCRIPTION_INDEX);
        context.getElement().setDescription(description);
    }

    void parseUrl(ModelItemDslContext context, Tokens tokens) {
        // url <url>
        if (tokens.hasMoreThan(URL_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: url <url>");
        }

        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: url <url>");
        }

        String url = tokens.get(URL_INDEX);
        context.getModelItem().setUrl(url);
    }

}