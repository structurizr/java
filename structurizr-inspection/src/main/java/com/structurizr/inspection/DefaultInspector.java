package com.structurizr.inspection;

import com.structurizr.Workspace;
import com.structurizr.inspection.documentation.EmbeddedViewMissingInspection;
import com.structurizr.inspection.documentation.EmbeddedViewWithGeneratedKeyInspection;
import com.structurizr.inspection.model.*;
import com.structurizr.inspection.view.*;
import com.structurizr.inspection.workspace.WorkspaceScopeInspection;
import com.structurizr.inspection.workspace.WorkspaceToolingInspection;
import com.structurizr.model.*;
import com.structurizr.view.*;

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
        add(new EmbeddedViewMissingInspection(this).run(getWorkspace()));
        add(new EmbeddedViewWithGeneratedKeyInspection(this).run(getWorkspace()));
    }

    private void runModelInspections() {
        add(new EmptyModelInspection(this).run());
        add(new MultipleSoftwareSystemsDetailedInspection(this).run());
        ElementNotIncludedInAnyViewsInspection elementNotIncludedInAnyViewsCheck = new ElementNotIncludedInAnyViewsInspection(this);
        DisconnectedElementInspection disconnectedElementCheck = new DisconnectedElementInspection(this);
        for (Element element : getWorkspace().getModel().getElements()) {
            if (element instanceof Person) {
                add(new PersonDescriptionInspection(this).run(element));
            }

            if (element instanceof SoftwareSystem) {
                add(new SoftwareSystemDescriptionInspection(this).run(element));
                add(new SoftwareSystemDocumentationInspection(this).run(element));
                add(new SoftwareSystemDecisionsInspection(this).run(element));
                add(new EmbeddedViewMissingInspection(this).run((SoftwareSystem)element));
                add(new EmbeddedViewWithGeneratedKeyInspection(this).run((SoftwareSystem)element));
            }

            if (element instanceof Container) {
                add(new ContainerDescriptionInspection(this).run(element));
                add(new ContainerTechnologyInspection(this).run(element));
                add(new EmbeddedViewMissingInspection(this).run((Container)element));
                add(new EmbeddedViewWithGeneratedKeyInspection(this).run((Container)element));
            }

            if (element instanceof Component) {
                add(new ComponentDescriptionInspection(this).run(element));
                add(new ComponentTechnologyInspection(this).run(element));
                add(new EmbeddedViewMissingInspection(this).run((Component)element));
                add(new EmbeddedViewWithGeneratedKeyInspection(this).run((Component)element));
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

            add(disconnectedElementCheck.run(element));
            add(elementNotIncludedInAnyViewsCheck.run(element));
        }

        for (Relationship relationship : getWorkspace().getModel().getRelationships()) {
            add(new RelationshipDescriptionInspection(this).run(relationship));
            add(new RelationshipTechnologyInspection(this).run(relationship));
        }
    }

    private void runViewInspections() {
        add(new EmptyViewsInspection(this).run());

        for (CustomView view : getWorkspace().getViews().getCustomViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        for (SystemLandscapeView view : getWorkspace().getViews().getSystemLandscapeViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        add(new SystemContextViewsForMultipleSoftwareSystemsInspection(this).run());
        for (SystemContextView view : getWorkspace().getViews().getSystemContextViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        add(new ContainerViewsForMultipleSoftwareSystemsInspection(this).run());
        for (ContainerView view : getWorkspace().getViews().getContainerViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        for (ComponentView view : getWorkspace().getViews().getComponentViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        for (DynamicView view : getWorkspace().getViews().getDynamicViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        for (DeploymentView view : getWorkspace().getViews().getDeploymentViews()) {
            add(new GeneratedKeyInspection(this).run(view));
            add(new EmptyViewInspection(this).run(view));
            add(new ManualLayoutInspection(this).run(view));
        }

        for (FilteredView view : getWorkspace().getViews().getFilteredViews()) {
            add(new GeneratedKeyInspection(this).run(view));
        }

        for (ImageView view : getWorkspace().getViews().getImageViews()) {
            add(new GeneratedKeyInspection(this).run(view));
        }

        for (ElementStyle elementStyle : getWorkspace().getViews().getConfiguration().getStyles().getElements()) {
            add(new ElementStyleMetadataInspection(this).run(elementStyle));
        }
    }

    @Override
    public SeverityStrategy getSeverityStrategy() {
        return new PropertyBasedSeverityStrategy();
    }

}