package com.structurizr.documentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single (architecture) decision, as described at http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions
 */
public final class Decision {

    // elementId is here for backwards compatibility
    private String elementId;

    private String id;
    private Date date;
    private String title;
    private String status;
    private String content;
    private Format format;

    private Set<Link> links = new HashSet<>();

    Decision() {
    }

    public Decision(String id) {
        this.id = id;
    }

    public String getElementId() {
        return elementId;
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Set<Link> getLinks() {
        return new HashSet<>(links);
    }

    void setLinks(Set<Link> links) {
        this.links = links;
    }

    public void addLink(Decision decision, String type) {
        if (!decision.getId().equals(this.getId())) {
            links.add(new Link(decision.getId(), type));
        }
    }

    public boolean hasLinkTo(Decision decision) {
        return links.stream().anyMatch(l -> l.getId().equals(decision.getId()));
    }

}