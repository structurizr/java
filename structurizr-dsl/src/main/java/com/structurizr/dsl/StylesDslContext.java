package com.structurizr.dsl;

import com.structurizr.view.ColorScheme;

final class StylesDslContext extends DslContext {

    private final ColorScheme colorScheme;

    StylesDslContext() {
        colorScheme = null;
    }

    StylesDslContext(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    ColorScheme getColorScheme() {
        return colorScheme;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.ELEMENT_STYLE_TOKEN,
                StructurizrDslTokens.RELATIONSHIP_STYLE_TOKEN
        };
    }

}