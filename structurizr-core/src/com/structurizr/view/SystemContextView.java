package com.structurizr.view;

import com.structurizr.model.SoftwareSystem;

/**
 * Represents the System Context view from the C4 model. This is the "big picture" view,
 * showing how a software system fits into its environment, in terms of key types of
 * users and system dependencies. The permitted elements in this view are
 * software systems and people.
 */
public class SystemContextView extends View {

    SystemContextView() {
    }

    /**
     * Creates a system context view for the given software system.
     *
     * @param softwareSystem        the SoftwareSystem to create a view for
     */
    SystemContextView(SoftwareSystem softwareSystem) {
        super(softwareSystem);

        addElement(softwareSystem);
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

}