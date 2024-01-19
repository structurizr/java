package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.View;
import com.structurizr.view.ViewSet;

public class FixedSeverityStrategy implements SeverityStrategy {

    private final Severity severity;

    public FixedSeverityStrategy(Severity severity) {
        this.severity = severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Workspace workspace) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, ViewSet viewSet) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, View view) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, ElementStyle elementStyle) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Model model) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Element element) {
        return severity;
    }

    @Override
    public Severity getSeverity(Inspection inspection, Relationship relationship) {
        return severity;
    }

}