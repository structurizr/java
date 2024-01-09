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

    /**
     * Creates an implied relationship based upon the specified relationship, between the specified source and destination elements.
     *
     * @param relationship      the Relationship on which the implied relationship is based
     * @param source            the implied relationship source
     * @param destination       the implied relationship destination
     * @return                  a Relationship object representing the implied relationship, or null if one wasn't created
     */
    protected Relationship createImpliedRelationship(Relationship relationship, Element source, Element destination) {
        Model model = relationship.getModel();
        Relationship impliedRelationship = model.addRelationship(source, destination, relationship.getDescription(), relationship.getTechnology(), false);
        if (impliedRelationship != null) {
            impliedRelationship.setLinkedRelationshipId(relationship.getId());
        }

        return impliedRelationship;
    }

}