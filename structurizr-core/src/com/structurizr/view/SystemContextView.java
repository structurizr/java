package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

import javax.annotation.Nonnull;

/**
 * Represents the System Context view from the C4 model, showing how a software system fits into its environment,
 * in terms of the users (people) and other software system dependencies.
 */
public final class SystemContextView extends StaticView {

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
        return getSoftwareSystem().getName() + " - System Context";
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
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, SoftwareSystem.class);
    }

}