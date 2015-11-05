package com.structurizr;

import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

public class Workspace extends AbstractWorkspace {

    private Model model = new Model();
    private ViewSet viewSet = new ViewSet(model);

    Workspace() {
    }

    public Workspace(String name, String description) {
        super(name, description);
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
