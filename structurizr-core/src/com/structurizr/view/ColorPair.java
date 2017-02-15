package com.structurizr.view;

import com.structurizr.util.Colour;

/**
 * Represents a pair of colours: background and foreground.
 */
public class ColorPair {

    private String background;
    private String foreground;

    ColorPair() {
    }

    public ColorPair(String background, String foreground) {
        setBackground(background);
        setForeground(foreground);
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        if (Colour.isHexColourCode(background)) {
            this.background = background.toLowerCase();
        } else {
            throw new IllegalArgumentException("'" + background + "' is not a valid hex colour code.");
        }
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        if (Colour.isHexColourCode(foreground)) {
            this.foreground = foreground.toLowerCase();
        } else {
            throw new IllegalArgumentException("'" + foreground + "' is not a valid hex colour code.");
        }
    }

}
