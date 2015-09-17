package com.structurizr.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Sorry about the name, send a pull request if you think of something better! :-)
 */
abstract class TaggableThing {

    private Set<String> tags = new LinkedHashSet<>();

    public String getTags() {
        if (this.tags.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for (String tag : tags) {
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

}
