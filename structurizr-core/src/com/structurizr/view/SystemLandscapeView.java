package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;

import javax.annotation.Nonnull;

/**
 * Represents a System Landscape view that sits "above" the C4 model,
 * showing the software systems and people in a given environment.
 */
public final class SystemLandscapeView extends StaticView {

    private Model model;

    SystemLandscapeView() {
    }

    SystemLandscapeView(Model model, String key, String description) {
        super(null, key, description);

        this.model = model;
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        Enterprise enterprise = model.getEnterprise();
        return "System Landscape" + (enterprise != null && enterprise.getName().trim().length() > 0 ? " for " + enterprise.getName() : "");
    }

    /**
     * Gets the model that this view belongs to.
     *
     * @return  a Model object
     */
    @JsonIgnore
    @Override
    public Model getModel() {
        return this.model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    /**
     * Adds all software systems and all people to this view.
     */
    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
    }

    /**
     * Adds all software systems and people that are directly connected to the specified element.
     *
     * @param element   an Element
     */
    @Override
    public void addNearestNeighbours(@Nonnull Element element) {
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, SoftwareSystem.class);
    }

}