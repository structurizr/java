package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.inspection.model.*;
import com.structurizr.inspection.view.ContainerViewsForMultipleSoftwareSystemsInspection;
import com.structurizr.inspection.view.ElementStyleMetadataInspection;
import com.structurizr.inspection.view.EmptyViewsInspection;
import com.structurizr.inspection.view.SystemContextViewsForMultipleSoftwareSystemsInspection;
import com.structurizr.inspection.workspace.WorkspaceScopeInspection;
import com.structurizr.inspection.workspace.WorkspaceToolingInspection;
import com.structurizr.model.*;
import com.structurizr.view.ElementStyle;

public class DefaultInspector extends Inspector {

    public DefaultInspector(Workspace workspace) {
        super(workspace);

        runWorkspaceInspections();
        runModelInspections();
        runViewInspections();
    }

    private void runWorkspaceInspections() {
        add(new WorkspaceToolingInspection(this).run());
        add(new WorkspaceScopeInspection(this).run());
    }

    private void runModelInspections() {
        add(new EmptyModelInspection(this).run());
        add(new MultipleSoftwareSystemsDetailedInspection(this).run());
        ElementNotIncludedInAnyViewsInspection elementNotIncludedInAnyViewsCheck = new ElementNotIncludedInAnyViewsInspection(this);
        OrphanedElementInspection orphanedElementCheck = new OrphanedElementInspection(this);
        for (Element element : getWorkspace().getModel().getElements()) {
            if (element instanceof Person) {
                add(new PersonDescriptionInspection(this).run(element));
            }

            if (element instanceof SoftwareSystem) {
                add(new SoftwareSystemDescriptionInspection(this).run(element));
                add(new SoftwareSystemDocumentationInspection(this).run(element));
                add(new SoftwareSystemDecisionsInspection(this).run(element));
            }

            if (element instanceof Container) {
                add(new ContainerDescriptionInspection(this).run(element));
                add(new ContainerTechnologyInspection(this).run(element));
            }

            if (element instanceof Component) {
                add(new ComponentDescriptionInspection(this).run(element));
                add(new ComponentTechnologyInspection(this).run(element));
            }

            if (element instanceof DeploymentNode) {
                add(new DeploymentNodeDescriptionInspection(this).run(element));
                add(new DeploymentNodeTechnologyInspection(this).run(element));
                add(new EmptyDeploymentNodeInspection(this).run(element));
            }

            if (element instanceof InfrastructureNode) {
                add(new InfrastructureNodeDescriptionInspection(this).run(element));
                add(new InfrastructureNodeTechnologyInspection(this).run(element));
            }

            add(orphanedElementCheck.run(element));
            add(elementNotIncludedInAnyViewsCheck.run(element));
        }

        for (Relationship relationship : getWorkspace().getModel().getRelationships()) {
            add(new RelationshipDescriptionInspection(this).run(relationship));
            add(new RelationshipTechnologyInspection(this).run(relationship));
        }
    }

    private void runViewInspections() {
        add(new EmptyViewsInspection(this).run());
        add(new SystemContextViewsForMultipleSoftwareSystemsInspection(this).run());
        add(new ContainerViewsForMultipleSoftwareSystemsInspection(this).run());

        for (ElementStyle elementStyle : getWorkspace().getViews().getConfiguration().getStyles().getElements()) {
            add(new ElementStyleMetadataInspection(this).run(elementStyle));
        }
    }

    @Override
    public SeverityStrategy getSeverityStrategy() {
        return new PropertyBasedSeverityStrategy();
    }

}