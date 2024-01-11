package com.structurizr.assistant;

import com.structurizr.Workspace;
import com.structurizr.assistant.model.*;
import com.structurizr.assistant.view.ContainerViewsForMultipleSoftwareSystemsInspection;
import com.structurizr.assistant.view.ElementStyleMetadataInspection;
import com.structurizr.assistant.view.EmptyViewsInspection;
import com.structurizr.assistant.view.SystemContextViewsForMultipleSoftwareSystemsInspection;
import com.structurizr.assistant.workspace.WorkspaceScopeInspection;
import com.structurizr.model.*;
import com.structurizr.view.ElementStyle;

public class DefaultAssistant extends Assistant {

    private static final String ALL_RECOMMENDATIONS = "structurizr.recommendations";

    public DefaultAssistant(Workspace workspace) {
        if (workspace.getProperties().getOrDefault(ALL_RECOMMENDATIONS, "true").equalsIgnoreCase("false")) {
            // skip all inspections
            return;
        }

        runWorkspaceInspections(workspace);
        runModelInspections(workspace);
        runViewInspections(workspace);
    }

    private void runWorkspaceInspections(Workspace workspace) {
        add(new WorkspaceScopeInspection(workspace).run());
    }

    private void runModelInspections(Workspace workspace) {
        add(new EmptyModelInspection(workspace).run());
        add(new MultipleSoftwareSystemsDetailedInspection(workspace).run());
        ElementNotIncludedInAnyViewsInspection elementNotIncludedInAnyViewsCheck = new ElementNotIncludedInAnyViewsInspection(workspace);
        OrphanedElementInspection orphanedElementCheck = new OrphanedElementInspection(workspace);
        for (Element element : workspace.getModel().getElements()) {
            add(elementNotIncludedInAnyViewsCheck.run(element));
            add(orphanedElementCheck.run(element));

            if (element instanceof Person) {
                add(new PersonDescriptionInspection(workspace).run(element));
            }

            if (element instanceof SoftwareSystem) {
                add(new SoftwareSystemDescriptionInspection(workspace).run(element));
                add(new SoftwareSystemDocumentationInspection(workspace).run(element));
                add(new SoftwareSystemDecisionsInspection(workspace).run(element));
            }

            if (element instanceof Container) {
                add(new ContainerDescriptionInspection(workspace).run(element));
                add(new ContainerTechnologyInspection(workspace).run(element));
            }

            if (element instanceof Component) {
                add(new ComponentDescriptionInspection(workspace).run(element));
                add(new ComponentTechnologyInspection(workspace).run(element));
            }
        }
    }

    private void runViewInspections(Workspace workspace) {
        add(new EmptyViewsInspection(workspace).run());
        add(new SystemContextViewsForMultipleSoftwareSystemsInspection(workspace).run());
        add(new ContainerViewsForMultipleSoftwareSystemsInspection(workspace).run());

        for (ElementStyle elementStyle : workspace.getViews().getConfiguration().getStyles().getElements()) {
            add(new ElementStyleMetadataInspection(workspace).run(elementStyle));
        }
    }

}