package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Relationship {

    private Element source;
    private int sourceId;
    private Element destination;
    private int destinationId;
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

    public int getSourceId() {
        if (this.source != null) {
            return this.source.getId();
        } else {
            return this.sourceId;
        }
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setSource(Element source) {
        this.source = source;
    }

    @JsonIgnore
    public Element getDestination() {
        return destination;
    }

    public int getDestinationId() {
        if (this.destination != null) {
            return this.destination.getId();
        } else {
            return this.destinationId;
        }
    }

    public void setDestinationId(int destinationId) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relationship that = (Relationship)o;

        if (getDestinationId() != that.getDestinationId()) return false;
        if (getSourceId() != that.getSourceId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (sourceId ^ (sourceId >>> 32));
        result = 31 * result + (int) (destinationId ^ (destinationId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return source.toString() + " ---[" + description + "]---> " + destination.toString();
    }

}