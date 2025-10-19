package com.structurizr.dsl;

import com.structurizr.util.FeatureNotEnabledException;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.Url;
import com.structurizr.view.Font;

import java.io.File;

final class BrandingParser extends AbstractParser {

    private static final String LOGO_GRAMMAR = "logo <path|url>";
    private static final String FONT_GRAMMAR = "font <name> [url]";

    private static final int LOGO_FILE_INDEX = 1;

    private static final int FONT_NAME_INDEX = 1;
    private static final int FONT_URL_INDEX = 2;

    void parseLogo(BrandingDslContext context, Tokens tokens) {
        // logo <path>

        if (tokens.hasMoreThan(LOGO_FILE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + LOGO_GRAMMAR);
        } else if (tokens.includes(LOGO_FILE_INDEX)) {
            String path = tokens.get(1);

            if (path.startsWith("data:image/")) {
                ImageUtils.validateImage(path);
                context.getWorkspace().getViews().getConfiguration().getBranding().setLogo(path);
            } else if (Url.isHttpsUrl(path)) {
                if (context.getFeatures().isEnabled(Features.HTTPS)) {
                    ImageUtils.validateImage(path);
                    context.getWorkspace().getViews().getConfiguration().getBranding().setLogo(path);
                } else {
                    throw new FeatureNotEnabledException(Features.HTTPS, "Icons via HTTPS are not permitted");
                }
            } else if (Url.isHttpUrl(path)) {
                if (context.getFeatures().isEnabled(Features.HTTP)) {
                    ImageUtils.validateImage(path);
                    context.getWorkspace().getViews().getConfiguration().getBranding().setLogo(path);
                } else {
                    throw new FeatureNotEnabledException(Features.HTTP, "Icons via HTTP are not permitted");
                }
            } else {
                if (context.getFeatures().isEnabled(Features.FILE_SYSTEM)) {
                    File file = new File(context.getFile().getParent(), path);
                    if (file.exists() && !file.isDirectory()) {
                        context.setDslPortable(false);
                        try {
                            String dataUri = ImageUtils.getImageAsDataUri(file);
                            context.getWorkspace().getViews().getConfiguration().getBranding().setLogo(dataUri);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new RuntimeException(path + " does not exist");
                    }
                } else {
                    throw new FeatureNotEnabledException(Features.FILE_SYSTEM, "!branding <file> is not permitted");
                }
            }
        } else {
            throw new RuntimeException("Expected: " + LOGO_GRAMMAR);
        }
    }

    void parseFont(BrandingDslContext context, Tokens tokens) {
        // font <name> [url]

        if (tokens.hasMoreThan(FONT_URL_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + FONT_GRAMMAR);
        } else if (tokens.includes(FONT_URL_INDEX)) {
            String name = tokens.get(FONT_NAME_INDEX);
            String url = tokens.get(FONT_URL_INDEX);

            context.getWorkspace().getViews().getConfiguration().getBranding().setFont(new Font(name, url));
        } else if (tokens.includes(FONT_NAME_INDEX)) {
            String name = tokens.get(FONT_NAME_INDEX);

            context.getWorkspace().getViews().getConfiguration().getBranding().setFont(new Font(name));
        } else {
            throw new RuntimeException("Expected: " + FONT_GRAMMAR);
        }
    }

}