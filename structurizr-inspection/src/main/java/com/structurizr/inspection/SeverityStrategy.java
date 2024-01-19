package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.model.Element;
import com.structurizr.model.Model;
import com.structurizr.model.Relationship;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.View;
import com.structurizr.view.ViewSet;

public interface SeverityStrategy {

    Severity getSeverity(Inspection inspection, Workspace workspace);

    Severity getSeverity(Inspection inspection, ViewSet viewSet);

    Severity getSeverity(Inspection inspection, View view);

    Severity getSeverity(Inspection inspection, ElementStyle elementStyle);

    Severity getSeverity(Inspection inspection, Model model);

    Severity getSeverity(Inspection inspection, Element element);

    Severity getSeverity(Inspection inspection, Relationship relationship);

}
