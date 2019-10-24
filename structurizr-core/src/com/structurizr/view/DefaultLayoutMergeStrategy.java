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

        for (RelationshipView sourceRelationshipView : sourceView.getRelationships()) {
            RelationshipView destinationRelationshipView;
            if (destinationView instanceof DynamicView) {
                destinationRelationshipView = findRelationshipView((DynamicView)destinationView, sourceRelationshipView);
            } else {
                destinationRelationshipView = findRelationshipView(destinationView, sourceRelationshipView.getRelationship());
            }

            if (destinationRelationshipView != null) {
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
            if (rv.getRelationship().equals(relationship)) {
                return rv;
            }
        }

        return null;
    }

    protected RelationshipView findRelationshipView(DynamicView view, RelationshipView sourceRelationshipView) {
        for (RelationshipView relationshipView : view.getRelationships()) {
            if (relationshipView.getRelationship().equals(sourceRelationshipView.getRelationship())) {
                if ((relationshipView.getDescription() != null && relationshipView.getDescription().equals(sourceRelationshipView.getDescription())) &&
                        relationshipView.getOrder().equals(sourceRelationshipView.getOrder())) {
                    return relationshipView;
                }
            }
        }

        return null;
    }

}