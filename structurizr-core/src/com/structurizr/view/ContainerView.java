package com.structurizr.view;

import com.structurizr.model.Container;
import com.structurizr.model.Element;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;

public final class ContainerView extends StaticView {

    ContainerView() {
    }

    ContainerView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);
    }

    @Override
    public void add(SoftwareSystem softwareSystem) {
        if (softwareSystem != null && !softwareSystem.equals(getSoftwareSystem())) {
            addElement(softwareSystem, true);
        }
    }

    /**
     * Adds all containers in the software system to this view.
     */
    public void addAllContainers() {
        getSoftwareSystem().getContainers().forEach(this::add);
    }

    /**
     * Adds an individual container to this view.
     *
     * @param container the Container to add
     */
    public void add(Container container) {
        addElement(container, true);
    }

    /**
     * Removes an individual container from this view.
     *
     * @param container the Container to remove
     */
    public void remove(Container container) {
        removeElement(container);
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Containers";
    }

    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
    }

    @Override
    public void addNearestNeighbours(Element element) {
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Person.class);
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

     * Adds all {@link SoftwareSystem}s that have efferent {@link com.structurizr.model.Relationship}s with the
     * {@link SoftwareSystem} of this {@link ContainerView}.
     */
    public final void addDependentSoftwareSystems() {
        getModel().getSoftwareSystems().stream()
                .filter(softwareSystem -> softwareSystem.hasEfferentRelationshipWith(this.getSoftwareSystem()))
                .forEach(this::add);
    }


}