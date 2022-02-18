package com.structurizr.view;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import java.util.*;

public final class Styles {

    private static final Integer DEFAULT_WIDTH_OF_ELEMENT = 450;
    private static final Integer DEFAULT_HEIGHT_OF_ELEMENT = 300;

    private static final Integer DEFAULT_WIDTH_OF_PERSON = 400;
    private static final Integer DEFAULT_HEIGHT_OF_PERSON = 400;

    private Collection<ElementStyle> elements = new LinkedList<>();
    private Collection<RelationshipStyle> relationships = new LinkedList<>();

    private Map<String,Theme> themes = new LinkedHashMap<>();

    public Collection<ElementStyle> getElements() {
        return elements;
    }

    public void add(ElementStyle elementStyle) {
        if (elementStyle != null) {
            if (StringUtils.isNullOrEmpty(elementStyle.getTag())) {
                throw new IllegalArgumentException("A tag must be specified.");
            }

            if (elements.stream().anyMatch(es -> es.getTag().equals(elementStyle.getTag()))) {
                throw new IllegalArgumentException("An element style for the tag \"" + elementStyle.getTag() + "\" already exists.");
            }

            this.elements.add(elementStyle);
        }
    }

    public ElementStyle addElementStyle(String tag) {
        ElementStyle elementStyle = new ElementStyle(tag);
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

            if (relationships.stream().anyMatch(es -> es.getTag().equals(relationshipStyle.getTag()))) {
                throw new IllegalArgumentException("A relationship style for the tag \"" + relationshipStyle.getTag() + "\" already exists.");
            }

            this.relationships.add(relationshipStyle);
        }
    }

    public RelationshipStyle addRelationshipStyle(String tag) {
        RelationshipStyle relationshipStyle = new RelationshipStyle(tag);
        add(relationshipStyle);

        return relationshipStyle;
    }

    /**
     * Finds the element style for the given tag. This method creates an empty style,
     * and copies properties from any element styles (from the workspace and any themes) for the given tag.
     *
     *
     * @param tag       the tag (a String)
     * @return          an ElementStyle instance
     */
    public ElementStyle findElementStyle(String tag) {
        if (tag == null) {
            return null;
        }

        tag = tag.trim();
        ElementStyle style = new ElementStyle(tag);

        Collection<ElementStyle> elementStyles = new ArrayList<>();
        for (Theme theme : themes.values()) {
            elementStyles.addAll(theme.getElements());
        }
        elementStyles.addAll(elements);

        for (ElementStyle elementStyle : elementStyles) {
            if (elementStyle != null && elementStyle.getTag().equals(tag)) {
                style.copyFrom(elementStyle);
            }
        }

        return style;
    }

    /**
     * Finds the relationship style for the given tag. This method creates an empty style,
     * and copies properties from any relationship styles (from the workspace and any themes) for the given tag.
     *
     *
     * @param tag       the tag (a String)
     * @return          a RelationshipStyle instance
     */
    public RelationshipStyle findRelationshipStyle(String tag) {
        if (tag == null) {
            return null;
        }

        tag = tag.trim();
        RelationshipStyle style = new RelationshipStyle(tag);

        Collection<RelationshipStyle> relationshipStyles= new ArrayList<>();
        for (Theme theme : themes.values()) {
            relationshipStyles.addAll(theme.getRelationships());
        }
        relationshipStyles.addAll(relationships);

        for (RelationshipStyle relationshipStyle : relationshipStyles) {
            if (relationshipStyle != null && relationshipStyle.getTag().equals(tag)) {
                style.copyFrom(relationshipStyle);
            }
        }

        return style;
    }

    public ElementStyle findElementStyle(Element element) {
        ElementStyle style = new ElementStyle("").background("#dddddd").color("#000000").shape(Shape.Box).fontSize(24).border(Border.Solid).opacity(100).metadata(true).description(true);

        if (element instanceof DeploymentNode) {
            style.setBackground("#ffffff");
            style.setColor("#000000");
            style.setStroke("#888888");
        }

        if (element != null) {
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
                    ElementStyle elementStyle = findElementStyle(tag);
                    if (elementStyle != null) {
                        style.copyFrom(elementStyle);
                    }
                }
            }
        }

        if (style.getWidth() == null) {
            if (style.getShape() == Shape.Person) {
                style.setWidth(DEFAULT_WIDTH_OF_PERSON);
            } else {
                style.setWidth(DEFAULT_WIDTH_OF_ELEMENT);
            }
        }

        if (style.getHeight() == null) {
            if (style.getShape() == Shape.Person || style.getShape() == Shape.Robot) {
                style.setHeight(DEFAULT_HEIGHT_OF_PERSON);
            } else {
                style.setHeight(DEFAULT_HEIGHT_OF_ELEMENT);
            }
        }

        if (style.getStroke() == null) {
            java.awt.Color color = java.awt.Color.decode(style.getBackground());
            style.setStroke(String.format("#%06X", (0xFFFFFF & color.darker().getRGB())));
        }

        return style;
    }

    public RelationshipStyle findRelationshipStyle(Relationship relationship) {
        RelationshipStyle style = new RelationshipStyle("").thickness(2).color("#707070").dashed(true).routing(Routing.Direct).fontSize(24).width(200).position(50).opacity(100);

        if (relationship != null) {
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
                    RelationshipStyle relationshipStyle = findRelationshipStyle(tag);
                    if (relationshipStyle != null) {
                        style.copyFrom(relationshipStyle);
                    }
                }
            }
        }

        return style;
    }

    void addStylesFromTheme(String url, Collection<ElementStyle> elements, Collection<RelationshipStyle> relationships) {
        themes.put(url, new Theme(elements, relationships));
    }

}
