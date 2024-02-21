package com.structurizr.inspection.model;

import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.HashSet;
import java.util.Set;

public class DisconnectedElementInspection extends AbstractElementInspection {

    private final Set<String> elementsWithRelationships = new HashSet<>();

    public DisconnectedElementInspection(Inspector inspector) {
        super(inspector);

        for (Relationship relationship : getWorkspace().getModel().getRelationships()) {
            elementsWithRelationships.add(relationship.getSourceId());
            elementsWithRelationships.add(relationship.getDestinationId());
        }
    }

    @Override
    protected Violation inspect(Element element) {
        if (element instanceof DeploymentNode) {
            // deployment nodes typically won't have relationships to/from them
            return noViolation();
        }

        if (!elementsWithRelationships.contains(element.getId())) {
            return violation("The " + terminologyFor(element).toLowerCase() + " \"" + nameOf(element) + "\" is disconnected - add a relationship to/from it, or consider removing it from the model.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "model.element.disconnected";
    }

}