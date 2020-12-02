package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * A dynamic view, used to describe behaviour between static elements at runtime.
 */
public final class DynamicView extends View {

    private Model model;

    private Element element;
    private String elementId;

    private boolean externalBoundariesVisible = false;

    private SequenceNumber sequenceNumber = new SequenceNumber();

    DynamicView() {
    }

    DynamicView(Model model, String key, String description) {
        super(null, key, description);

        setModel(model);
        setElement(null);
    }

    DynamicView(SoftwareSystem softwareSystem, String key, String description) {
        super(softwareSystem, key, description);

        setModel(softwareSystem.getModel());
        setElement(softwareSystem);
    }

    DynamicView(Container container, String key, String description) {
        super(container.getSoftwareSystem(), key, description);

        setModel(container.getModel());
        setElement(container);
    }

    @JsonIgnore
    @Override
    public Model getModel() {
        return this.model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    @Override
    @JsonIgnore
    public String getSoftwareSystemId() {
        return super.getSoftwareSystemId();
    }

    /**
     * Gets the ID of the software system or container associated with this view.
     *
     * @return the ID, as a String, or null if not set
     */
    public String getElementId() {
        if (this.element != null) {
            return element.getId();
        } else {
            return this.elementId;
        }
    }

    void setElementId(String elementId) {
        this.elementId = elementId;
    }

    @JsonIgnore
    public Element getElement() {
        return element;
    }

    void setElement(Element element) {
        this.element = element;

        if (element instanceof SoftwareSystem) {
            setSoftwareSystem((SoftwareSystem)element);
        }
    }

    public RelationshipView add(@Nonnull Element source, @Nonnull Element destination) {
        return add(source, "", destination);
    }

    public RelationshipView add(@Nonnull Element source, String description, @Nonnull Element destination) {
        return add(source, description, "", destination);
    }

    public RelationshipView add(@Nonnull Element source, String description, String technology, @Nonnull Element destination) {
        if (source == null) {
            throw new IllegalArgumentException("A source element must be specified.");
        }

        if (destination == null) {
            throw new IllegalArgumentException("A destination element must be specified.");
        }

        checkElement(source);
        checkElement(destination);

        // check that the relationship is in the model before adding it
        Relationship relationship = null;

        if (StringUtils.isNullOrEmpty(technology)) {
            // no technology is specified, so just pick the first relationship we find
            relationship = source.getEfferentRelationshipWith(destination);
        } else {
            Set<Relationship> relationships = source.getEfferentRelationshipsWith(destination);
            for (Relationship rel : relationships) {
                if (technology.equals(rel.getTechnology())) {
                    relationship = rel;
                }
            }
        }

        if (relationship != null) {
            addElement(source, false);
            addElement(destination, false);

            return addRelationship(relationship, description, sequenceNumber.getNext(), false);
        } else {
            // perhaps model this as a return/reply/response message instead, if the reverse relationship exists

            if (StringUtils.isNullOrEmpty(technology)) {
                // no technology is specified, so just pick the first relationship we find
                relationship = destination.getEfferentRelationshipWith(source);
            } else {
                Set<Relationship> relationships = destination.getEfferentRelationshipsWith(source);
                for (Relationship rel : relationships) {
                    if (technology.equals(rel.getTechnology())) {
                        relationship = rel;
                    }
                }
            }

            if (relationship != null) {
                addElement(source, false);
                addElement(destination, false);

                return addRelationship(relationship, description, sequenceNumber.getNext(), true);
            } else {
                if (StringUtils.isNullOrEmpty(technology)) {
                    throw new IllegalArgumentException("A relationship between " + source.getName() + " and " + destination.getName() + " does not exist in model.");
                } else {
                    throw new IllegalArgumentException("A relationship between " + source.getName() + " and " + destination.getName() + " with technology " + technology + " does not exist in model.");
                }
            }
        }
    }

    protected RelationshipView addRelationship(Relationship relationship, String description, String order, boolean response) {
        RelationshipView relationshipView = addRelationship(relationship);
        if (relationshipView != null) {
            relationshipView.setDescription(description);
            relationshipView.setOrder(order);
            relationshipView.setResponse(response);
        }

        return relationshipView;
    }

    /**
     * This checks that only appropriate elements can be added to the view.
     */
    private void checkElement(Element elementToBeAdded) {
        if (!(elementToBeAdded instanceof Person) && !(elementToBeAdded instanceof SoftwareSystem) && !(elementToBeAdded instanceof Container) && !(elementToBeAdded instanceof Component)) {
            throw new IllegalArgumentException("Only people, software systems, containers and components can be added to dynamic views.");
        }

        // people can always be added
        if (elementToBeAdded instanceof Person) {
            return;
        }

        // if the scope of this dynamic view is a software system, we only want:
        //  - containers
        //  - other software systems
        if (element instanceof SoftwareSystem) {
            if (elementToBeAdded.equals(element)) {
                throw new IllegalArgumentException(elementToBeAdded.getName() + " is already the scope of this view and cannot be added to it.");
            }

            if (elementToBeAdded instanceof Component) {
                throw new IllegalArgumentException("Components can't be added to a dynamic view when the scope is a software system.");
            }
        }

        // if the scope of this dynamic view is a container, we only want:
        //  - other containers
        //  - components
        if (element instanceof Container) {
            if (elementToBeAdded.equals(element) || elementToBeAdded.equals(element.getParent())) {
                throw new IllegalArgumentException(elementToBeAdded.getName() + " is already the scope of this view and cannot be added to it.");
            }
        }

        if (element == null) {
            if (!(elementToBeAdded instanceof SoftwareSystem)) {
                throw new IllegalArgumentException("Only people and software systems can be added to this dynamic view.");
            }
        }
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        if (element != null) {
            return element.getName() + " - Dynamic";
        } else {
            return "Dynamic";
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        List<RelationshipView> list = new ArrayList<>(getRelationships());
        Collections.sort(list, (rv1, rv2) -> rv1.getOrder().compareTo(rv2.getOrder()));
        list.forEach(rv -> buf.append(rv.toString() + "\n"));

        return buf.toString();
    }

    public void startParallelSequence() {
        sequenceNumber.startParallelSequence();
    }

    public void endParallelSequence() {
        endParallelSequence(false);
    }

    public void endParallelSequence(boolean endAllParallelSequencesAndContinueNumbering) {
        sequenceNumber.endParallelSequence(endAllParallelSequencesAndContinueNumbering);
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

    /**
     * Gets the set of RelationshipView objects for this view, ordered by the order property.
     *
     * @return  an ordered set of RelationshipView objects
     */
    @Override
    public Set<RelationshipView> getRelationships() {
        List<RelationshipView> list = new LinkedList<>(super.getRelationships());
        boolean ordersAreNumeric = true;

        for (RelationshipView relationshipView : list) {
            ordersAreNumeric = ordersAreNumeric && isNumeric(relationshipView.getOrder());
        }

        if (ordersAreNumeric) {
            list.sort(Comparator.comparingDouble(rv -> Double.parseDouble(rv.getOrder())));
        } else {
            list.sort(Comparator.comparing(RelationshipView::getOrder));
        }

        return new LinkedHashSet<>(list);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Determines whether software system/container boundaries should be visible for "external" containers/components (those outside the element in scope).
     *
     * @return  true if external boundaries are visible, false otherwise
     */
    public boolean getExternalBoundariesVisible() {
        return externalBoundariesVisible;
    }

    /**
     * Sets whether software system/container boundaries should be visible for "external" containers/components (those outside the element in scope).
     *
     * @param externalBoundariesVisible     true if external boundaries should be visible, false otherwise
     */
    public void setExternalBoundariesVisible(boolean externalBoundariesVisible) {
        this.externalBoundariesVisible = externalBoundariesVisible;
    }

}