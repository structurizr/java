package com.structurizr.view;

import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;

/**
 * A default implementation of a LayoutMergeStrategy that:
 *
 * - Sets the paper size (if not set).
 * - Copies element x,y positions.
 * - Copies relationship vertices.
 *
 * Elements are matched by the full canonical name. The downside of this approach is that if an element is renamed
 * between versions of a workspace, it won't be possible to find/copy the layout information associated with an element.
 */
public class DefaultLayoutMergeStrategy implements LayoutMergeStrategy {

    private static final Log log = LogFactory.getLog(View.class);

    /**
     * Attempts to copy the visual layout information (e.g. x,y coordinates) of elements and relationships
     * from the specified source view into the specified destination view.
     *
     * @param sourceView        the source view (e.g. the version stored by the Structurizr service)
     * @param destinationView   the destination View (e.g. the new version, created locally with code)
     */
    public void copyLayoutInformation(@Nonnull View sourceView, @Nonnull View destinationView) {
        setPaperSizeIfNotSpecified(sourceView, destinationView);

        for (ElementView destinationElementView : destinationView.getElements()) {
            ElementView sourceElementView = findElementView(sourceView, destinationElementView.getElement());
            if (sourceElementView != null) {
                destinationElementView.copyLayoutInformationFrom(sourceElementView);
            } else {
                log.warn("There is no layout information for the element named " + destinationElementView.getElement().getName() + " on view " + sourceView.getKey());
            }
        }

        for (RelationshipView destinationRelationshipView : destinationView.getRelationships()) {
            RelationshipView sourceRelationshipView;
            if (destinationView instanceof DynamicView) {
                sourceRelationshipView = findRelationshipView(sourceView, destinationRelationshipView);
            } else {
                sourceRelationshipView = findRelationshipView(sourceView, destinationRelationshipView.getRelationship());
            }

            if (sourceRelationshipView != null) {
                destinationRelationshipView.copyLayoutInformationFrom(sourceRelationshipView);
            }
        }
    }

    private void setPaperSizeIfNotSpecified(@Nonnull View remoteView, @Nonnull View localView) {
        if (localView.getPaperSize() == null) {
            localView.setPaperSize(remoteView.getPaperSize());
        }
    }

    /**
     * Finds an element by canonical name. Override this to change the behaviour.

     * @param view          the view to search
     * @param element       the Element to find
     * @return  an ElementView
     */
    protected ElementView findElementView(View view, Element element) {
        return view.getElements().stream().filter(ev -> ev.getElement().getCanonicalName().equals(element.getCanonicalName())).findFirst().orElse(null);
    }

    protected RelationshipView findRelationshipView(View view, Relationship relationship) {
        for (RelationshipView rv : view.getRelationships()) {
            if (
                rv.getRelationship().getSource().getCanonicalName().equals(relationship.getSource().getCanonicalName()) &&
                rv.getRelationship().getDestination().getCanonicalName().equals(relationship.getDestination().getCanonicalName()) &&
                rv.getRelationship().getDescription().equals(relationship.getDescription())
            ) {
                return rv;
            }
        }

        return null;
    }

    protected RelationshipView findRelationshipView(View view, RelationshipView relationshipView) {
        for (RelationshipView rv : view.getRelationships()) {
            if (
                rv.getRelationship().getSource().getCanonicalName().equals(relationshipView.getRelationship().getSource().getCanonicalName()) &&
                rv.getRelationship().getDestination().getCanonicalName().equals(relationshipView.getRelationship().getDestination().getCanonicalName()) &&
                rv.getDescription().equals(relationshipView.getDescription()) &&
                rv.getOrder().equals(relationshipView.getOrder())) {
                    return rv;
            }
        }

        return null;
    }

}