package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

/**
 * Represents the System Context view from the C4 model. This is the "big picture" view,
 * showing how a software system fits into its environment, in terms of key types of
 * users and system dependencies. The permitted elements in this view are
 * software systems and people.
 */
public class SystemContextView extends StaticView {

    SystemContextView() {
    }

    /**
     * Creates a system context view for the given software system.
     *
     * @param softwareSystem        the SoftwareSystem to create a view for
     */
    SystemContextView(SoftwareSystem softwareSystem) {
        this(softwareSystem, null);
    }

    /**
     * Creates a system context view for the given software system.
     *
     * @param softwareSystem        the SoftwareSystem to create a view for
     * @param description           the (optional) description for the view
     */
    SystemContextView(SoftwareSystem softwareSystem, String description) {
        super(softwareSystem, description);

        addElement(softwareSystem, true);
    }

    @Override
    public final ViewType getType() {
        return ViewType.SystemContext;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - System Context";
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