package com.structurizr.documentation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single (architecture) decision, as described at http://thinkrelevance.com/blog/2011/11/15/documenting-architecture-decisions
 */
public final class Decision extends DocumentationContent {

    private String id;
    private Date date;
    private String status;

    private Set<Link> links = new HashSet<>();

    Decision() {
    }

    public Decision(String id) {
        this.id = id;
    }

    /**
     * Gets the ID of this decision.
     *
     * @return      the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the date of this decision.
     *
     * @return      a Date object
     */
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the status of this decision.
     *
     * @return      the status, as a String
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the set of links from this decision.
     *
     * @return  a Set of Link objects
     */
    public Set<Link> getLinks() {
        return new HashSet<>(links);
    }

    void setLinks(Set<Link> links) {
        this.links = links;
    }

    /**
     * Adds a link between this decision and another.
     *
     * @param decision      the Decision to link to
     * @param type          the "type" of the link (e.g. "superseded by")
     */
    public void addLink(Decision decision, String type) {
        if (!decision.getId().equals(this.getId())) {
            links.add(new Link(decision.getId(), type));
        }
    }

    /**
     * Determines whether a decision already has a link to another decision
     *
     * @param decision      the Decision to check against
     * @return              true if a link exists, false otherwise
     */
    public boolean hasLinkTo(Decision decision) {
        return links.stream().anyMatch(l -> l.getId().equals(decision.getId()));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Decision decision = (Decision)object;
        if (getElementId() != null) {
            return getElementId().equals(decision.getElementId()) && getId().equals(decision.getId());
        } else {
            return getId().equals(decision.getId());
        }
    }

    @Override
    public int hashCode() {
        int result = getElementId() != null ? getElementId().hashCode() : 0;
        result = 31 * result + getId().hashCode();
        return result;
    }

}