package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The superclass for all views (static views, dynamic views and deployment views).
 */
public abstract class View {

    private SoftwareSystem softwareSystem;
    private String softwareSystemId;
    private String description = "";
    private String key;
    private PaperSize paperSize = null;
    private String title;

    private Set<ElementView> elementViews = new LinkedHashSet<>();
    private Set<RelationshipView> relationshipViews = new LinkedHashSet<>();

    private ViewSet viewSet;

    View() {
    }

    View(SoftwareSystem softwareSystem, String key, String description) {
        this.softwareSystem = softwareSystem;
        if (!StringUtils.isNullOrEmpty(key)) {
            setKey(key);
        } else {
            throw new IllegalArgumentException("A key must be specified.");
        }
        setDescription(description);
    }

    /**
     * Gets the model that this view belongs to.
     *
     * @return  a Model object
     */
    @JsonIgnore
    public Model getModel() {
        return softwareSystem.getModel();
    }

    /**
     * Gets the software system that this view is associated with.
     *
     * @return  a SoftwareSystem object, or null if this view is not associated with a software system (e.g. it's a system landscape view)
     */
    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return softwareSystem;
    }

    void setSoftwareSystem(SoftwareSystem softwareSystem) {
        this.softwareSystem = softwareSystem;
    }

    /**
     * Gets the ID of the software system this view is associated with.
     *
     * @return the ID, as a String, or null if this view is not associated with a software system (e.g. it's a system landscape view)
     */
    public String getSoftwareSystemId() {
        if (this.softwareSystem != null) {
            return this.softwareSystem.getId();
        } else {
            return this.softwareSystemId;
        }
    }

    void setSoftwareSystemId(String softwareSystemId) {
        this.softwareSystemId = softwareSystemId;
    }

    /**
     * Gets the description of this view.
     *
     * @return  the description, as a String
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    /**
     * Gets the identifier for this view.
     *
     * @return the identifier, as a String
     */
    public String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the paper size that should be used to render this view.
     *
     * @return a PaperSize
     */
    public PaperSize getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        this.paperSize = paperSize;
    }

    /**
     * Gets the title of this view, if one has been set.
     *
     * @return  the title, as a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title for this view.
     *
     * @param title     the title, as a String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return the name, as a String
     */
    @JsonIgnore
    public abstract String getName();

    protected final void addElement(Element element, boolean addRelationships) {
        if (element == null) {
            throw new IllegalArgumentException("An element must be specified.");
        }

        if (getModel().contains(element)) {
            elementViews.add(new ElementView(element));

            if (addRelationships) {
                addRelationships(element);
            }
        } else {
            throw new IllegalArgumentException("The element named " + element.getName() + " does not exist in the model associated with this view.");
        }
    }

    private void addRelationships(Element element) {
        Set<Element> elements = getElements().stream()
                .map(ElementView::getElement)
                .collect(Collectors.toSet());

        // add relationships where the destination exists in the view already
        for (Relationship relationship : element.getRelationships()) {
            if (elements.contains(relationship.getDestination())) {
                this.relationshipViews.add(new RelationshipView(relationship));
            }
        }

        // add relationships where the source exists in the view already
        for (Element e : elements) {
            for (Relationship r : e.getRelationships()) {
                if (r.getDestination().equals(element)) {
                    this.relationshipViews.add(new RelationshipView(r));
                }
            }
        }
    }

    protected void removeElement(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("An element must be specified.");
        }

        if (!canBeRemoved(element)) {
            throw new IllegalArgumentException("The element named '" + element.getName() + "' cannot be removed from this view.");
        }

        ElementView elementView = new ElementView(element);
        elementViews.remove(elementView);

        for (RelationshipView relationshipView : getRelationships()) {
            if (relationshipView.getRelationship().getSource().equals(element) ||
                    relationshipView.getRelationship().getDestination().equals(element)) {
                remove(relationshipView.getRelationship());
            }
        }
    }

    protected RelationshipView addRelationship(Relationship relationship) {
        if (relationship == null) {
            throw new IllegalArgumentException("A relationship must be specified.");
        }

        if (isElementInView(relationship.getSource()) && isElementInView(relationship.getDestination())) {
            RelationshipView relationshipView = new RelationshipView(relationship);
            relationshipViews.add(relationshipView);

            return relationshipView;
        }

        return null;
    }

    protected boolean isElementInView(Element element) {
        return this.elementViews.stream().filter(ev -> ev.getElement().equals(element)).count() > 0;
    }

    protected RelationshipView addRelationship(Relationship relationship, String description, String order) {
        RelationshipView relationshipView = addRelationship(relationship);
        if (relationshipView != null) {
            relationshipView.setDescription(description);
            relationshipView.setOrder(order);
        }

        return relationshipView;
    }

    /**
     * Removes a relationship from this view.
     *
     * @param relationship      the Relationship to remove
     */
    public void remove(Relationship relationship) {
        if (relationship != null) {
            RelationshipView relationshipView = new RelationshipView(relationship);
            relationshipViews.remove(relationshipView);
        }
    }

    /**
     * Removes relationships that are not connected to the specified element.
     *
     * @param element       the Element to test against
     */
    public void removeRelationshipsNotConnectedToElement(Element element) {
        if (element != null) {
            getRelationships().stream()
                    .map(RelationshipView::getRelationship)
                    .filter(r -> !r.getSource().equals(element) && !r.getDestination().equals(element))
                    .forEach(this::remove);
        }
    }

    /**
     * Gets the set of elements in this view.
     *
     * @return a Set of ElementView objects
     */
    public Set<ElementView> getElements() {
        return new HashSet<>(elementViews);
    }

    void setElements(Set<ElementView> elementViews) {
        if (elementViews != null) {
            this.elementViews = new HashSet<>(elementViews);
        }
    }

    /**
     * Gets the set of relationships in this view.
     *
     * @return a Set of RelationshipView objects
     */
    public Set<RelationshipView> getRelationships() {
        return new HashSet<>(this.relationshipViews);
    }

    void setRelationships(Set<RelationshipView> relationshipViews) {
        if (relationshipViews != null) {
            this.relationshipViews = new HashSet<>(relationshipViews);
        }
    }

    /**
     * Removes all elements that have no relationships to other elements in this view.
     */
    public void removeElementsWithNoRelationships() {
        Set<RelationshipView> relationships = getRelationships();

        Set<String> elementIds = new HashSet<>();
        relationships.forEach(rv -> elementIds.add(rv.getRelationship().getSourceId()));
        relationships.forEach(rv -> elementIds.add(rv.getRelationship().getDestinationId()));

        for (ElementView elementView : getElements()) {
            if (!elementIds.contains(elementView.getId())) {
                removeElement(elementView.getElement());
            }
        }
    }

    /**
     * Attempts to copy the visual layout information (e.g. x,y coordinates) of elements and relationships
     * from the specified source view into this view.
     *
     * @param source    the source View
     */
    void copyLayoutInformationFrom(@Nonnull View source) {
        if (this.getPaperSize() == null) {
            this.setPaperSize(source.getPaperSize());
        }

        for (ElementView sourceElementView : source.getElements()) {
            ElementView destinationElementView = findElementView(sourceElementView);
            if (destinationElementView != null) {
                destinationElementView.copyLayoutInformationFrom(sourceElementView);
            }
        }

        for (RelationshipView sourceRelationshipView : source.getRelationships()) {
            RelationshipView destinationRelationshipView = findRelationshipView(sourceRelationshipView);
            if (destinationRelationshipView != null) {
                destinationRelationshipView.copyLayoutInformationFrom(sourceRelationshipView);
            }
        }
    }

    private ElementView findElementView(ElementView sourceElementView) {
        for (ElementView elementView : getElements()) {
            if (elementView.getElement().equals(sourceElementView.getElement())) {
                return elementView;
            }
        }

        return null;
    }

    /**
     * Gets the element view for the given element.
     *
     * @param element   the Element to find the ElementView for
     * @return  an ElementView object, or null if the element doesn't exist in the view
     */
    public ElementView getElementView(@Nonnull Element element) {
        Optional<ElementView> elementView = this.elementViews.stream().filter(ev -> ev.getId().equals(element.getId())).findFirst();
        return elementView.orElse(null);
    }

    protected RelationshipView findRelationshipView(@Nonnull RelationshipView sourceRelationshipView) {
        for (RelationshipView relationshipView : getRelationships()) {
            if (relationshipView.getRelationship().equals(sourceRelationshipView.getRelationship())) {
                return relationshipView;
            }
        }

        return null;
    }

    /**
     * Gets the relationship view for the given relationship.
     *
     * @param relationship  the Relationship to find the RelationshipView for
     * @return  an RelationshipView object, or null if the relationship doesn't exist in the view
     */
    public RelationshipView getRelationshipView(@Nonnull Relationship relationship) {
        Optional<RelationshipView> relationshipView = this.relationshipViews.stream().filter(rv -> rv.getId().equals(relationship.getId())).findFirst();
        return relationshipView.orElse(null);
    }

    void setViewSet(@Nonnull ViewSet viewSet) {
        this.viewSet = viewSet;
    }

    /**
     * Gets the view set that this view belongs to.
     *
     * @return  a ViewSet object
     */
    @JsonIgnore
    public ViewSet getViewSet() {
        return viewSet;
    }

    protected abstract boolean canBeRemoved(Element element);

}