package com.structurizr.view;

import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

import javax.annotation.Nonnull;

/**
 * Represents the System Context view from the C4 model, showing how a software system fits into its environment,
 * in terms of the users (people) and other software system dependencies.
 */
public final class SystemContextView extends StaticView {

    private boolean enterpriseBoundaryVisible = true;

    SystemContextView() {
    }

    SystemContextView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);

        addElement(softwareSystem, true);
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        return "System Context View: " + getSoftwareSystem().getName();
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
     * @param greedy    true (add all relationships) or false (adds relationships to/from the scoped software system only)
     */
    public void addDefaultElements(boolean greedy) {
        addNearestNeighbours(getSoftwareSystem(), CustomElement.class);
        addNearestNeighbours(getSoftwareSystem(), Person.class);
        addNearestNeighbours(getSoftwareSystem(), SoftwareSystem.class);

        if (!greedy) {
            removeRelationshipsNotConnectedToElement(getSoftwareSystem());
        }
    }

    /**
     * Adds all software systems and all people.
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
            throw new ElementNotPermittedInViewException("Only people and software systems can be added to a system context view.");
        }
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return !getSoftwareSystem().equals(element);
    }

}