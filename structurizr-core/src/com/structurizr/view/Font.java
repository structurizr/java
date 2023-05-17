package com.structurizr.view;

import com.structurizr.util.Url;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a font, including a name and an optional URL for web fonts.
 */
public final class Font {

    @Nullable
    private String name;
    @Nullable
    private String url;

    Font() {
    }

    public Font(@Nullable String name) {
        this.name = Objects.requireNonNull(name);
    }

    public Font(@Nullable String name, @Nullable String url) {
        setName(name);
        setUrl(url);
    }

    @Nullable
    public String getName() {
        return name;
    }

    /**
     * The name of the font family to use; e.g. "Times New Roman", etc.
     *
     * @param name      the name of a font family
     */
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where the web font can be found.
     *
     * @param url   a URL as a String
     */
    public void setUrl(@Nullable String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

}
