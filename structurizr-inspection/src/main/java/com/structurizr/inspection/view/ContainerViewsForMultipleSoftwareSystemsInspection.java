package com.structurizr.inspection.view;

import com.structurizr.Workspace;
import com.structurizr.inspection.Inspector;
import com.structurizr.inspection.Violation;
import com.structurizr.view.ContainerView;

import java.util.HashSet;
import java.util.Set;

public class ContainerViewsForMultipleSoftwareSystemsInspection extends AbstractViewsInspection {

    public ContainerViewsForMultipleSoftwareSystemsInspection(Inspector inspector) {
        super(inspector);
    }

    @Override
    public Violation inspect(Workspace workspace) {
        Set<String> softwareSystemsWithContainerViews = new HashSet<>();
        for (ContainerView view : workspace.getViews().getContainerViews()) {
            softwareSystemsWithContainerViews.add(view.getSoftwareSystemId());
        }
        if (softwareSystemsWithContainerViews.size() > 1) {
            return violation("Container views exist for " + softwareSystemsWithContainerViews.size() + " software systems. It is recommended that a workspace includes container views for a single software system only.");
        }

        return noViolation();
    }

    @Override
    protected String getType() {
        return "workspace.scope";
    }

}