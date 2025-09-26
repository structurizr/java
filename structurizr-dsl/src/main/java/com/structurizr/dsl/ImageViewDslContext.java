package com.structurizr.dsl;

import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ImageView;

import java.io.IOException;

class ImageViewDslContext extends ViewDslContext {

    private ColorScheme colorScheme;

    ImageViewDslContext(ImageView view) {
        super(view);
    }

    ImageView getView() {
        return (ImageView)super.getView();
    }

    ColorScheme getColorScheme() {
        return colorScheme;
    }

    void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    @Override
    protected String[] getPermittedTokens() {
        return new String[] {
                StructurizrDslTokens.VIEW_TITLE_TOKEN,
                StructurizrDslTokens.VIEW_DESCRIPTION_TOKEN,
                StructurizrDslTokens.PLANTUML_TOKEN,
                StructurizrDslTokens.MERMAID_TOKEN,
                StructurizrDslTokens.KROKI_TOKEN,
                StructurizrDslTokens.IMAGE_TOKEN
        };
    }

    @Override
    void end() {
        super.end();

        if (colorScheme != null) {
            colorScheme = null;
        }

        if (!getView().hasContent()) {
            throw new RuntimeException("The image view \"" + getView().getKey() + "\" has no content");
        }
    }
}