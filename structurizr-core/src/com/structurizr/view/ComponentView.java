package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashSet;
import java.util.Set;

public class ComponentView extends StaticView {

    private Container container;
    private String containerId;

    private static final Log LOG = LogFactory.getLog(ComponentView.class);

    ComponentView() {
    }

    ComponentView(Container container, String description) {
        super(container.getSoftwareSystem(), description);

        this.container = container;
    }

    /**
     * Gets the ID of the container associated with this view.
     *
     * @return the ID, as a String
     */
    public String getContainerId() {
        if (this.container != null) {
            return container.getId();
        } else {
            return this.containerId;
        }
    }

    void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @JsonIgnore
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
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
        getSoftwareSystem().getContainers().stream()
                .forEach(this::add);
    }

    /**
     * Adds an individual container to this view.
     *
     * @param container the Container to add
     */
    public void add(Container container) {
        if (container != null && !container.equals(getContainer())) {
            addElement(container, true);
        }
    }

    /**
     * Adds all components in the container to this view.
     */
    public void addAllComponents() {
        container.getComponents().forEach(this::add);
    }

    /**
     * Adds an individual component to this view.
     *
     * @param component the Component to add
     */
    public void add(Component component) {
        if (component != null) {
            if (component.getContainer().equals(getContainer())) {
                addElement(component, true);
            } else {
                LOG.warn(String.format("Component %s is not component of %s and thus cannot be added to its ComponentView", component, getContainer()));
            }
        }
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
     * Removes an individual component from this view.
     *
     * @param component the Component to remove
     */
    public void remove(Component component) {
        removeElement(component);
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - " + getContainer().getName() + " - Components";
    }

    @Override
    public void addAllElements() {
        addAllSoftwareSystems();
        addAllPeople();
        addAllContainers();
        addAllComponents();
    }

    @Override
    public void addNearestNeighbours(Element element) {
        super.addNearestNeighbours(element, SoftwareSystem.class);
        super.addNearestNeighbours(element, Person.class);
        super.addNearestNeighbours(element, Container.class);
        super.addNearestNeighbours(element, Component.class);
    }

    /**
     * <p>Calling this method gives you an isolated view of this {@link Container} with all its
     * components and their ingoing/outgoing relationships. Effectively, the following components
     * and relationships are added to the view:</p>
     * <ul>
     * <li>all other {@link Element}s (Person, SoftwareSystem, Container or Component) that have direct {@link Relationship}s to
     * all {@link Component}s in this view</li>
     * <li>all other {@link Element}s (Person, SoftwareSystem, Container or Component) that are referenced by one
     * of the {@link Component}s in this view</li>
     * </ul>
     * <p>
     * Not included are:
     * <ul>
     * <li>References to and from the {@link Container} of this view (only references to and from the components are considered)</li>
     * <li>{@link Relationship}s between external {@link Element}s (i.e. elements that are not part of this container)</li>
     * </ul>
     * </p>
     * <p>Don't forget to add elements to your view prior to calling this method, e.g. by calling {@link #addAllComponents()}
     * or be selectively choosing certain components.</p>
     */
    public void addDirectDependencies() {
        final Set<Element> insideElements = new HashSet<>();
        getElements().stream()
                .map(ElementView::getElement)
                .filter(e -> e instanceof Component)
                .forEach(insideElements::add);

        // add relationships of all other elements to or from our inside components
        for (Relationship relationship : getContainer().getModel().getRelationships()) {
            if (insideElements.contains(relationship.getSource())) {
                addDependency(relationship.getDestination(), insideElements);
            }
            if (insideElements.contains(relationship.getDestination())) {
                addDependency(relationship.getSource(), insideElements);
            }
        }

        // remove all relationships between outside components, we don't care about them here
        getRelationships().stream()
                .map(RelationshipView::getRelationship)
                .filter(r -> !insideElements.contains(r.getSource()) && !insideElements.contains(r.getDestination()))
                .forEach(this::remove);
    }

    private void addDependency(Element element, Set<Element> insideElements) {
        if (element instanceof Component && !element.getParent().equals(getContainer())) {
            final Container container = ((Component) element).getContainer();
            // in case there is a dependency from a component of another dependency to one of our elements,
            // we add its parent container instead
            addElement(container, true);

            if (!hasAnyRelationship(container, insideElements)) {
                LOG.warn(String.format("Container %s was added to the ComponentView '%s' because its component %s has a relationship " +
                        "with one of the elements inside this diagram. Nevertheless, the container does not have any relationship " +
                        "to the elements of this diagram. You might add one manually or call Model#addImplicitRelationships() " +
                        "to add all implicit relationships automatically", container, getName(), element));
            }

        } else {
            addElement(element, true);
        }
    }

    private boolean hasAnyRelationship(Container container, Set<Element> insideElements) {
        for (Element insideElement : insideElements) {
            if (insideElement.hasEfferentRelationshipWith(container) || container.hasEfferentRelationshipWith(insideElement)) {
                return true;
            }
        }
        return false;
    }

}