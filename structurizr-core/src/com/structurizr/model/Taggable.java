package com.structurizr.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

abstract class Taggable {

    private Set<String> tags = new LinkedHashSet<>();

    protected abstract Set<String> getRequiredTags();

    /**
     * Gets the comma separated list of tags.
     *
     * @return  a comma separated list of tags,
     *          or an empty string if there are no tags
     */
    public String getTags() {
        Set<String> setOfTags = new LinkedHashSet<>(getRequiredTags());
        setOfTags.addAll(tags);

        if (setOfTags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : setOfTags) {
            buf.append(tag);
            buf.append(",");
        }

        String tagsAsString = buf.toString();
        return tagsAsString.substring(0, tagsAsString.length()-1);
    }

    void setTags(String tags) {
        if (tags == null) {
            return;
        }

        this.tags.clear();
        Collections.addAll(this.tags, tags.split(","));
    }

    public void addTags(String... tags) {
        if (tags == null) {
            return;
        }

        for (String tag : tags) {
            if (tag != null) {
                this.tags.add(tag);
            }
        }
    }

    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag);
        }
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}
