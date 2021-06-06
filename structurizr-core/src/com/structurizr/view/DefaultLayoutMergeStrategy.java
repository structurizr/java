package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of a LayoutMergeStrategy that:
 *
 * - Sets the paper size (if not set).
 * - Copies element x,y positions.
 * - Copies relationship vertices.
 *
 * Elements are matched using the following properties, in order:
 * - the element's full canonical name
 * - the element's name
 * - the element's description
 */
public class DefaultLayoutMergeStrategy implements LayoutMergeStrategy {

    private static final Log log = LogFactory.getLog(View.class);

    /**
     * Attempts to copy the visual layout information (e.g. x,y coordinates) of elements and relationships
     * from the specified source view into the specified destination view.
     *
     * @param viewWithLayoutInformation         the source view (e.g. the version stored by the Structurizr service)
     * @param viewWithoutLayoutInformation      the destination View (e.g. the new version, created locally with code)
     */
    public void copyLayoutInformation(@Nonnull View viewWithLayoutInformation, @Nonnull View viewWithoutLayoutInformation) {
        setPaperSizeIfNotSpecified(viewWithLayoutInformation, viewWithoutLayoutInformation);
        setDimensionsIfNotSpecified(viewWithLayoutInformation, viewWithoutLayoutInformation);

        Map<ElementView, ElementView> elementViewMap = new HashMap<>();
        Map<Element, Element> elementMap = new HashMap<>();

        for (ElementView elementViewWithoutLayoutInformation : viewWithoutLayoutInformation.getElements()) {
            ElementView elementViewWithLayoutInformation = findElementView(viewWithLayoutInformation, elementViewWithoutLayoutInformation.getElement());
            if (elementViewWithLayoutInformation != null) {
                elementViewMap.put(elementViewWithoutLayoutInformation, elementViewWithLayoutInformation);
                elementMap.put(elementViewWithoutLayoutInformation.getElement(), elementViewWithLayoutInformation.getElement());
            } else {
                log.warn("There is no layout information for the element named " + elementViewWithoutLayoutInformation.getElement().getName() + " on view " + viewWithLayoutInformation.getKey());
            }
        }

        for (ElementView elementViewWithoutLayoutInformation : elementViewMap.keySet()) {
            ElementView elementViewWithLayoutInformation = elementViewMap.get(elementViewWithoutLayoutInformation);
            elementViewWithoutLayoutInformation.copyLayoutInformationFrom(elementViewWithLayoutInformation);
        }

        for (RelationshipView relationshipViewWithoutLayoutInformation : viewWithoutLayoutInformation.getRelationships()) {
            RelationshipView relationshipViewWithLayoutInformation;
            if (viewWithoutLayoutInformation instanceof DynamicView) {
                relationshipViewWithLayoutInformation = findRelationshipView(viewWithLayoutInformation, relationshipViewWithoutLayoutInformation, elementMap);
            } else {
                relationshipViewWithLayoutInformation = findRelationshipView(viewWithLayoutInformation, relationshipViewWithoutLayoutInformation.getRelationship(), elementMap);
            }

            if (relationshipViewWithLayoutInformation != null) {
                relationshipViewWithoutLayoutInformation.copyLayoutInformationFrom(relationshipViewWithLayoutInformation);
            }
        }
    }

    private void setPaperSizeIfNotSpecified(@Nonnull View remoteView, @Nonnull View localView) {
        if (localView.getPaperSize() == null) {
            localView.setPaperSize(remoteView.getPaperSize());
        }
    }

    private void setDimensionsIfNotSpecified(@Nonnull View remoteView, @Nonnull View localView) {
        if (localView.getDimensions() == null) {
            localView.setDimensions(remoteView.getDimensions());
        }
    }

    /**
     * Finds an element. Override this to change the behaviour.
     *
     * @param viewWithLayoutInformation             the view to search
     * @param elementWithoutLayoutInformation       the Element to find
     * @return  an ElementView
     */
    protected ElementView findElementView(View viewWithLayoutInformation, Element elementWithoutLayoutInformation) {
        // see if we can find an element with the same canonical name in the source view
        ElementView elementView = viewWithLayoutInformation.getElements().stream().filter(ev -> ev.getElement().getCanonicalName().equals(elementWithoutLayoutInformation.getCanonicalName())).findFirst().orElse(null);

        if (elementView == null) {
            // no element was found, so try finding an element of the same type with the same name (in this situation, the parent element may have been renamed)
            elementView = viewWithLayoutInformation.getElements().stream().filter(ev -> ev.getElement().getName().equals(elementWithoutLayoutInformation.getName()) && ev.getElement().getClass().equals(elementWithoutLayoutInformation.getClass())).findFirst().orElse(null);
        }

        if (elementView == null) {
            // no element was found, so try finding an element of the same type with the same description if set (in this situation, the element itself may have been renamed)
            if (!StringUtils.isNullOrEmpty(elementWithoutLayoutInformation.getDescription())) {
                elementView = viewWithLayoutInformation.getElements().stream().filter(ev -> elementWithoutLayoutInformation.getDescription().equals(ev.getElement().getDescription()) && ev.getElement().getClass().equals(elementWithoutLayoutInformation.getClass())).findFirst().orElse(null);
            }
        }

        if (elementView == null) {
            // no element was found, so try finding an element of the same type with the same ID (in this situation, the name and description may have changed)
            elementView = viewWithLayoutInformation.getElements().stream().filter(ev -> ev.getElement().getId().equals(elementWithoutLayoutInformation.getId()) && ev.getElement().getClass().equals(elementWithoutLayoutInformation.getClass())).findFirst().orElse(null);
        }

        return elementView;
    }

    private RelationshipView findRelationshipView(View viewWithLayoutInformation, Relationship relationshipWithoutLayoutInformation, Map<Element,Element> elementMap) {
        if (!elementMap.containsKey(relationshipWithoutLayoutInformation.getSource()) || !elementMap.containsKey(relationshipWithoutLayoutInformation.getDestination())) {
            return null;
        }

        Element sourceElementWithLayoutInformation = elementMap.get(relationshipWithoutLayoutInformation.getSource());
        Element destinationElementWithLayoutInformation = elementMap.get(relationshipWithoutLayoutInformation.getDestination());

        for (RelationshipView rv : viewWithLayoutInformation.getRelationships()) {
            if (
                rv.getRelationship().getSource().equals(sourceElementWithLayoutInformation) &&
                rv.getRelationship().getDestination().equals(destinationElementWithLayoutInformation) &&
                rv.getRelationship().getDescription().equals(relationshipWithoutLayoutInformation.getDescription())
            ) {
                return rv;
            }
        }

        return null;
    }

    private RelationshipView findRelationshipView(View view, RelationshipView relationshipWithoutLayoutInformation, Map<Element,Element> elementMap) {
        if (!elementMap.containsKey(relationshipWithoutLayoutInformation.getRelationship().getSource()) || !elementMap.containsKey(relationshipWithoutLayoutInformation.getRelationship().getDestination())) {
            return null;
        }

        Element sourceElementWithLayoutInformation = elementMap.get(relationshipWithoutLayoutInformation.getRelationship().getSource());
        Element destinationElementWithLayoutInformation = elementMap.get(relationshipWithoutLayoutInformation.getRelationship().getDestination());

        for (RelationshipView rv : view.getRelationships()) {
            if (
                rv.getRelationship().getSource().equals(sourceElementWithLayoutInformation) &&
                rv.getRelationship().getDestination().equals(destinationElementWithLayoutInformation) &&
                rv.getDescription().equals(relationshipWithoutLayoutInformation.getDescription()) &&
                rv.getOrder().equals(relationshipWithoutLayoutInformation.getOrder())) {
                    return rv;
            }
        }

        return null;
    }

}