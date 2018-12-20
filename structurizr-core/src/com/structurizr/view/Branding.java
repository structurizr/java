package com.structurizr.view;

import com.structurizr.util.Url;

/**
 * A wrapper for the font, logo and color scheme associated with a corporate branding.
 */
public final class Branding {

    private String logo;

    private Font font;

    Branding() {
    }

    public String getLogo() {
        return logo;
    }

    /**
     * Sets the URL of an image representing a logo.
     *
     * @param url   a URL as a String
     */
    public void setLogo(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url) || url.startsWith("data:image/")) {
                this.logo = url.trim();
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    public Font getFont() {
        return font;
    }

    /**
     * Sets the font to use.
     *
     * @param font  a Font object
     */
    public void setFont(Font font) {
        this.font = font;
    }

}