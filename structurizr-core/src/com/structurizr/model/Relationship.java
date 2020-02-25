package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.util.Url;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A relationship between two elements.
 */
public final class Relationship extends ModelItem {

    private Element source;
    private String sourceId;
    private Element destination;
    private String destinationId;
    private String description;
    private String technology;
    private InteractionStyle interactionStyle = InteractionStyle.Synchronous;
    private String url;

    private String linkedRelationshipId;

    Relationship() {
    }

    Relationship(Element source, Element destination, String description, String technology, InteractionStyle interactionStyle) {
        this();

        setSource(source);
        setDestination(destination);
        setDescription(description);
        setTechnology(technology);
        setInteractionStyle(interactionStyle);

        if (interactionStyle == InteractionStyle.Synchronous) {
            addTags(Tags.SYNCHRONOUS);
        } else {
            addTags(Tags.ASYNCHRONOUS);
        }
    }

    @JsonIgnore
    public Element getSource() {
        return source;
    }

    /**
     * Gets the ID of the source element.
     *
     * @return  the ID of the source element, as a String
     */
    public String getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    void setSource(Element source) {
        this.source = source;
    }

    @JsonIgnore
    public Element getDestination() {
        return destination;
    }

    /**
     * Gets the ID of the destination element.
     *
     * @return  the ID of the destination element, as a String
     */
    public String getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    void setDestination(Element destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the technology associated with this relationship (e.g. HTTPS, JDBC, etc).
     *
     * @return  the technology as a String,
     *          or null if a technology is not specified
     */
    public String getTechnology() {
        return technology;
    }

    void setTechnology(String technology) {
        this.technology = technology;
    }

    /**
     * Gets the interaction style (synchronous or asynchronous).
     *
     * @return  an InteractionStyle,
     *          or null if an interaction style has not been specified
     */
    public InteractionStyle getInteractionStyle() {
        return interactionStyle;
    }

    void setInteractionStyle(InteractionStyle interactionStyle) {
        this.interactionStyle = interactionStyle;
    }

    public String getLinkedRelationshipId() {
        return linkedRelationshipId;
    }

    void setLinkedRelationshipId(String baseRelationshipId) {
        this.linkedRelationshipId = baseRelationshipId;
    }

    @Override
    protected Set<String> getRequiredTags() {
        if (linkedRelationshipId == null) {
            return new LinkedHashSet<>(Collections.singletonList(Tags.RELATIONSHIP));
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * Gets the URL where more information about this relationship can be found.
     *
     * @return  a URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this relationship can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (url != null && url.trim().length() > 0) {
            if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    @Override
    public String toString() {
        return source.toString() + " ---[" + description + "]---> " + destination.toString();
    }

}