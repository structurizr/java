package com.structurizr.dsl;

import com.structurizr.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

final class Archetype {

    private final String name;
    private final String type;
    private String description = "";
    private String technology = "";
    private final Set<String> tags = new LinkedHashSet<>();

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

}