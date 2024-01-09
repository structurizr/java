package com.structurizr.assistant.model;

import com.structurizr.Workspace;
import com.structurizr.assistant.Inspection;
import com.structurizr.assistant.Recommendation;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public abstract class RelationshipInspection extends Inspection {

    public RelationshipInspection(Workspace workspace) {
        super(workspace);
    }

    public final Recommendation run(Relationship relationship) {
        Element source = relationship.getSource();
        Relationship linkedRelationship = null;
        if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
            getWorkspace().getModel().getRelationship(relationship.getLinkedRelationshipId());
        }

        if (isEnabled(getType(), getWorkspace(), getWorkspace().getModel(), source.getParent(), source, linkedRelationship, relationship)) {
            return inspect(relationship);
        }

        return noRecommendation();
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Recommendation inspect(Relationship relationship);

}