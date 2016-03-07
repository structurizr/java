package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class View implements Comparable<View> {

    private SoftwareSystem softwareSystem;
    private String softwareSystemId;
    private String description = "";
    private String key;
    private PaperSize paperSize = PaperSize.A4_Portrait;

    private Set<ElementView> elementViews = new LinkedHashSet<>();

    private Set<RelationshipView> relationshipViews = new LinkedHashSet<>();

    View() {
    }

    View(SoftwareSystem softwareSystem, String description) {
        this.softwareSystem = softwareSystem;
        setDescription(description);
    }

    @JsonIgnore
    public Model getModel() {
        return softwareSystem.getModel();
    }

    @JsonIgnore
    public SoftwareSystem getSoftwareSystem() {
        return softwareSystem;
    }

    public void setSoftwareSystem(SoftwareSystem softwareSystem) {
        this.softwareSystem = softwareSystem;
    }

    /**
     * Gets the ID of the software system this view is associated with.
     *
     * @return  the ID, as a String
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
     * Gets the identifer for this view.
     *
     * @return  the identifier, as a String,
     *          or null if no key has been specified
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets the paper size that should be used to render this view.
     *
     * @return  a PaperSize (A4_Portrait by default)
     */
    public PaperSize getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        this.paperSize = paperSize;
    }

    /**
     * Gets the name of this view.
     *
     * @return  the name, as a String
     */
    @JsonIgnore
    public abstract String getName();

    @JsonIgnore
    public String getTitle() {
        if (getDescription() != null && getDescription().trim().length() > 0) {
            return getName() + " [" + getDescription() + "]";
        } else {
            return getName();
        }
    }

    protected final void addElement(Element element, boolean addRelationships) {
        if (element != null) {
            if (getSoftwareSystem().getModel().contains(element)) {
                elementViews.add(new ElementView(element));

                if (addRelationships) {
                    addRelationships(element);
                }
            }
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

    public void remove(Element element) {
        removeElement(element);
    }

    protected void removeElement(Element element) {
        if (element != null) {
            ElementView elementView = new ElementView(element);
            elementViews.remove(elementView);

            for (RelationshipView relationshipView : getRelationships()) {
                if (relationshipView.getRelationship().getSource().equals(element) ||
                        relationshipView.getRelationship().getDestination().equals(element)) {
                    removeRelationship(relationshipView.getRelationship());
                }
            }
        }
    }

    /**
     * @deprecated use {@link View#add(com.structurizr.model.Relationship)}
     */
    @Deprecated
    public RelationshipView addRelationship(Relationship relationship) {
        return add(relationship);
    }

    public RelationshipView add(Relationship relationship) {
        if (relationship != null) {
            if (isElementInView(relationship.getSource()) && isElementInView(relationship.getDestination())) {
                RelationshipView relationshipView = new RelationshipView(relationship);
                relationshipViews.add(relationshipView);

                return relationshipView;
            }
        }

        return null;
    }

    private boolean isElementInView(Element element) {
        return this.elementViews.stream().filter(ev -> ev.getElement().equals(element)).count() > 0;
    }

    protected void addRelationship(Relationship relationship, String description, String order) {
        RelationshipView relationshipView = add(relationship);
        if (relationshipView != null) {
            relationshipView.setDescription(description);
            relationshipView.setOrder(order);
        }
    }

    /**
     * @deprecated use {@link View#remove(com.structurizr.model.Relationship)}
     */
    @Deprecated
    public void removeRelationship(Relationship relationship) {
        remove(relationship);
    }

    public void remove(Relationship relationship) {
        if (relationship != null) {
            RelationshipView relationshipView = new RelationshipView(relationship);
            relationshipViews.remove(relationshipView);
        }
    }

    /**
     * Gets the set of elements in this view.
     *
     * @return  a Set of ElementView objects
     */
    public Set<ElementView> getElements() {
        return new HashSet<>(elementViews);
    }

    void setElements(Set<ElementView> elementViews) {
        this.elementViews = elementViews;
    }

    /**
     * Gets the set of relationships in this view.
     *
     * @return  a Set of RelationshipView objects
     */
    public Set<RelationshipView> getRelationships() {
        return new HashSet<>(this.relationshipViews);
    }

    public void setRelationships(Set<RelationshipView> relationships) {
        this.relationshipViews = relationships;
    }

    /**
     * Removes all elements that have no relationships
     * to other elements in this view.
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

    public void copyLayoutInformationFrom(View source) {
        this.setPaperSize(source.getPaperSize());

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

    public ElementView getElementView(Element element) {
        Optional<ElementView> elementView = this.elementViews.stream().filter(ev -> ev.getElement().equals(element)).findFirst();
        return elementView.isPresent() ? elementView.get() : null;
    }

    RelationshipView findRelationshipView(RelationshipView sourceRelationshipView) {
        for (RelationshipView relationshipView : getRelationships()) {
            if (relationshipView.getRelationship().equals(sourceRelationshipView.getRelationship())) {
                return relationshipView;
            }
        }

        return null;
    }

    public RelationshipView getRelationshipView(Relationship relationship) {
        Optional<RelationshipView> relationshipView = this.relationshipViews.stream().filter(rv -> rv.getRelationship().equals(relationship)).findFirst();
        return relationshipView.isPresent() ? relationshipView.get() : null;
    }

    @Override
    public int compareTo(View view) {
        return getTitle().compareTo(view.getTitle());
    }

}