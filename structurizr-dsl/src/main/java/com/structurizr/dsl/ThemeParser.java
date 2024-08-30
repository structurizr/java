package com.structurizr.dsl;

import com.structurizr.util.Url;
import com.structurizr.view.ThemeUtils;

import java.io.File;

final class ThemeParser extends AbstractParser {

    private static final String DEFAULT_THEME_NAME = "default";
    private static final String DEFAULT_THEME_URL = "https://static.structurizr.com/themes/default/theme.json";

    private final static int FIRST_THEME_INDEX = 1;

    void parseTheme(DslContext context, File dslFile, Tokens tokens) {
        // theme <default|url|file>
        if (tokens.hasMoreThan(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: theme <url|file>");
        }

        if (!tokens.includes(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Expected: theme <url|file>");
        }

        addTheme(context, dslFile, tokens.get(FIRST_THEME_INDEX));
    }

    void parseThemes(DslContext context, File dslFile, Tokens tokens) {
        // themes <url|file> [url|file] ... [url|file]
        if (!tokens.includes(FIRST_THEME_INDEX)) {
            throw new RuntimeException("Expected: themes <url|file> [url|file] ... [url|file]");
        }

        for (int i = FIRST_THEME_INDEX; i < tokens.size(); i++) {
            addTheme(context, dslFile, tokens.get(i));
        }
    }

    private void addTheme(DslContext context, File dslFile, String theme) {
        if (DEFAULT_THEME_NAME.equalsIgnoreCase(theme)) {
            theme = DEFAULT_THEME_URL;
        }

        if (Url.isUrl(theme)) {
            // this adds the theme to the list of theme URLs in the workspace
            context.getWorkspace().getViews().getConfiguration().addTheme(theme);
        } else {
            // this inlines the file-based theme into the workspace
            File file = new File(dslFile.getParentFile(), theme);
            if (file.exists()) {
                if (file.isFile()) {
                    try {
                        ThemeUtils.inlineTheme(context.getWorkspace(), file);
                    } catch (Exception e) {
                        throw new RuntimeException("Error loading theme from " + file.getAbsolutePath() + ": " + e.getMessage());
                    }
                } else {
                    throw new RuntimeException(file.getAbsolutePath() + " is not a file");
                }
            } else {
                throw new RuntimeException(file.getAbsolutePath() + " does not exist");
            }
        }
    }

}