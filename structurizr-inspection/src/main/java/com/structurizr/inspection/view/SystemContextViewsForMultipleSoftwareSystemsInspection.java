package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.view.SystemContextView;

import java.util.HashSet;
import java.util.Set;

public class SystemContextViewsForMultipleSoftwareSystemsInspection extends AbstractViewsInspection {

    public SystemContextViewsForMultipleSoftwareSystemsInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    protected Violation inspect(Workspace workspace) {
        Set<String> softwareSystemsWithSystemContextViews = new HashSet<>();
        for (SystemContextView view : workspace.getViews().getSystemContextViews()) {
            softwareSystemsWithSystemContextViews.add(view.getSoftwareSystemId());
        }
        if (softwareSystemsWithSystemContextViews.size() > 1) {
            return violation("System context views exist for " + softwareSystemsWithSystemContextViews.size() + " software systems. It is recommended that a workspace includes system context views for a single software system only.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}