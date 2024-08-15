package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.structurizr.PropertyHolder;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;
import com.structurizr.util.Url;

import java.util.*;

/**
 * This class represents an instance of a Relationship on a View.
 */
public final class RelationshipView implements PropertyHolder, Comparable<RelationshipView> {

    private static final int START_OF_LINE = 0;
    private static final int END_OF_LINE = 100;

    private Relationship relationship;
    private String id;
    private String description;
    private Map<String, String> properties = new HashMap<>();
    private String url;
    private String order;
    private Boolean response;
    private List<Vertex> vertices = new ArrayList<>();

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Routing routing;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Integer position;

    RelationshipView() {
    }

    RelationshipView(Relationship relationship) {
        this.relationship = relationship;
    }

    /**
     * Gets the ID of the relationship this RelationshipView represents.
     *
     * @return  the ID, as a String
     */
    public String getId() {
        if (relationship != null) {
            return relationship.getId();
        } else {
            return this.id;
        }
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the relationship that this RelationshipView represents.
     *
     * @return  a Relationship instance
     */
    @JsonIgnore
    public Relationship getRelationship() {
        return relationship;
    }

    void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    /**
     * Gets the description of this relationship (used in dynamic views only).
     *
     * @return  the description, as a String
     *          or an empty string if a description has not been set
     */
    public String getDescription() {
        return description != null ? description : "";
    }

    /**
     * Sets the description of this relationship (used in dynamic views only).
     *
     * @param description   the description, as a String
     */
    void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the collection of name-value property pairs associated with this relationship view, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Adds a name-value pair property to this relationship view.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

    /**
     * Gets the URL where more information about this relationship instance can be found.
     *
     * @return  a URL as a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this relationship instance can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(String url) {
        if (StringUtils.isNullOrEmpty(url)) {
            this.url = null;
        } else {
            if (url.startsWith(Url.INTRA_WORKSPACE_URL_PREFIX)) {
                this.url = url;
            } else if (url.matches(Url.INTER_WORKSPACE_URL_REGEX)) {
                this.url = url;
            } else if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the order of this relationship (used in dynamic views only; e.g. 1.0, 1.1, 2.0, etc).
     *
     * @return  the order, as a String
     */
    public String getOrder() {
        return order != null ? order : "";
    }

    /**
     * Sets the order of this relationship (used in dynamic views only; e.g. 1.0, 1.1, 2.0, etc).
     *
     * @param order     the order, as a String
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Gets whether this relationship view represents a response (used in dynamic views only).
     *
     * @return  true if a response, false or null otherwise
     */
    public Boolean isResponse() {
        return response;
    }

    void setResponse(Boolean response) {
        this.response = response;
    }

    /**
     * Gets the set of vertices used to render the relationship.
     *
     * @return  a collection of Vertex objects
     */
    public Collection<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * Sets the collection of vertices used when rendering this relationship.
     *
     * @param vertices  a Collection of Vertex instances
     */
    public void setVertices(Collection<Vertex> vertices) {
        if (vertices != null) {
            this.vertices = new ArrayList<>(vertices);
        }
    }

    /**
     * Gets the routing algorithm used when rendering this relationship.
     *
     * @return  a Routing instance, or null if not explicitly set
     */
    public Routing getRouting() {
        return routing;
    }

    /**
     * Sets the routing algorithm used when rendering this relationship.
     *
     * @param routing       a Routing instance, or null to not explicitly set this property
     */
    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    /**
     * Gets the position of the annotation along the line.
     *
     * @return  an integer between 0 (start of the line) to 100 (end of the line) inclusive
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the position of the annotation along the line.
     *
     * @param position  the position, as an integer between 0 (start of the line) to 100 (end of the line) inclusive
     */
    public void setPosition(Integer position) {
        if (position == null) {
            this.position = null;
        } else if (position < START_OF_LINE) {
            this.position = START_OF_LINE;
        } else if (position > END_OF_LINE) {
            this.position = END_OF_LINE;
        } else {
            this.position = position;
        }
    }

    void copyLayoutInformationFrom(RelationshipView source) {
        if (source != null) {
            setVertices(source.getVertices());
            setPosition(source.getPosition());
            setRouting(source.getRouting());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipView that = (RelationshipView) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (!getId().equals(that.getId())) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (relationship != null) {
            return (order != null ? order + ": " : "") + (description != null ? description + " " : "") + relationship.toString();
        }
        return "";
    }

    @Override
    public int compareTo(RelationshipView relationshipView) {
        String identifier1 = getId() + "/" + getOrder();
        String identifier2 = relationshipView.getId() + "/" + relationshipView.getOrder();

        return identifier1.compareTo(identifier2);
    }

}