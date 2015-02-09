package com.structurizr;

import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

public class Workspace {

    private long id;
    private String name;
    private String description;
    private Model model = new Model();
    private ViewSet viewSet = new ViewSet(model);

    Workspace() {
    }

    public Workspace(String name, String description) {
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

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ViewSet getViews() {
        return viewSet;
    }

    public void setViews(ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    public void hydrate() {
        this.viewSet.setModel(model);
        this.model.hydrate();
        this.viewSet.hydrate();
    }

}
