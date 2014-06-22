package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class View {

    private SoftwareSystem softwareSystem;
    private String description;

    private Set<ElementView> elementViews = new HashSet<>();

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

    public long getSoftwareSystemId() {
        return this.softwareSystem.getId();
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

    public abstract ViewType getType();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void removeElementsWithNoRelationships() {
        Set<RelationshipView> relationships = getRelationships();

        Set<Long> elementIds = new HashSet<>();
        relationships.forEach(rv -> elementIds.add(rv.getSourceId()));
        relationships.forEach(rv -> elementIds.add(rv.getDestinationId()));

        elementViews.removeIf(ev -> !elementIds.contains(ev.getId()));
    }

}
