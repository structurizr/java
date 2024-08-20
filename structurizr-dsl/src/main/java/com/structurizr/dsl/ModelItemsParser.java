package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

final class ModelItemsParser extends AbstractParser {

    private final static int TAGS_INDEX = 1;

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

}