package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.structurizr.util.ImageUtils;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedList;

final class Theme {

    private String name;
    private String description;
    @Nonnull
    private Collection<ElementStyle> elements = new LinkedList<>();
    @Nonnull
    private Collection<RelationshipStyle> relationships = new LinkedList<>();
    @Nullable
    private String logo;
    @Nullable
    private Font font;

    Theme() {
    }

    Theme(@Nonnull Collection<ElementStyle> elements, @Nonnull Collection<RelationshipStyle> relationships) {
        this.elements = elements;
        this.relationships = relationships;
    }

    Theme(String name, String description, @Nonnull Collection<ElementStyle> elements, @Nonnull Collection<RelationshipStyle> relationships) {
        this.name = name;
        this.description = description;
        this.elements = elements;
        this.relationships = relationships;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Nonnull
    @JsonGetter
    Collection<ElementStyle> getElements() {
        return elements;
    }

    void setElements(@Nonnull Collection<ElementStyle> elements) {
        this.elements = elements;
    }

    @Nonnull
    @JsonGetter
    Collection<RelationshipStyle> getRelationships() {
        return relationships;
    }

    void setRelationships(@Nonnull Collection<RelationshipStyle> relationships) {
        this.relationships = relationships;
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
