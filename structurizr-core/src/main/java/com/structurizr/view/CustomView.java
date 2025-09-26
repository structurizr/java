package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.structurizr.model.CustomElement;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a custom view, containing custom elements.
 */
public final class CustomView extends ModelView implements AnimatedView {

    @Nonnull
    private List<Animation> animations = new ArrayList<>();

    private Model model;

    CustomView() {
    }

    CustomView(Model model, String key, String title, String description) {
        super(null, key, description);

        setTitle(title);
        this.model = model;
    }

    /**
     * Gets the (computed) name of this view.
     *
     * @return  the name, as a String
     */
    @Override
    public String getName() {
        return "Custom View: " + getTitle();
    }

    /**
     * Gets the model that this view belongs to.
     *
     * @return  a Model object
     */
    @JsonIgnore
    @Override
    public Model getModel() {
        return this.model;
    }

    void setModel(Model model) {
        this.model = model;
    }

    @Override
    protected void checkElementCanBeAdded(Element element) {
        if (element instanceof CustomElement) {
            // all good
        } else {
            throw new ElementNotPermittedInViewException("Only custom elements can be added to a custom view.");
        }
    }

    @Override
    protected boolean canBeRemoved(Element element) {
        return true;
    }

    /**
     * Adds a specific relationship to this view.
     *
     * @param relationship  the Relationship to be added
     * @return  a RelationshipView object representing the relationship added
     */
    public RelationshipView add(@Nonnull Relationship relationship) {
        return addRelationship(relationship);
    }

    /**
     * Adds an animation step, with the specified elements.
     *
     * @param elements      the elements that should be shown in the animation step
     */
    public void addAnimation(CustomElement... elements) {
        if (elements == null || elements.length == 0) {
            throw new IllegalArgumentException("One or more elements must be specified.");
        }

        Set<String> elementIdsInPreviousAnimationSteps = new HashSet<>();

        for (Animation animationStep : animations) {
            elementIdsInPreviousAnimationSteps.addAll(animationStep.getElements());
        }

        Set<Element> elementsInThisAnimationStep = new HashSet<>();
        Set<Relationship> relationshipsInThisAnimationStep = new HashSet<>();

        for (CustomElement element : elements) {
            if (isElementInView(element)) {
                if (!elementIdsInPreviousAnimationSteps.contains(element.getId())) {
                    elementIdsInPreviousAnimationSteps.add(element.getId());
                    elementsInThisAnimationStep.add(element);
                }
            }
        }

        if (elementsInThisAnimationStep.isEmpty()) {
            throw new IllegalArgumentException("None of the specified elements exist in this view.");
        }

        for (RelationshipView relationshipView : this.getRelationships()) {
            if (
                    (elementsInThisAnimationStep.contains(relationshipView.getRelationship().getSource()) && elementIdsInPreviousAnimationSteps.contains(relationshipView.getRelationship().getDestination().getId())) ||
                            (elementIdsInPreviousAnimationSteps.contains(relationshipView.getRelationship().getSource().getId()) && elementsInThisAnimationStep.contains(relationshipView.getRelationship().getDestination()))
            ) {
                relationshipsInThisAnimationStep.add(relationshipView.getRelationship());
            }
        }

        animations.add(new Animation(animations.size() + 1, elementsInThisAnimationStep, relationshipsInThisAnimationStep));
    }

    @Nonnull
    @Override
    public List<Animation> getAnimations() {
        return new ArrayList<>(animations);
    }

    void setAnimations(@Nullable List<Animation> animations) {
        if (animations != null) {
            this.animations = new ArrayList<>(animations);
        } else {
            this.animations = new ArrayList<>();
        }
    }

    /**
     * Adds the given custom element to this view, including relationships to/from that custom element.
     *
     * @param customElement the CustomElement to add
     */
    public void add(@Nonnull CustomElement customElement) {
        add(customElement, true);
    }

    /**
     * Adds the given custom element to this view.
     *
     * @param customElement the CustomElement to add
     * @param addRelationships  whether to add relationships to/from the custom element
     */
    public void add(@Nonnull CustomElement customElement, boolean addRelationships) {
        addElement(customElement, addRelationships);
    }

    /**
     * Removes the given custom element from this view.
     *
     * @param customElement the CustomElement to add
     */
    public void remove(@Nonnull CustomElement customElement) {
        removeElement(customElement);
    }

    /**
     * Adds the default set of elements to this view.
     */
    public void addDefaultElements() {
        addAllCustomElements();
    }

    /**
     * Adds all custom elements to this view.
     */
    public void addAllCustomElements() {
        getModel().getCustomElements().forEach(ce -> {
            try {
                add(ce);
            } catch (ElementNotPermittedInViewException e) {
                // ignore
            }
        });
    }

}
