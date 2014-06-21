package com.structurizr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Relationship {

    private Element source;
    private long sourceId;
    private Element destination;
    private long destinationId;
    private String description;

    public Relationship() {
    }

    public Relationship(Element source, Element destination, String description) {
        this.source = source;
        this.destination = destination;
        this.description = description;
    }

    @JsonIgnore
    public Element getSource() {
        return source;
    }

    public long getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public void setSource(Element source) {
        this.source = source;
    }

    @JsonIgnore
    public Element getDestination() {
        return destination;
    }

    public long getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public void setDestination(Element destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return source.toString() + " -> " + destination.toString();
    }
}
