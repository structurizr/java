package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
            } else {
                throw new IllegalArgumentException("relationship does not exist in model");
            }
        } else {
            throw new IllegalArgumentException("source and description must not be null");
        }
    }

    @Override
    public RelationshipView add(Relationship relationship) {
        // when adding a relationship to a DynamicView we suppose the user really wants to also see both components
        addElement(relationship.getSource(), false);
        addElement(relationship.getDestination(), false);
        return super.add(relationship);
    }

    public void startChildSequence() {
        this.counter = new HierarchicalSequenceCounter(counter);
    }

    public void endChildSequence() {
        this.counter = counter.getParent();
    }

    @Override
    public Set<RelationshipView> getRelationships() {
        SortedSet<RelationshipView> set = new TreeSet<RelationshipView>((rv1, rv2) -> rv1.getOrder().compareTo(rv2.getOrder()));
        set.addAll(super.getRelationships());

        return set;
    }

    RelationshipView findRelationshipView(RelationshipView sourceRelationshipView) {
        for (RelationshipView relationshipView : getRelationships()) {
            if (relationshipView.getRelationship().equals(sourceRelationshipView.getRelationship())) {
                if ((relationshipView.getDescription() != null && relationshipView.getDescription().equals(sourceRelationshipView.getDescription())) &&
                        relationshipView.getOrder().equals(sourceRelationshipView.getOrder())) {
                    return relationshipView;
                }
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return getSoftwareSystem().getName() + " - Dynamic - " + getDescription();
    }

}