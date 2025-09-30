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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyBasedSeverityStrategy implements SeverityStrategy {

    private static final String STRUCTURIZR_INSPECTION_PREFIX = "structurizr.inspection.";

    public PropertyBasedSeverityStrategy() {
    }

    @Override
    public Severity getSeverity(Inspection inspection, Workspace workspace) {
        Severity severity = getSeverityFromProperties(inspection.getType(), workspace);
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, ViewSet viewSet) {
        Severity severity = getSeverityFromProperties(inspection.getType(), inspection.getWorkspace(), viewSet.getConfiguration());
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, View view) {
        Severity severity = getSeverityFromProperties(inspection.getType(), inspection.getWorkspace(), inspection.getWorkspace().getViews().getConfiguration(), view);
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, ElementStyle elementStyle) {
        Severity severity = getSeverityFromProperties(inspection.getType(), inspection.getWorkspace(), inspection.getWorkspace().getViews().getConfiguration(), elementStyle);
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Model model) {
        Severity severity = getSeverityFromProperties(inspection.getType(), inspection.getWorkspace(), model);
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Element element) {
        Element parentElement = element.getParent();
        Element grandParentElement = null;
        if (parentElement != null) {
            grandParentElement = parentElement.getParent();
        }

        Severity severity = getSeverityFromProperties(inspection.getType(), inspection.getWorkspace(), inspection.getWorkspace().getModel(), grandParentElement, parentElement, element);
        return severity != null ? severity : Severity.ERROR;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Relationship relationship) {
        Element source = relationship.getSource();
        Relationship linkedRelationship = null;
        if (!StringUtils.isNullOrEmpty(relationship.getLinkedRelationshipId())) {
            linkedRelationship = inspection.getWorkspace().getModel().getRelationship(relationship.getLinkedRelationshipId());
        }

        String allRelationshipsType = inspection.getType();
        String specificRelationshipType = inspection.getType();

        // convert model.relationship.description to model.relationship[sourceType->destinationType].description
        String sourceType = relationship.getSource().getClass().getSimpleName().toLowerCase();
        String destinationType = relationship.getDestination().getClass().getSimpleName().toLowerCase();
        specificRelationshipType = allRelationshipsType.replaceFirst(
                "\\.relationship\\.",
                String.format(".relationship[%s->%s].", sourceType, destinationType)
        );

        Severity severity = getSeverityFromProperties(specificRelationshipType, inspection.getWorkspace(), inspection.getWorkspace().getModel(), source.getParent(), source, linkedRelationship, relationship);
        if (severity == null) {
            severity = getSeverityFromProperties(allRelationshipsType, inspection.getWorkspace(), inspection.getWorkspace().getModel(), source.getParent(), source, linkedRelationship, relationship);
        }

        return severity != null ? severity : Severity.ERROR;
    }

    protected Severity getSeverityFromProperties(String type, PropertyHolder... propertyHolders) {
        List<String> types = generatePropertyNames(type);
        List<PropertyHolder> reversedPropertyHolders = Arrays.asList(propertyHolders);
        Collections.reverse(reversedPropertyHolders);

        for (PropertyHolder propertyHolder : reversedPropertyHolders) {
            if (propertyHolder != null) {
                for (String t : types) {
                    if (propertyHolder.getProperties().containsKey(t)) {
                        return Severity.valueOf(propertyHolder.getProperties().get(t).toUpperCase());
                    }
                }
            }
        }

        return null;
    }

    protected List<String> generatePropertyNames(String type) {
        // example input:
        // model.component.description
        //
        // example output:
        // structurizr.inspection.model.component.description
        // structurizr.inspection.model.component.*
        // structurizr.inspection.model.*
        // structurizr.inspection.*

        // example input:
        // model.relationship[component->component].technology
        //
        // example output:
        // structurizr.inspection.model.relationship[component->component].technology
        // structurizr.inspection.model.relationship[component->component].*

        type = type.toLowerCase();
        List<String> types = new ArrayList<>();

        String[] parts = type.split("\\.");
        String buf = STRUCTURIZR_INSPECTION_PREFIX;
        types.add(buf + "*");
        for (int i = 0; i < parts.length-1; i++) {
            buf = buf + parts[i] + ".";
            types.add(buf + "*");
        }

        types.add(STRUCTURIZR_INSPECTION_PREFIX + type);
        Collections.reverse(types);

        if (type.contains(".relationship[")) {
            return types.subList(0, types.size()-2);
        }

        return types;
    }

}
