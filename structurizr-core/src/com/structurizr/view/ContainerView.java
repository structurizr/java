package com.structurizr.view;

import com.structurizr.model.*;

import javax.annotation.Nonnull;

/**
 * Represents a Container view from the C4 model, showing the containers within a given software system.
 */
public final class ContainerView extends StaticView {

    private boolean externalSoftwareSystemBoundariesVisible = false;

    ContainerView() {
    }

    ContainerView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);
    }

    /**
     * Adds a software system to this view, including relationships to/from that software system.
     * Please note that you cannot add the software system that is the scope of this view.
     *
     * @param softwareSystem the SoftwareSystem to add
     */
    @Override
    public void add(@Nonnull SoftwareSystem softwareSystem) {
        add(softwareSystem, true);
    }

    /**
     * Adds all containers within the software system in scope to this view.
     */
    public void addAllContainers() {
        getSoftwareSystem().getContainers().forEach(c -> {
            try {
                add(c);
            } catch (ElementNotPermittedInViewException e) {
                // ignore
            }
        });
    }

    /**
     * Adds an individual container (belonging to any software system) to this view, including relationships to/from that container.
     *
     * @param container the Container to add
     */
    public void add(Container container) {
        add(container, true);
    }

    /**
     * Adds an individual container (belonging to any software system) to this view.
     *
     * @param container         the Container to add
     * @param addRelationships  whether to add relationships to/from the container
     */
    public void add(Container container, boolean addRelationships) {
        addElement(container, addRelationships);
    }

    /**
     * Removes an individual container from this view.
     *
     * @param container the Container to remove
     */
    public void remove(Container container) {
        removeElement(container);
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Containers";
    }

    /**
     * Adds the default set of elements to this view.
     */
    @Override
    public void addDefaultElements() {
        for (Container container : getSoftwareSystem().getContainers()) {
            add(container);
            addNearestNeighbours(container, CustomElement.class);
            addNearestNeighbours(container, Person.class);
            addNearestNeighbours(container, SoftwareSystem.class);
        }
    }

    /**
     * Adds all people, software systems and containers that belong to the software system in scope.
     */
    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
    }

    /**
     * Adds all people, software systems and containers that are directly connected to the specified element.
     *
     * @param element   an Element
     */
    @Override
    public void addNearestNeighbours(@Nonnull Element element) {
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Container.class);
    }

    /**
     * <p>Adds all {@link com.structurizr.model.Container}s of the given {@link ContainerView} as well as all external influencers, that is all
     * persons and all other software systems with incoming or outgoing dependencies.</p>
     * <p>Additionally, all relationships of external dependencies are omitted to keep the diagram clean</p>
     */
    public final void addAllInfluencers() {

        // add all software systems with incoming or outgoing dependencies
        getModel().getSoftwareSystems()
                .stream()
                .filter(softwareSystem -> softwareSystem.hasEfferentRelationshipWith(getSoftwareSystem()) || getSoftwareSystem().hasEfferentRelationshipWith(softwareSystem))
                .forEach(this::add);

        // then add all people with incoming or outgoing dependencies
        getModel().getPeople()
                .stream()
                .filter(person -> person.hasEfferentRelationshipWith(getSoftwareSystem()) || getSoftwareSystem().hasEfferentRelationshipWith(person))
                .forEach(this::add);

        // then remove all relationships of external elements to keep the container view clean
        getRelationships()
                .stream()
                .map(view -> view.getRelationship())
                .filter(relationship -> !isPartOf(relationship.getDestination(), getSoftwareSystem()) && !isPartOf(relationship.getSource(), getSoftwareSystem()))
                .forEach(this::remove);
    }

    /**
     * <p>Adds all {@link com.structurizr.model.Container}s of the given {@link ContainerView} as well as all external influencers, that is all
     * persons and all other software systems with incoming or outgoing dependencies.</p>
     * <p>Additionally, all relationships of external dependencies are omitted to keep the diagram clean</p>
     */
    public final void addAllContainersAndInfluencers() {
        // first add all containers of the underlying software system
        this.addAllContainers();
        addAllInfluencers();
    }

    private static boolean isPartOf(Element element, Element other) {
        if (element.getId().equals(other.getId())) {
            return true;
        } else if (element.getParent() != null) {
            return isPartOf(element.getParent(), other);
        }
        return false;
    }

    /*
     * Adds all {@link SoftwareSystem}s that have efferent {@link com.structurizr.model.Relationship}s with the
     * {@link SoftwareSystem} of this {@link ContainerView}.
     */
    public final void addDependentSoftwareSystems() {
        getModel().getSoftwareSystems().stream()
                .filter(softwareSystem -> softwareSystem.hasEfferentRelationshipWith(this.getSoftwareSystem()))
                .forEach(this::add);
    }

    @Override
    protected void checkElementCanBeAdded(Element element) {
        if (element instanceof CustomElement || element instanceof Person) {
            return;
        }

        if (element instanceof SoftwareSystem) {
            if (element.equals(getSoftwareSystem())) {
                throw new ElementNotPermittedInViewException("The software system in scope cannot be added to a container view.");
            } else {
                checkParentAndChildrenHaveNotAlreadyBeenAdded((SoftwareSystem)element);
                return;
            }
        }

        if (element instanceof Container) {
            checkParentAndChildrenHaveNotAlreadyBeenAdded((Container)element);
            return;
        }

        throw new ElementNotPermittedInViewException("Only people, software systems, and containers can be added to a container view.");
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

    /**
     * Determines whether software system boundaries should be visible for "external" containers (those outside the software system in scope).
     *
     * @return  true if external software system boundaries are visible, false otherwise
     */
    public boolean getExternalSoftwareSystemBoundariesVisible() {
        return externalSoftwareSystemBoundariesVisible;
    }

    /**
     * Sets whether software system boundaries should be visible for "external" containers (those outside the software system in scope).
     *
     * @param externalSoftwareSystemBoundariesVisible     true if external software system boundaries should be visible, false otherwise
     */
    public void setExternalSoftwareSystemBoundariesVisible(boolean externalSoftwareSystemBoundariesVisible) {
        this.externalSoftwareSystemBoundariesVisible = externalSoftwareSystemBoundariesVisible;
    }

}