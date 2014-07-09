package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class View implements Comparable<View> {

    private SoftwareSystem softwareSystem;
    private int softwareSystemId;
    private String description = "";

    private Set<ElementView> elementViews = new HashSet<>();

    protected View() {
    }

    public View(SoftwareSystem softwareSystem) {
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

    public int getSoftwareSystemId() {
        if (this.softwareSystem != null) {
            return this.softwareSystem.getId();
        } else {
            return this.softwareSystemId;
        }
    }

    public void setSoftwareSystemId(int softwareSystemId) {
        this.softwareSystemId = softwareSystemId;
    }

    public void add(SoftwareSystem softwareSystem) {
        addElement(softwareSystem);
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
        // do nothing
    }

    public abstract ViewType getType();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void removeElementsWithNoRelationships() {
        Set<RelationshipView> relationships = getRelationships();

        Set<Integer> elementIds = new HashSet<>();
        relationships.forEach(rv -> elementIds.add(rv.getSourceId()));
        relationships.forEach(rv -> elementIds.add(rv.getDestinationId()));

        elementViews.removeIf(ev -> !elementIds.contains(ev.getId()));
    }

    public void removeElementsThatCantBeReachedFrom(Element element) {
        Set<Integer> elementIdsToShow = new HashSet<>();
        findElementsToShow(element, elementIdsToShow, 1);

        elementViews.removeIf(ev -> !elementIdsToShow.contains(ev.getId()));
    }

    private void findElementsToShow(Element element, Set<Integer> elementIds, int depth) {
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

}
