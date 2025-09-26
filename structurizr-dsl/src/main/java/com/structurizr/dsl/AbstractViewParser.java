package com.structurizr.dsl;

import java.util.regex.Pattern;

abstract class AbstractViewParser extends AbstractParser {

    private static final String PERMITTED_CHARACTERS_IN_VIEW_KEY = "a-zA-Z0-9_-";
    private static final Pattern VIEW_KEY_PATTERN = Pattern.compile("[" + PERMITTED_CHARACTERS_IN_VIEW_KEY + "]+");

    void validateViewKey(String key) {
        if (!VIEW_KEY_PATTERN.matcher(key).matches()) {
            throw new RuntimeException("View keys can only contain the following characters: a-zA-Z0-9_-");
        }
    }

}