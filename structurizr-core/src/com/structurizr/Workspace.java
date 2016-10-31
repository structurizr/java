package com.structurizr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.documentation.Documentation;
import com.structurizr.model.Model;
import com.structurizr.view.ViewSet;

/**
 * Represents a Structurizr workspace, which is a wrapper for a
 * software architecture model, views and documentation.
 */
public final class Workspace extends AbstractWorkspace {

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

    void setViews(ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    public void hydrate() {
        this.viewSet.setModel(model);
        this.documentation.setModel(model);

        this.model.hydrate();
        this.viewSet.hydrate();
        this.documentation.hydrate();
    }

    /**
     * Gets the documentation associated with this workspace.
     *
     * @return  a Documentation object
     */
    public Documentation getDocumentation() {
        return documentation;
    }

    void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return model.isEmpty() && viewSet.isEmpty() && documentation.isEmpty();
    }

}
