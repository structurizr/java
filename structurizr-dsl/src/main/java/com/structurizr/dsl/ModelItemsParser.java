package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

final class ModelItemsParser extends AbstractParser {

    private final static int TAGS_INDEX = 1;
    private final static int URL_INDEX = 1;

    void parseTags(ModelItemsDslContext context, Tokens tokens) {
        // tags <tags> [tags]
        if (!tokens.includes(TAGS_INDEX)) {
            throw new RuntimeException("Expected: tags <tags> [tags]");
        }

        for (int i = TAGS_INDEX; i < tokens.size(); i++) {
            String tags = tokens.get(i);

            for (ModelItem modelItem : context.getModelItems()) {
                modelItem.addTags(tags.split(","));
            }
        }
    }

    void parseUrl(ModelItemsDslContext context, Tokens tokens) {
        // url <url>
        if (tokens.hasMoreThan(URL_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: url <url>");
        }

        if (!tokens.includes(URL_INDEX)) {
            throw new RuntimeException("Expected: url <url>");
        }

        String url = tokens.get(URL_INDEX);
        for (ModelItem modelItem : context.getModelItems()) {
            modelItem.setUrl(url);
        }
    }

}