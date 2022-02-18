package com.structurizr.model;

/**
 * This strategy creates implied relationships between all valid combinations of the parent elements,
 * unless the same relationship already exists between them.
 */
public class CreateImpliedRelationshipsUnlessSameRelationshipExistsStrategy extends AbstractImpliedRelationshipsStrategy {

    @Override
    public void createImpliedRelationships(Relationship relationship) {
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        Model model = source.getModel();

        while (source != null) {
            while (destination != null) {
                if (impliedRelationshipIsAllowed(source, destination)) {
                    boolean createRelationship = !source.hasEfferentRelationshipWith(destination, relationship.getDescription());

                    if (createRelationship) {
                        Relationship impliedRelationship = model.addRelationship(source, destination, relationship.getDescription(), relationship.getTechnology(), false);
                        if (impliedRelationship != null) {
                            impliedRelationship.setLinkedRelationshipId(relationship.getId());
                        }
                    }
                }

                destination = destination.getParent();
            }

            destination = relationship.getDestination();
            source = source.getParent();
        }
    }

}