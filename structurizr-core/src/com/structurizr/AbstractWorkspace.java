package com.structurizr;

import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

public abstract class AbstractWorkspace {

    private long id;
    private String name;
    private String description;
    private String thumbnail;

    public AbstractWorkspace() {
    }

    public AbstractWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
