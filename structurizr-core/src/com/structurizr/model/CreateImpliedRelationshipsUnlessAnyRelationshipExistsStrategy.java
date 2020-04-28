package com.structurizr.model;

/**
 * This strategy creates implied relationships between all valid combinations of the parent elements,
 * unless any relationship already exists between them.
 */
public class CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy extends AbstractImpliedRelationshipsStrategy {

    @Override
    public void createImpliedRelationships(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        Model model = source.getModel();

        while (source != null) {
            while (destination != null) {
                if (impliedRelationshipIsAllowed(source, destination)) {
                    boolean createRelationship = !source.hasEfferentRelationshipWith(destination);

                    if (createRelationship) {
                        model.addRelationship(source, destination, relationship.getDescription(), relationship.getTechnology(), relationship.getInteractionStyle(), false);
                    }
                }

                destination = destination.getParent();
            }

            destination = relationship.getDestination();
            source = source.getParent();
        }
    }

}