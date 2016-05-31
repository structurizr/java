package com.structurizr;

import com.structurizr.documentation.Documentation;
import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

/**
 * Represents a Structurizr workspace, which is a wrapper for a
 * software architecture model and the associated views.
 */
public class Workspace extends AbstractWorkspace {

    private Model model = new Model();
    private ViewSet viewSet = new ViewSet(model);
    private Documentation documentation = new Documentation(model);

    Workspace() {
    }

    public Workspace(String name, String description) {
        super(name, description);
    }

    /**
     * Gets the software architecture model.
     *
     * @return  a Model instance
     */
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Gets the set of views onto a software architecture model.
     *
     * @return  a ViewSet instance
     */
    public ViewSet getViews() {
        return viewSet;
    }

    public void setViews(ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    public void hydrate() {
        this.viewSet.setModel(model);
        this.documentation.setModel(model);

        this.model.hydrate();
        this.viewSet.hydrate();
        this.documentation.hydrate();
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

}
