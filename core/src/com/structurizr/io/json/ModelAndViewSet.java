package com.structurizr.io.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Model;
import com.structurizr.view.View;
import com.structurizr.view.ViewSet;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

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
