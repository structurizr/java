package com.structurizr.dsl;

import com.structurizr.PerspectivesHolder;
import com.structurizr.PropertyHolder;
import com.structurizr.model.Perspective;
import com.structurizr.util.StringUtils;

import java.util.*;

final class Archetype implements PropertyHolder, PerspectivesHolder {

    private final String name;
    private final String type;
    private String metadata = "";
    private String description = "";
    private String technology = "";
    private final Set<String> tags = new LinkedHashSet<>();

    private final Map<String, String> properties = new HashMap<>();
    private final Set<Perspective> perspectives = new TreeSet<>();

    Archetype(String name, String type) {
        if (StringUtils.isNullOrEmpty(name)) {
            name = type;
        }

        this.name = name.toLowerCase();
        this.type = type;
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    String getMetadata() {
        return metadata;
    }

    void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getTechnology() {
        return technology;
    }

    void setTechnology(String technology) {
        this.technology = technology;
    }

    void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag.trim());
            }
        }
    }

    Set<String> getTags() {
        return new LinkedHashSet<>(tags);
    }

    /**
     * Gets the collection of name-value property pairs associated with this archetype, as a Map.
     *
     * @return  a Map (String, String) (empty if there are no properties)
     */
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    /**
     * Adds a name-value pair property to this archetype.
     *
     * @param name      the name of the property
     * @param value     the value of the property
     */
    public void addProperty(String name, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A property name must be specified.");
        }

        if (StringUtils.isNullOrEmpty(value)) {
            throw new IllegalArgumentException("A property value must be specified.");
        }

        properties.put(name, value);
    }

    /**
     * Gets the set of perspectives associated with this archetype.
     *
     * @return  a Set of Perspective objects (empty if there are none)
     */
    public Set<Perspective> getPerspectives() {
        return new TreeSet<>(perspectives);
    }

    /**
     * Adds a perspective to this archetype.
     *
     * @param name          the name of the perspective (e.g. "Security", must be unique)
     * @param description   the description of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    public Perspective addPerspective(String name, String description) {
        return addPerspective(name, description, "");
    }

    /**
     * Adds a perspective to this archetype.
     *
     * @param name          the name of the perspective (e.g. "Technical Debt", must be unique)
     * @param description   the description of the perspective (e.g. "High")
     * @param value         the value of the perspective
     * @return              a Perspective object
     * @throws IllegalArgumentException     if perspective details are not specified, or the named perspective exists already
     */
    public Perspective addPerspective(String name, String description, String value) {
        if (StringUtils.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("A name must be specified.");
        }

        if (StringUtils.isNullOrEmpty(description)) {
            throw new IllegalArgumentException("A description must be specified.");
        }

        if (perspectives.stream().anyMatch(p -> p.getName().equals(name))) {
            throw new IllegalArgumentException("A perspective named \"" + name + "\" already exists.");
        }

        Perspective perspective = new Perspective(name, description, value);
        perspectives.add(perspective);

        return perspective;
    }

}