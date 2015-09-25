package com.structurizr.view;

import com.structurizr.model.*;

public class DynamicView extends View {

    private HierarchicalSequenceCounter counter = new HierarchicalSequenceCounter();

    DynamicView() {
    }

    DynamicView(SoftwareSystem softwareSystem, String description) {
        super(softwareSystem, description);
    }

    public void add(Element source, Element destination) {
        add(source, "", destination);
    }

    public void add(Element source, String description, Element destination) {
        // check that the relationship is in the model before adding it
        if (source != null && destination != null) {
            Relationship relationship = source.getEfferentRelationshipWith(destination);
            if (relationship != null) {
                addElement(source, false);
                addElement(destination, false);
                addRelationship(relationship, description, counter.toString());
                counter.increment();
            }
        }
    }

    public void startChildSequence() {
        this.counter = new HierarchicalSequenceCounter(counter);
    }

    public void endChildSequence() {
        this.counter = counter.getParent();
    }

    @Override
    public ViewType getType() {
        return ViewType.Dynamic;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Dynamic - " + getDescription();
    }

}