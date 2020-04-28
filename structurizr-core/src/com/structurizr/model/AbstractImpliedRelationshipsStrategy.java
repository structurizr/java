package com.structurizr.model;

/**
 * Abstract base class for supplied ImpliedRelationshipsStrategy implementations.
 */
public abstract class AbstractImpliedRelationshipsStrategy implements ImpliedRelationshipsStrategy {

    protected boolean impliedRelationshipIsAllowed(Element source, Element destination) {
        if (source.equals(destination)) {
            return false;
        }

        return !(isChildOf(source, destination) || isChildOf(destination, source));
    }

    private boolean isChildOf(Element e1, Element e2) {
        if (e1 instanceof Person || e2 instanceof Person) {
            return false;
        }

        Element parent = e2.getParent();
        while (parent != null) {
            if (parent.getId().equals(e1.getId())) {
                return true;
            }

            parent = parent.getParent();
        }

        return false;
    }

}