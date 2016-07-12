package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;

/**
 * Represents an Enterprise Context view that sits above the C4 model. This is the "big picture" view,
 * showing the software systems and people in an given environment.
 * The permitted elements in this view are software systems and people.
 */
public class EnterpriseContextView extends StaticView {

    private Model model;

    EnterpriseContextView() {
    }

    /**
     * Creates an enterprise context view.
     *
     * @param key                   the key for the view
     * @param description           the description for the view
     */
    EnterpriseContextView(Model model, String key, String description) {
        super(null, key, description);

        this.model = model;
    }

    @Override
    public String getName() {
        Enterprise enterprise = model.getEnterprise();
        return "Enterprise Context" + (enterprise != null && enterprise.getName().trim().length() > 0 ? " for " + enterprise.getName() : "");
    }

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

    @Override
    public void addNearestNeighbours(Element element) {
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Person.class);
    }

}