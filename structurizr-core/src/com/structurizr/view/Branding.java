package com.structurizr.view;

import com.structurizr.util.Url;

/**
 * A wrapper for the font, logo and color scheme associated with a corporate branding.
 */
public final class Branding {

    private String logo;

    private Font font;

    private ColorPair color1;
    private ColorPair color2;
    private ColorPair color3;
    private ColorPair color4;
    private ColorPair color5;

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
                this.logo = url;
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

    public ColorPair getColor1() {
        return color1;
    }

    public void setColor1(ColorPair color1) {
        this.color1 = color1;
    }

    public ColorPair getColor2() {
        return color2;
    }

    public void setColor2(ColorPair color2) {
        this.color2 = color2;
    }

    public ColorPair getColor3() {
        return color3;
    }

    public void setColor3(ColorPair color3) {
        this.color3 = color3;
    }

    public ColorPair getColor4() {
        return color4;
    }

    public void setColor4(ColorPair color4) {
        this.color4 = color4;
    }

    public ColorPair getColor5() {
        return color5;
    }

    public void setColor5(ColorPair color5) {
        this.color5 = color5;
    }

}
