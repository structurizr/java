package com.structurizr.model;

import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents an element that can be included in a group.
 */
public abstract class GroupableElement extends Element {

    private String group;

    GroupableElement() {
    }

    /**
     * Gets the name of the group in which this element should be included in.
     *
     * @return the group name, or null if not set
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the name of the group in which this element should be included in.
     *
     * @param group the group name
     */
    public void setGroup(String group) {
        if (group == null) {
            this.group = null;
        } else {
            this.group = group.trim();

            if (StringUtils.isNullOrEmpty(this.group)) {
                this.group = null;
            }
        }
    }

}