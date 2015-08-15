package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
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

    public abstract ViewType getType();

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PaperSize getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        this.paperSize = paperSize;
    }

    /**
     * Adds all software systems in the model to this view.
     */
    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().forEach(this::addElement);
    }

    /**
     * Adds the given software system to this view.
     *
     * @param softwareSystem        the SoftwareSystem to add
     */
    public void addSoftwareSystem(SoftwareSystem softwareSystem) {
        addElement(softwareSystem);
    }

    /**
     * Adds all software systems in the model to this view.
     */
    public void addAllPeople() {
        getModel().getPeople().forEach(this::addElement);
    }

    /**
     * Adds the given person to this view.
     *
     * @param person        the Person to add
     */
    public void addPerson(Person person) {
        addElement(person);
    }

    protected final void addElement(Element element) {
        if (element != null) {
            if (softwareSystem.getModel().contains(element)) {
                elementViews.add(new ElementView(element));
                addRelationships(element);
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

    protected void removeElement(Element element) {
        if (element != null) {
            ElementView elementView = new ElementView(element);
            elementViews.remove(elementView);
        }
    }

    public void removeRelationship(Relationship relationship) {
        getRelationships();

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
        return elementViews;
    }

    void setElements(Set<ElementView> elementViews) {
        this.elementViews = elementViews;
    }

    public abstract void addAllElements();

    public abstract void addNearestNeighbours(Element element);

    protected void addNearestNeighbours(Element element, ElementType type) {
        if (element == null) {
            return;
        }

        addElement(element);

        Set<Relationship> relationships = getModel().getRelationships();
        relationships.stream().filter(r -> r.getSource().equals(element) && r.getDestination().isType(type))
                .map(Relationship::getDestination)
                .forEach(this::addElement);

        relationships.stream().filter(r -> r.getDestination().equals(element) && r.getSource().isType(type))
                .map(Relationship::getSource)
                .forEach(this::addElement);
    }

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

        elementViews.removeIf(ev -> !elementIds.contains(ev.getId()));
    }

    /**
     * Removes all elements that cannot be reached by traversing the graph of relationships
     * starting with the specified element.
     *
     * @param element       the starting element
     */
    public void removeElementsThatCantBeReachedFrom(Element element) {
        if (element != null) {
            Set<String> elementIdsToShow = new HashSet<>();
            Set<String> elementIdsVisited = new HashSet<>();
            findElementsToShow(element, element, elementIdsToShow, elementIdsVisited);

            elementViews.removeIf(ev -> !elementIdsToShow.contains(ev.getId()));
        }
    }

    private void findElementsToShow(Element startingElement, Element element, Set<String> elementIdsToShow, Set<String> elementIdsVisited) {
        if (!elementIdsVisited.contains(element.getId()) && elementViews.contains(new ElementView(element))) {
            elementIdsVisited.add(element.getId());
            elementIdsToShow.add(element.getId());

            // check that we've not gone back to the starting point of the graph
            if (!element.hasEfferentRelationshipWith(startingElement)) {
                element.getRelationships().forEach(r -> findElementsToShow(startingElement, r.getDestination(), elementIdsToShow, elementIdsVisited));
            }
        }
    }

    public abstract String getName();

    @Override
    public int compareTo(View view) {
        return getTitle().compareTo(view.getTitle());
    }

    @JsonIgnore
    public String getTitle() {
        if (getDescription() != null && getDescription().trim().length() > 0) {
            return getName() + " [" + getDescription() + "]";
        } else {
            return getName();
        }
    }

    ElementView findElementView(Element element) {
        for (ElementView elementView : getElements()) {
            if (elementView.getElement().equals(element)) {
                return elementView;
            }
        }

        return null;
    }

    RelationshipView findRelationshipView(Relationship relationship) {
        for (RelationshipView relationshipView : getRelationships()) {
            if (relationshipView.getRelationship().equals(relationship)) {
                return relationshipView;
            }
        }

        return null;
    }

    public void copyLayoutInformationFrom(View source) {
        this.setPaperSize(source.getPaperSize());

        for (ElementView sourceElementView : source.getElements()) {
            ElementView destinationElementView = findElementView(sourceElementView.getElement());
            if (destinationElementView != null) {
                destinationElementView.copyLayoutInformationFrom(sourceElementView);
            }
        }

        for (RelationshipView sourceRelationshipView : source.getRelationships()) {
            RelationshipView destinationRelationshipView = findRelationshipView(sourceRelationshipView.getRelationship());
            if (destinationRelationshipView != null) {
                destinationRelationshipView.copyLayoutInformationFrom(sourceRelationshipView);
            }
        }
    }

}