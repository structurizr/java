package com.structurizr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.PropertyHolder;
import com.structurizr.util.StringUtils;
import com.structurizr.util.TagUtils;
import com.structurizr.util.Url;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * The base class for elements and relationships.
 */
public abstract class ModelItem implements PropertyHolder {

    private String id = "";
    @Nonnull
    private final Set<String> tags = new LinkedHashSet<>();

    @Nullable
    private String url;
    @Nonnull
    private Map<String, String> properties = new HashMap<>();
    @Nonnull
    private Set<Perspective> perspectives = new HashSet<>();

    @JsonIgnore
    public abstract String getCanonicalName();

    @Nonnull
    @JsonIgnore
    public abstract Set<String> getDefaultTags();

    /**
     * Gets the ID of this item in the model.
     *
     * @return the ID, as a String
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the comma separated list of tags.
     *
     * @return  a comma separated list of tags,
     *          or an empty string if there are no tags
     */
    @Nonnull
    public String getTags() {
        return TagUtils.toString(getTagsAsSet());
    }

    @Nonnull
    @JsonIgnore
    public Set<String> getTagsAsSet() {
        Set<String> setOfTags = new LinkedHashSet<>(getDefaultTags());
        setOfTags.addAll(tags);

        return setOfTags;
    }

    void setTags(@Nullable String tags) {
        this.tags.clear();

        if (tags == null) {
            return;
        }

        Collections.addAll(this.tags, tags.split(","));
    }

    public void addTags(@Nullable String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag.trim());
            }
        }
    }

    /**
     * Removes the given tag.
     *
     * @param tag       the tag to remove
     * @return          true if the tag was removed; will return false if a non-existent tag is passed, or if an attempt is
     *                  made to remove required tags, which cannot be removed.
     */
    public boolean removeTag(@Nullable String tag) {
        if (tag != null) {
            return this.tags.remove(tag.trim());
        }
        return false;
    }

    /**
     * Determines whether this model item has the given tag.
     *
     * @param tag   the tag to check for
     * @return      true if tag is present as a tag on this item, or if it is one of the
     *              required tags defined by the model in getRequiredTags(), false otherwise
     */
    public boolean hasTag(@Nonnull String tag) {
        return getTagsAsSet().contains(tag.trim());
    }

    /**
     * Gets the URL where more information about this item can be found.
     *
     * @return  a URL as a String
     */
    @Nullable
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where more information about this item can be found.
     *
     * @param url   the URL as a String
     * @throws IllegalArgumentException     if the URL is not a well-formed URL
     */
    public void setUrl(@Nullable String url) {
        if (StringUtils.isNullOrEmpty(url)) {
            this.url = null;
        } else {
            if (url.startsWith(Url.WORKSPACE_URL_PREFIX)) {
                this.url = url;
            } else if (Url.isUrl(url)) {
                this.url = url;
            } else {
                throw new IllegalArgumentException(url + " is not a valid URL.");
            }
        }
    }

    /**
     * Gets the collection of name-value property pairs associated with this model item, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    @Nonnull
    public Map<String, String> getProperties() {
        return new HashMap<>(properties);
    }

    /**
     * Adds a name-value pair property to this model item.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    void setProperties(@Nullable Map<String, String> properties) {
        if (properties != null) {
            this.properties = new HashMap<>(properties);
        }
    }

    /**
     * Gets the set of perspectives associated with this model item.
     *
     * @return  a Set of Perspective objects (empty if there are none)
     */
    @Nonnull
    public Set<Perspective> getPerspectives() {
        return new HashSet<>(perspectives);
    }

    void setPerspectives(@Nullable Set<Perspective> perspectives) {
        this.perspectives.clear();

        if (perspectives == null) {
            return;
        }

        this.perspectives.addAll(perspectives);
    }

    /**
     * Adds a perspective to this model item.
     *
     * @param name          the name of the perspective (e.g. "Security", must be unique)
     * @param description   a description of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    public Perspective addPerspective(String name, String description) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if (StringUtils.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("A description must be specified.");
        }

        if (perspectives.stream().anyMatch(p -> Objects.equals(p.getName(), name))) {
            throw new IllegalArgumentException("A perspective named \"" + name + "\" already exists.");
        }

        Perspective perspective = new Perspective(name, description);
        perspectives.add(perspective);

        return perspective;
    }

}
