package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;

import java.util.HashSet;
import java.util.Set;

public class OrphanedElementInspection extends ElementInspection {

    private final Set<String> elementsWithRelationships = new HashSet<>();

    public OrphanedElementInspection(Workspace workspace) {
        super(workspace);

        for (Relationship relationship : workspace.getModel().getRelationships()) {
            elementsWithRelationships.add(relationship.getSourceId());
            elementsWithRelationships.add(relationship.getDestinationId());
        }
    }

    @Override
    protected Recommendation inspect(Element element) {
        if (element instanceof DeploymentNode) {
            // deployment nodes typically won't have relationships to/from them
            return noRecommendation();
        }

        if (!elementsWithRelationships.contains(element.getId())) {
            return mediumPriorityRecommendation("The " + terminologyFor(element).toLowerCase() + " named \"" + element.getName() + "\" is orphaned - add a relationship to/from it, or consider removing it from the model.");
        }

        return noRecommendation();
    }

    @Override
    protected String getType() {
        return "model.element.orphaned";
    }

}