package com.structurizr.io.json;

import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

class ModelAndViewSet {

    private Model model;
    private ViewSet viewSet;

    ModelAndViewSet() {
    }

    ModelAndViewSet(Model model, ViewSet viewSet) {
        this.model = model;
        this.viewSet = viewSet;
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

}
