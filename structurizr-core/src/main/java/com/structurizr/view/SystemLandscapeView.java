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

    private boolean enterpriseBoundaryVisible = true;

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
        return "System Landscape View";
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
     * Adds the default set of elements to this view.
     */
    @Override
    public void addDefaultElements() {
        addDefaultElements(true);
    }

    /**
     * Adds the default set of elements and relationships to this view.
     *
     * @param greedy    true (add all relationships) or false (add all relationships)
     */
    public void addDefaultElements(boolean greedy) {
        addAllSoftwareSystems();
        addAllPeople();

        getElements().stream().map(ElementView::getElement).forEach(e -> addNearestNeighbours(e, CustomElement.class));
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
        if (element == null) {
            throw new IllegalArgumentException("An element must be specified.");
        }

        if (element instanceof Person || element instanceof SoftwareSystem) {
            super.addNearestNeighbours(element, Person.class);
            super.addNearestNeighbours(element, SoftwareSystem.class);
        } else {
            throw new IllegalArgumentException("A person or software system must be specified.");
        }
    }

    @Deprecated
    public boolean isEnterpriseBoundaryVisible() {
        return enterpriseBoundaryVisible;
    }

    @Deprecated
    void setEnterpriseBoundaryVisible(boolean enterpriseBoundaryVisible) {
        this.enterpriseBoundaryVisible = enterpriseBoundaryVisible;
    }

    @Override
    protected void checkElementCanBeAdded(Element element) {
        if (element instanceof CustomElement || element instanceof Person || element instanceof SoftwareSystem) {
            // all good
        } else {
            throw new ElementNotPermittedInViewException("Only people and software systems can be added to a system landscape view.");
        }
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

}