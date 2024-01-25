package com.structurizr.inspection;

import com.structurizr.PropertyHolder;
import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.View;
import com.structurizr.view.ViewSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PropertyBasedSeverityStrategy implements SeverityStrategy {

    private static final String STRUCTURIZR_INSPECTION_PREFIX = "structurizr.inspection.";

    public PropertyBasedSeverityStrategy() {
    }

    @Override
    public Severity getSeverity(Inspection inspection, Workspace workspace) {
        return getSeverityFromProperties(inspection, workspace);
    }

    @Override
    public Severity getSeverity(Inspection inspection, ViewSet viewSet) {
        return getSeverityFromProperties(inspection, inspection.getWorkspace(), viewSet.getConfiguration());
    }

    @Override
    public Severity getSeverity(Inspection inspection, View view) {
        return getSeverityFromProperties(inspection, inspection.getWorkspace(), inspection.getWorkspace().getViews().getConfiguration(), view);
    }

    @Override
    public Severity getSeverity(Inspection inspection, ElementStyle elementStyle) {
        return getSeverityFromProperties(inspection, inspection.getWorkspace(), inspection.getWorkspace().getViews().getConfiguration(), elementStyle);
    }

    @Override
    public Severity getSeverity(Inspection inspection, Model model) {
        return getSeverityFromProperties(inspection, inspection.getWorkspace(), model);
    }

    @Override
    public Severity getSeverity(Inspection inspection, Element element) {
        Element parentElement = element.getParent();
        Element grandParentElement = null;
        if (parentElement != null) {
            grandParentElement = parentElement.getParent();
        }

        return getSeverityFromProperties(inspection, inspection.getWorkspace(), inspection.getWorkspace().getModel(), grandParentElement, parentElement, element);
    }

    @Override
    public Severity getSeverity(Inspection inspection, Relationship relationship) {
        Element source = relationship.getSource();
        Relationship linkedRelationship = null;
        if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
            inspection.getWorkspace().getModel().getRelationship(relationship.getLinkedRelationshipId());
        }

        return getSeverityFromProperties(inspection, inspection.getWorkspace(), inspection.getWorkspace().getModel(), source.getParent(), source, linkedRelationship, relationship);
    }

    protected Severity getSeverityFromProperties(Inspection inspection, PropertyHolder... propertyHolders) {
        List<String> types = generatePropertyNames(inspection.getType());

        for (String type : types) {
            for (PropertyHolder propertyHolder : propertyHolders) {
                if (propertyHolder != null) {
                    if (propertyHolder.getProperties().containsKey(type)) {
                        return Severity.valueOf(propertyHolder.getProperties().get(type).toUpperCase());
                    }
                }
            }
        }

        return Severity.ERROR;
    }

    protected List<String> generatePropertyNames(String inspection) {
        // example input:
        // structurizr.inspection.model.component.description
        //
        // example output:
        // structurizr.inspection.model.component.description
        // structurizr.inspection.model.component.*
        // structurizr.inspection.model.*
        // structurizr.inspection.*

        List<String> types = new ArrayList<>();

        String[] parts = inspection.split("\\.");
        String buf = STRUCTURIZR_INSPECTION_PREFIX;
        types.add(buf + "*");
        for (int i = 0; i < parts.length-1; i++) {
            buf = buf + parts[i] + ".";
            types.add(buf + "*");
        }

        types.add(STRUCTURIZR_INSPECTION_PREFIX + inspection);
        Collections.reverse(types);

        return types;
    }

}
