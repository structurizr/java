package com.structurizr.view;

import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

import javax.annotation.Nullable;

/**
 * A wrapper for the font, logo and color scheme associated with a corporate branding.
 */
public final class Branding {

    @Nullable
    private String logo;

    @Nullable
    private Font font;

    Branding() {
    }

    @Nullable
    public String getLogo() {
        return logo;
    }

    /**
     * Sets the URL of an image representing a logo.
     *
     * @param logo   a URL or data URI as a String
     */
    public void setLogo(@Nullable String logo) {
        if (StringUtils.isNullOrEmpty(logo)) {
            this.logo = null;
        } else {
            ImageUtils.validateImage(logo);
            this.logo = logo.trim();
        }
    }

    @Nullable
    public Font getFont() {
        return font;
    }

    /**
     * Sets the font to use.
     *
     * @param font  a Font object
     */
    public void setFont(@Nullable Font font) {
        this.font = font;
    }

}
