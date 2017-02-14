package com.structurizr.view;

import com.structurizr.util.StringUtils;

/**
 * Represents a font, including a name and an optional URL for web fonts.
 */
public class Font {

    private String name;
    private String url;

    Font() {
    }

    public Font(String name) {
        this.name = name;
    }

    public Font(String name, String url) {
        setName(name);
        setUrl(url);
    }

    public String getName() {
        return name;
    }

    /**
     * The name of the font family to use; e.g. "Times New Roman", etc.
     *
     * @param name      the name of a font family
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where the web font can be found.
     *
     * @param url   a URL as a String
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (StringUtils.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

}
