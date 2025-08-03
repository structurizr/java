package com.structurizr.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * The superclass for all views that show elements/relationships from the model, namely:
 *
 * - Custom views
 * - System landscape views
 * - System context views
 * - Container views
 * - Dynamic views
 * - Deployment views
 */
public abstract class ModelView extends View {

    private static final int DEFAULT_RANK_SEPARATION = 300;
    private static final int DEFAULT_NODE_SEPARATION = 300;

    private SoftwareSystem softwareSystem;
    private String softwareSystemId;
    private PaperSize paperSize = null;
    private Dimensions dimensions = null;
    private AutomaticLayout automaticLayout = null;
    private boolean mergeFromRemote = true;

    private Set<ElementView> elementViews = new TreeSet<>();
    private Set<RelationshipView> relationshipViews = new TreeSet<>();

    private LayoutMergeStrategy layoutMergeStrategy = new DefaultLayoutMergeStrategy();

    private ViewSet viewSet;

    ModelView() {
    }

    ModelView(SoftwareSystem softwareSystem, String key, String description) {
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

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Gets the automatic layout settings for this view.
     *
     * @return  an AutomaticLayout object, or null if not enabled
     */
    public AutomaticLayout getAutomaticLayout() {
        return automaticLayout;
    }

    @JsonSetter
    void setAutomaticLayout(AutomaticLayout automaticLayout) {
        this.automaticLayout = automaticLayout;
    }

    /**
     * Enables automatic layout for this view, with some default settings.
     */
    public void enableAutomaticLayout() {
        enableAutomaticLayout(AutomaticLayout.RankDirection.TopBottom, 300, 600, 200, false);
    }

    /**
     * Enables automatic layout for this view, with the specified settings, using the Dagre implementation.
     *
     * @param rankDirection     the rank direction
     * @param rankSeparation    the separation between ranks (in pixels, a positive integer)
     * @param nodeSeparation    the separation between nodes within the same rank (in pixels, a positive integer)
     * @param edgeSeparation    the separation between edges (in pixels, a positive integer)
     * @param vertices          whether vertices should be created during automatic layout
     */
    public void enableAutomaticLayout(AutomaticLayout.RankDirection rankDirection, int rankSeparation, int nodeSeparation, int edgeSeparation, boolean vertices) {
        this.automaticLayout = new AutomaticLayout(AutomaticLayout.Implementation.Dagre, rankDirection, rankSeparation, nodeSeparation, edgeSeparation, vertices);
    }

    /**
     * Enables automatic layout for this view, with the specified direction, using the Graphviz implementation.
     *
     * @param rankDirection     the rank direction
     */
    public void enableAutomaticLayout(AutomaticLayout.RankDirection rankDirection) {
        enableAutomaticLayout(rankDirection, DEFAULT_RANK_SEPARATION, DEFAULT_NODE_SEPARATION);
    }

    /**
     * Enables automatic layout for this view, with the specified settings, using the Graphviz implementation.
     *
     * @param rankDirection     the rank direction
     * @param rankSeparation    the separation between ranks (in pixels, a positive integer)
     * @param nodeSeparation    the separation between nodes within the same rank (in pixels, a positive integer)
     */
    public void enableAutomaticLayout(AutomaticLayout.RankDirection rankDirection, int rankSeparation, int nodeSeparation) {
        this.automaticLayout = new AutomaticLayout(AutomaticLayout.Implementation.Graphviz, rankDirection, rankSeparation, nodeSeparation, 0, false);
    }

    /**
     * Disables automatic layout for this view.
     */
    public void disableAutomaticLayout() {
        this.automaticLayout = null;
    }

    /**
     * Gets whether layout information for this view should be merged from a remote version of the workspace.
     *
     * @return  true if layout information should be merged from the remote workspace, false otherwise
     */
    public boolean getMergeFromRemote() {
        return mergeFromRemote;
    }

    /**
     * Specifies whether layout information for this view should be merged from a remote version of the workspace.
     *
     * @param mergeFromRemote   true if layout information should be merged from the remote workspace, false otherwise
     */
    @JsonIgnore
    public void setMergeFromRemote(boolean mergeFromRemote) {
        this.mergeFromRemote = mergeFromRemote;
    }

    protected final void addElement(Element element, boolean addRelationships) {
        if (element == null) {
            throw new IllegalArgumentException("An element must be specified.");
        }

        if (getModel().contains(element)) {
            checkElementCanBeAdded(element);
            elementViews.add(new ElementView(element));

            if (addRelationships) {
                addRelationships(element);
            }
        } else {
            throw new IllegalArgumentException("The element named " + element.getName() + " does not exist in the model associated with this view.");
        }
    }

    protected abstract void checkElementCanBeAdded(Element element);

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

    /**
     * Determines whether the specified element exists in this view.
     *
     * @param element       the Element to look for
     * @return      true if the element exists in the view, false otherwise
     */
    public boolean isElementInView(Element element) {
        return this.elementViews.stream().anyMatch(ev -> ev.getElement().equals(element));
    }

    /**
     * Determines whether the specified relationship exists in this view.
     *
     * @param relationship      the Relationship to look for
     * @return      true if the relationship exists in the view, false otherwise
     */
    public boolean isRelationshipInView(Relationship relationship) {
        return this.relationshipViews.stream().anyMatch(rv -> rv.getRelationship().equals(relationship));
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
     * Removes relationships that are not connected to the specified elements.
     *
     * @param elements      the Set of Element objects to test against
     */
    public void removeRelationshipsNotConnectedToElements(Set<? extends Element> elements) {
        if (elements != null) {
            getRelationships().stream()
                    .map(RelationshipView::getRelationship)
                    .filter(r -> !elements.contains(r.getSource()) && !elements.contains(r.getDestination()))
                    .forEach(this::remove);
        }
    }

    /**
     * Gets the set of elements in this view.
     *
     * @return a Set of ElementView objects
     */
    public Set<ElementView> getElements() {
        return new TreeSet<>(elementViews);
    }

    void setElements(Set<ElementView> elementViews) {
        if (elementViews != null) {
            this.elementViews = new TreeSet<>(elementViews);
        }
    }

    /**
     * Gets the set of relationships in this view.
     *
     * @return a Set of RelationshipView objects
     */
    public Set<RelationshipView> getRelationships() {
        return new TreeSet<>(this.relationshipViews);
    }

    void setRelationships(Set<RelationshipView> relationshipViews) {
        if (relationshipViews != null) {
            this.relationshipViews = new TreeSet<>(relationshipViews);
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
     * Sets the strategy used for merging layout information (paper size, x/y positioning, etc)
     * from one version of this view to another.
     *
     * @param layoutMergeStrategy       an instance of LayoutMergeStrategy
     */
    public void setLayoutMergeStrategy(LayoutMergeStrategy layoutMergeStrategy) {
        if (layoutMergeStrategy == null) {
            throw new IllegalArgumentException("A LayoutMergeStrategy object must be provided.");
        }

        this.layoutMergeStrategy = layoutMergeStrategy;
    }

    /**
     * Attempts to copy the visual layout information (e.g. x,y coordinates) of elements and relationships
     * from the specified source view into this view.
     *
     * @param source    the source View
     */
    public void copyLayoutInformationFrom(@Nonnull ModelView source) {
        layoutMergeStrategy.copyLayoutInformation(source, this);
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

    /**
     * Gets the relationship view for the given relationship.
     *
     * @param relationship  the Relationship to find the RelationshipView for
     * @return  a RelationshipView object, or null if the relationship doesn't exist in the view
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

    final void checkParentAndChildrenHaveNotAlreadyBeenAdded(StaticStructureElement elementToBeAdded) {
        // check a parent hasn't been added already
        Set<String> idsOfElementsInView = getElements().stream().map(ElementView::getElement).map(Element::getId).collect(Collectors.toSet());

        Element parent = elementToBeAdded.getParent();
        while (parent != null) {
            if (idsOfElementsInView.contains(parent.getId())) {
                throw new ElementNotPermittedInViewException("A parent of " + elementToBeAdded.getName() + " is already in this view.");
            }

            parent = parent.getParent();
        }

        // and now check a child hasn't been added already
        Set<String> elementParentIds = new HashSet<>();
        for (ElementView elementView : getElements()) {
            Element element = elementView.getElement();
            parent = element.getParent();
            while (parent != null) {
                elementParentIds.add(parent.getId());
                parent = parent.getParent();
            }
        }

        if (elementParentIds.contains(elementToBeAdded.getId())) {
            throw new ElementNotPermittedInViewException("A child of " + elementToBeAdded.getName() + " is already in this view.");
        }
    }

    protected <T extends Element> void addNearestNeighbours(Element element, Class<T> typeOfElement) {
        if (element == null) {
            return;
        }

        try {
            addElement(element, true);

            Set<Relationship> relationships = getModel().getRelationships();
            relationships.stream().filter(r -> r.getSource().equals(element) && typeOfElement.isInstance(r.getDestination()))
                    .map(Relationship::getDestination)
                    .forEach(d -> {
                        try {
                            addElement(d, true);
                        } catch (ElementNotPermittedInViewException e) {
                            System.out.println(e.getMessage() + " (ignoring " + d.getName() + ")");
                        }
                    });

            relationships.stream().filter(r -> r.getDestination().equals(element) && typeOfElement.isInstance(r.getSource()))
                    .map(Relationship::getSource)
                    .forEach(s -> {
                        try {
                            addElement(s, true);
                        } catch (ElementNotPermittedInViewException e) {
                            System.out.println(e.getMessage() + " (ignoring " + s.getName() + ")");
                        }
                    });
        } catch (ElementNotPermittedInViewException e) {
            System.out.println(e.getMessage() + " (ignoring " + element.getName() + ")");
        }
    }

}