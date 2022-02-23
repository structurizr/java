package com.structurizr.documentation;

import com.structurizr.util.StringUtils;

import java.util.Objects;

public final class Link {

    private String type;
    private String id;

    public Link() {
    }

    Link(String id, String type) {
        if (StringUtils.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Link ID must be specfied");
        }
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return Objects.equals(type, link.type) && id.equals(link.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

}