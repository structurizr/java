package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.structurizr.PropertyHolder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * The superclass for all views.
 */
public abstract class View implements PropertyHolder {

    private String key;
    private boolean generatedKey = false;

    private int order;
    private String title;
    private String description;
    private Map<String, String> properties = new HashMap<>();

    private ViewSet viewSet;

    View() {
    }

    /**
     * Gets the description of this view.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    /**
     * Gets the identifier for this view.
     *
     * @return the identifier, as a String
     */
    public String getKey() {
        return key;
    }

    void setKey(String key) {
        if (key != null) {
            key = key.replaceAll("/", "_");
        }

        this.key = key;
    }

    /**
     * Returns true if this view has an automatically generated view key, false otherwise.
     *
     * @return  true if this view has an automatically generated view key, false otherwise
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isGeneratedKey() {
        return generatedKey;
    }

    void setGeneratedKey(boolean generatedKey) {
        this.generatedKey = generatedKey;
    }

    /**
     * Gets the order of this view.
     *
     * @return  a positive integer
     */
    public int getOrder() {
        return order;
    }

    void setOrder(int order) {
        this.order = Math.max(1, order);
    }

    /**
     * Gets the title of this view, if one has been set.
     *
     * @return  the title, as a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for this view.
     *
     * @param title     the title, as a String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return the name, as a String
     */
    @JsonIgnore
    public abstract String getName();

    void setViewSet(@Nonnull ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    /**
     * Gets the view set that this view belongs to.
     *
     * @return  a ViewSet object
     */
    @JsonIgnore
    public ViewSet getViewSet() {
        return viewSet;
    }

    /**
     * Gets the collection of name-value property pairs associated with this view, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property to this view.
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

}