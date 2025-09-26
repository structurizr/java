package com.structurizr.view;

import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.util.TagUtils;

import java.util.*;

public final class Styles {

    public static final String DEFAULT_BACKGROUND_LIGHT = "#ffffff";
    public static final String DEFAULT_COLOR_LIGHT = "#444444";

    public static final String DEFAULT_BACKGROUND_DARK = "#111111";
    public static final String DEFAULT_COLOR_DARK = "#cccccc";

    private Collection<ElementStyle> elements = new TreeSet<>();
    private Collection<RelationshipStyle> relationships = new TreeSet<>();

    private final List<Theme> themes = new ArrayList<>();

    public Collection<ElementStyle> getElements() {
        return elements;
    }

    public void add(ElementStyle elementStyle) {
        if (elementStyle != null) {
            if (StringUtils.isNullOrEmpty(elementStyle.getTag())) {
                throw new IllegalArgumentException("A tag must be specified.");
            }

            if (elements.stream().anyMatch(es -> es.getTag().equals(elementStyle.getTag()) && es.getColorScheme() == elementStyle.getColorScheme())) {
                if (elementStyle.getColorScheme() == null) {
                    throw new IllegalArgumentException("An element style for the tag \"" + elementStyle.getTag() + "\" already exists.");
                } else {
                    throw new IllegalArgumentException("An element style for the tag \"" + elementStyle.getTag() + "\" and color scheme " + elementStyle.getColorScheme() + " already exists.");
                }
            }

            this.elements.add(elementStyle);
        }
    }

    public ElementStyle addElementStyle(String tag) {
        return addElementStyle(tag, null);
    }

    public ElementStyle addElementStyle(String tag, ColorScheme colorScheme) {
        ElementStyle elementStyle = new ElementStyle(tag, colorScheme);
        add(elementStyle);

        return elementStyle;
    }

    /**
     * Removes all element styles.
     */
    public void clearElementStyles() {
        this.elements = new LinkedList<>();
    }

    /**
     * Removes all relationship styles.
     */
    public void clearRelationshipStyles() {
        this.relationships = new LinkedList<>();
    }

    public Collection<RelationshipStyle> getRelationships() {
        return relationships;
    }

    public void add(RelationshipStyle relationshipStyle) {
        if (relationshipStyle != null) {
            if (StringUtils.isNullOrEmpty(relationshipStyle.getTag())) {
                throw new IllegalArgumentException("A tag must be specified.");
            }

            if (relationships.stream().anyMatch(rs -> rs.getTag().equals(relationshipStyle.getTag()) && rs.getColorScheme() == relationshipStyle.getColorScheme())) {
                if (relationshipStyle.getColorScheme() == null) {
                    throw new IllegalArgumentException("A relationship style for the tag \"" + relationshipStyle.getTag() + "\" already exists.");
                } else {
                    throw new IllegalArgumentException("A relationship style for the tag \"" + relationshipStyle.getTag() + "\" and color scheme " + relationshipStyle.getColorScheme() + " already exists.");
                }
            }

            this.relationships.add(relationshipStyle);
        }
    }

    public RelationshipStyle addRelationshipStyle(String tag) {
        return addRelationshipStyle(tag, null);
    }

    public RelationshipStyle addRelationshipStyle(String tag, ColorScheme colorScheme) {
        RelationshipStyle relationshipStyle = new RelationshipStyle(tag, colorScheme);
        add(relationshipStyle);

        return relationshipStyle;
    }

    /**
     * Gets the element style that has been defined (in this workspace) for the given tag (without a color scheme).
     *
     * @param tag   the tag (a String)
     * @return      an ElementStyle instance, or null if no element style has been defined in this workspace
     */
    public ElementStyle getElementStyle(String tag) {
        return getElementStyle(tag, null);
    }

    /**
     * Gets the element style that has been defined (in this workspace) for the given tag and color scheme.
     *
     * @param tag           the tag (a String)
     * @param colorScheme  the ColorScheme (can be null)
     * @return      an ElementStyle instance, or null if no element style has been defined in this workspace
     */
    public ElementStyle getElementStyle(String tag, ColorScheme colorScheme) {
        if (StringUtils.isNullOrEmpty(tag)) {
            throw new IllegalArgumentException("A tag must be specified.");
        }

        return elements.stream().filter(es -> es.getTag().equals(tag) && es.getColorScheme() == colorScheme).findFirst().orElse(null);
    }

    /**
     * Finds the element style for the given tag and light color scheme.
     *
     * @param tag       the tag (a String)
     * @return          an ElementStyle instance, or null if there is no style for the given tag
     */
    public ElementStyle findElementStyle(String tag) {
        return findElementStyle(tag, ColorScheme.Light);
    }

    /**
     * Finds the element style for the given tag and color scheme. This method creates an empty style,
     * and copies properties from any element styles (from the workspace and any themes) for the given tag.
     *
     * @param tag           the tag (a String)
     * @param colorScheme   the target color scheme
     * @return          an ElementStyle instance, or null if there is no style for the given tag
     */
    public ElementStyle findElementStyle(String tag, ColorScheme colorScheme) {
        if (tag == null) {
            return null;
        }

        boolean elementStyleExists = false;
        tag = tag.trim();
        ElementStyle style = new ElementStyle(tag);

        Collection<ElementStyle> elementStyles = new ArrayList<>();
        for (Theme theme : themes) {
            elementStyles.addAll(theme.getElements());
        }
        elementStyles.addAll(elements);

        for (ElementStyle elementStyle : elementStyles) {
            if (elementStyle != null && elementStyle.getTag().equals(tag)) {
                if (elementStyle.getColorScheme() == null || elementStyle.getColorScheme() == colorScheme) {
                    elementStyleExists = true;
                    style.copyFrom(elementStyle);
                }
            }
        }

        if (elementStyleExists) {
            return style;
        } else {
            return null;
        }
    }

    /**
     * Gets the relationship style that has been defined (in this workspace) for the given tag (without a color scheme).
     *
     * @param tag   the tag (a String)
     * @return      an RelationshipStyle instance, or null if no relationship style has been defined in this workspace
     */
    public RelationshipStyle getRelationshipStyle(String tag) {
        return getRelationshipStyle(tag, null);
    }

    /**
     * Gets the relationship style that has been defined (in this workspace) for the given tag and color scheme.
     *
     * @param tag   the tag (a String)
     * @param colorScheme  the ColorScheme (can be null)
     * @return      an RelationshipStyle instance, or null if no relationship style has been defined in this workspace
     */
    public RelationshipStyle getRelationshipStyle(String tag, ColorScheme colorScheme) {
        if (StringUtils.isNullOrEmpty(tag)) {
            throw new IllegalArgumentException("A tag must be specified.");
        }

        return relationships.stream().filter(rs -> rs.getTag().equals(tag) && rs.getColorScheme() == colorScheme).findFirst().orElse(null);
    }

    /**
     * Finds the relationship style for the given tag with a light color scheme.
     *
     *
     * @param tag       the tag (a String)
     * @return          a RelationshipStyle instance, or null if there is no style for the given tag
     */
    public RelationshipStyle findRelationshipStyle(String tag) {
        return findRelationshipStyle(tag, ColorScheme.Light);
    }

    /**
     * Finds the relationship style for the given tag and color scheme. This method creates an empty style,
     * and copies properties from any relationship styles (from the workspace and any themes) for the given tag.
     *
     * @param tag           the tag (a String)
     * @param colorScheme   the target color scheme
     * @return          a RelationshipStyle instance, or null if there is no style for the given tag
     */
    public RelationshipStyle findRelationshipStyle(String tag, ColorScheme colorScheme) {
        if (tag == null) {
            return null;
        }

        boolean relationshipStyleExists = false;
        tag = tag.trim();
        RelationshipStyle style = new RelationshipStyle(tag);

        Collection<RelationshipStyle> relationshipStyles= new ArrayList<>();
        for (Theme theme : themes) {
            relationshipStyles.addAll(theme.getRelationships());
        }
        relationshipStyles.addAll(relationships);

        for (RelationshipStyle relationshipStyle : relationshipStyles) {
            if (relationshipStyle != null && relationshipStyle.getTag().equals(tag)) {
                if (relationshipStyle.getColorScheme() == null || relationshipStyle.getColorScheme() == colorScheme) {
                    style.copyFrom(relationshipStyle);
                    relationshipStyleExists = true;
                }
            }
        }

        if (relationshipStyleExists) {
            return style;
        } else {
            return null;
        }
    }

    /**
     * Finds the element style used to render the specified element with a light color scheme.
     *
     * @param element       an Element object
     * @return  an ElementStyle object
     */
    public ElementStyle findElementStyle(Element element) {
        return findElementStyle(element, ColorScheme.Light);
    }

    /**
     * Finds the element style used to render the specified element and color scheme, according to the following rules:
     *
     * 1. Start with a default style.
     * 2. Calculate set of tags associated with the element.
     * 3. Find the style properties for each tag (themes first, followed by workspace styles)
     *
     * @param element       an Element object
     * @param colorScheme   a ColorScheme (Light or Dark)
     * @return  an ElementStyle object
     */
    public ElementStyle findElementStyle(Element element, ColorScheme colorScheme) {
        ElementStyle style = new ElementStyle(Tags.ELEMENT).shape(Shape.Box).width(ElementStyle.DEFAULT_WIDTH).height(ElementStyle.DEFAULT_HEIGHT).fontSize(24).border(Border.Solid).opacity(100).metadata(true).description(true);

        if (element != null) {
            Set<String> tagsUsedToComposeStyle = new LinkedHashSet<>();
            tagsUsedToComposeStyle.add(Tags.ELEMENT);
            String tags = element.getTags();

            if (element instanceof SoftwareSystemInstance) {
                SoftwareSystem ss = ((SoftwareSystemInstance)element).getSoftwareSystem();
                tags = ss.getTags() + "," + tags;
            } else if (element instanceof ContainerInstance) {
                Container c = ((ContainerInstance)element).getContainer();
                tags = c.getTags() + "," + tags;
            }

            for (String tag : tags.split(",")) {
                if (!StringUtils.isNullOrEmpty(tag)) {
                    ElementStyle elementStyle = findElementStyle(tag, colorScheme);
                    if (elementStyle != null) {
                        style.copyFrom(elementStyle);
                        tagsUsedToComposeStyle.add(elementStyle.getTag());
                    }
                }
            }

            style.setTag(TagUtils.toString(tagsUsedToComposeStyle));
        }

        if (style.getBackground() == null) {
            if (colorScheme == ColorScheme.Dark) {
                style.background(DEFAULT_BACKGROUND_DARK);
                if (style.getStroke() == null) {
                    style.stroke(DEFAULT_COLOR_DARK);
                }
            } else {
                style.background(DEFAULT_BACKGROUND_LIGHT);
                if (style.getStroke() == null) {
                    style.stroke(DEFAULT_COLOR_LIGHT);
                }
            }
        } else {
            if (style.getStroke() == null) {
                java.awt.Color color = java.awt.Color.decode(style.getBackground());
                style.setStroke(String.format("#%06X", (0xFFFFFF & color.darker().getRGB())));
            }
        }

        if (style.getColor() == null) {
            if (colorScheme == ColorScheme.Dark) {
                style.color(DEFAULT_COLOR_DARK);
            } else {
                style.color(DEFAULT_COLOR_LIGHT);
            }
        }


        return style;
    }

    /**
     * Finds the relationship style used to render the specified relationship with a light color scheme.
     */
    public RelationshipStyle findRelationshipStyle(Relationship relationship) {
        return findRelationshipStyle(relationship, ColorScheme.Light);
    }

    /**
     * Finds the relationship style used to render the specified relationship and color scheme, according to the following rules:
     *
     * 1. Start with a default style.
     * 2. Calculate set of tags associated with the relationship, and any linked relationship(s).
     * 3. Find the style properties for each tag (themes first, followed by workspace styles)
     *
     * @param relationship      a Relationship object
     * @param colorScheme       a ColorScheme (Light or Dark)
     * @return      a RelationshipStyle object
     */
    public RelationshipStyle findRelationshipStyle(Relationship relationship, ColorScheme colorScheme) {
        RelationshipStyle style = new RelationshipStyle(Tags.RELATIONSHIP).thickness(2).style(LineStyle.Dashed).dashed(true).routing(Routing.Direct).fontSize(24).width(200).position(50).opacity(100);

        if (relationship != null) {
            Set<String> tagsUsedToComposeStyle = new LinkedHashSet<>();
            tagsUsedToComposeStyle.add(Tags.RELATIONSHIP);
            String tags = relationship.getTags();
            String linkedRelationshipId = relationship.getLinkedRelationshipId();

            while (!StringUtils.isNullOrEmpty(linkedRelationshipId)) {
                // the "linked relationship ID" is used for:
                // - container instance -> container instance relationships
                // - implied relationships
                Relationship linkedRelationship = relationship.getModel().getRelationship(linkedRelationshipId);
                tags = linkedRelationship.getTags() + "," + tags;
                linkedRelationshipId = linkedRelationship.getLinkedRelationshipId();
            }

            for (String tag : tags.split(",")) {
                if (!StringUtils.isNullOrEmpty(tag)) {
                    RelationshipStyle relationshipStyle = findRelationshipStyle(tag, colorScheme);
                    if (relationshipStyle != null) {
                        style.copyFrom(relationshipStyle);
                        tagsUsedToComposeStyle.add(relationshipStyle.getTag());
                    }
                }
            }

            style.setTag(TagUtils.toString(tagsUsedToComposeStyle));
        }

        if (style.getColor() == null) {
            if (colorScheme == ColorScheme.Dark) {
                style.color(DEFAULT_COLOR_DARK);
            } else {
                style.color(DEFAULT_COLOR_LIGHT);
            }
        }

        return style;
    }

    /**
     * Adds the element/relationship styles from the given theme.
     *
     * @param theme     a Theme object
     */
    public void addStylesFromTheme(Theme theme) {
        if (theme != null) {
            themes.add(theme);
        }
    }

    /**
     * Inlines the element and relationship styles from the specified theme, adding the styles into the workspace
     * and overriding any properties already set.
     *
     * @param theme         a Theme object
     */
    public void inlineTheme(Theme theme) {
        for (ElementStyle elementStyle : theme.getElements()) {
            ElementStyle es = getElementStyle(elementStyle.getTag());
            if (es == null) {
                es = addElementStyle(elementStyle.getTag());
            }

            es.copyFrom(elementStyle);
        }

        for (RelationshipStyle relationshipStyle : theme.getRelationships()) {
            RelationshipStyle rs = getRelationshipStyle(relationshipStyle.getTag());
            if (rs == null) {
                rs = addRelationshipStyle(relationshipStyle.getTag());
            }

            rs.copyFrom(relationshipStyle);
        }
    }

}