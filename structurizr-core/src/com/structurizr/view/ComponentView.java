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
     * <p>Adds all {@link Element}s external to the container (Person, SoftwareSystem or Container)
     * that have {@link Relationship}s <b>to</b> or <b>from</b> {@link Component}s in this view.</p>
     * <p>Not included are:</p>
     * <ul>
     * <li>References to and from the {@link Container} of this view (only references to and from the components are considered)</li>
     * <li>{@link Relationship}s between external {@link Element}s (i.e. elements that are not part of this container)</li>
     * </ul>
     * <p>Don't forget to add elements to your view prior to calling this method, e.g. by calling {@link #addAllComponents()}
     * or be selectively choosing certain components.</p>
     */
    public void addExternalDependencies() {
        final Set<Element> components = new HashSet<>();
        getElements().stream()
                .map(ElementView::getElement)
                .filter(e -> e instanceof Component)
                .forEach(components::add);

        // add relationships of all other elements to or from our inside components
        for (Relationship relationship : getContainer().getModel().getRelationships()) {
            if (components.contains(relationship.getSource())) {
                addDependency(relationship.getDestination(), components);
            }
            if (components.contains(relationship.getDestination())) {
                addDependency(relationship.getSource(), components);
            }
        }

        // remove all relationships between elements outside of this container
        getRelationships().stream()
                .map(RelationshipView::getRelationship)
                .filter(r -> !components.contains(r.getSource()) && !components.contains(r.getDestination()))
                .forEach(this::remove);
    }

    private void addDependency(Element element, Set<Element> components) {
        if (element instanceof Component && !element.getParent().equals(getContainer())) {
            final Container container = ((Component) element).getContainer();
            // in case there is a dependency from a component of another dependency to one of our elements,
            // we add its parent container instead
            addElement(container, true);

            if (!hasAnyRelationship(container, components)) {
                LOG.warn(String.format("Container %s was added to the ComponentView '%s' because its component %s has a relationship " +
                        "with one of the elements inside this diagram. Nevertheless, the container does not have any relationship " +
                        "to the elements of this diagram. You might add one manually or call Model#addImplicitRelationships() " +
                        "to add all implicit relationships automatically", container, getName(), element));
            }

        } else {
            addElement(element, true);
        }
    }

    private boolean hasAnyRelationship(Container container, Set<Element> components) {
        for (Element component : components) {
            if (component.hasEfferentRelationshipWith(container) || container.hasEfferentRelationshipWith(component)) {
                return true;
            }
        }
        return false;
    }

}