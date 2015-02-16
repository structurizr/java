package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.structurizr.model.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class View implements Comparable<View> {

    private SoftwareSystem softwareSystem;
    private String softwareSystemId;
    private String description = "";
    private PaperSize paperSize = PaperSize.A4_Portrait;

    private Set<ElementView> elementViews = new LinkedHashSet<>();

    View() {
    }

    View(SoftwareSystem softwareSystem) {
        this.softwareSystem = softwareSystem;
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
        this.description = description;
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

    protected void addElement(Element element) {
        if (softwareSystem.getModel().contains(element)) {
            elementViews.add(new ElementView(element));
        }
    }

    protected void removeElement(Element element) {
        ElementView elementView = new ElementView(element);
        elementViews.remove(elementView);
    }

    /**
     * Gets the set of elements in this view.
     *
     * @return  a Set of ElementView objects
     */
    public Set<ElementView> getElements() {
        return elementViews;
    }

    public Set<RelationshipView> getRelationships() {
        Set<Relationship> relationships = new HashSet<>();
        Set<Element> elements = getElements().stream()
                .map(ElementView::getElement)
                .collect(Collectors.toSet());

        elements.forEach(b -> relationships.addAll(b.getRelationships()));

        return relationships.stream()
                .filter(r -> elements.contains(r.getSource()) && elements.contains(r.getDestination()))
                .map(RelationshipView::new)
                .collect(Collectors.toSet());
    }

    public void setRelationships(Set<RelationshipView> relationships) {
        // do nothing ... this are determined automatically
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

    public void removeElementsThatCantBeReachedFrom(Element element) {
        Set<String> elementIdsToShow = new HashSet<>();
        findElementsToShow(element, elementIdsToShow, 1);

        elementViews.removeIf(ev -> !elementIdsToShow.contains(ev.getId()));
    }

    private void findElementsToShow(Element element, Set<String> elementIds, int depth) {
        if (elementViews.contains(new ElementView(element))) {
            elementIds.add(element.getId());
            if (depth < 100) {
                element.getRelationships().forEach(r -> findElementsToShow(r.getDestination(), elementIds, depth + 1));
            }
        }
    }

    public abstract String getName();

    @Override
    public int compareTo(View view) {
        return getTitle().compareTo(view.getTitle());
    }

    private String getTitle() {
        return getName() + " - " + getDescription();
    }

    ElementView findElementView(Element element) {
        for (ElementView elementView : getElements()) {
            if (elementView.getElement().equals(element)) {
                return elementView;
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
    }

}