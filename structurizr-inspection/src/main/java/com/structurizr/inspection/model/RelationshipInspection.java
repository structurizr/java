package com.structurizr.inspection.model;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspection;
import com.structurizr.inspection.Severity;
import com.structurizr.inspection.Violation;
import com.structurizr.model.Element;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;

public abstract class RelationshipInspection extends Inspection {

    public RelationshipInspection(Workspace workspace) {
        super(workspace);
    }

    public final Violation run(Relationship relationship) {
        Element source = relationship.getSource();
        Relationship linkedRelationship = null;
        if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
            getWorkspace().getModel().getRelationship(relationship.getLinkedRelationshipId());
        }

        Severity severity = getSeverity(getWorkspace(), getWorkspace().getModel(), source.getParent(), source, linkedRelationship, relationship);
        Violation violation = inspect(relationship);

        return violation == null ? null : violation.withSeverity(severity);
    }

    protected String terminologyFor(Element element) {
        return getWorkspace().getViews().getConfiguration().getTerminology().findTerminology(element).toLowerCase();
    }

    protected abstract Violation inspect(Relationship relationship);

}