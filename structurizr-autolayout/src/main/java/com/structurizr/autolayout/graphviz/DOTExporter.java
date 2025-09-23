package com.structurizr.autolayout.graphviz;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.ModelView;
import com.structurizr.view.RelationshipView;

import java.util.Locale;

/**
 * Writes a Structurizr view to a graphviz dot file. Please note that this is not a full export (colours, shapes, etc);
 * it just contains the basics required for layout purposes.
 */
class DOTExporter extends AbstractDiagramExporter {

    private static final String DEFAULT_CLUSTER_INTERNAL_PADDING = "25";
    private static final String GROUP_PADDING_PROPERTY_NAME = "structurizr.groupPadding";
    private static final String BOUNDARY_PADDING_PROPERTY_NAME = "structurizr.boundaryPadding";
    private static final String DEPLOYMENT_NODE_PADDING_PROPERTY_NAME = "structurizr.deploymentNodePadding";

    private Locale locale = Locale.US;
    private final RankDirection rankDirection;
    private final double rankSeparation;
    private final double nodeSeparation;

    private int groupId = 1;

    DOTExporter(RankDirection rankDirection, double rankSeparation, double nodeSeparation) {
        this.rankDirection = rankDirection != null ? rankDirection : RankDirection.TopBottom;
        this.rankSeparation = rankSeparation / Constants.STRUCTURIZR_DPI;
        this.nodeSeparation = nodeSeparation / Constants.STRUCTURIZR_DPI;
    }

    void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        writer.writeLine("digraph {");
        writer.indent();
        writer.writeLine("compound=true");
        writer.writeLine(String.format(locale, "graph [splines=polyline,rankdir=%s,ranksep=%s,nodesep=%s,fontsize=5]", rankDirection.getCode(), rankSeparation, nodeSeparation));
        writer.writeLine("node [shape=box,fontsize=5]");
        writer.writeLine("edge []");
        writer.writeLine();
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
        writer.writeLine("subgraph \"cluster_group_" + (groupId++) + "\" {");
        writer.indent();
        writer.writeLine("margin=" + Integer.parseInt(getViewOrViewSetProperty(view, GROUP_PADDING_PROPERTY_NAME, DEFAULT_CLUSTER_INTERNAL_PADDING)));
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        writer.writeLine(String.format("subgraph cluster_%s {", softwareSystem.getId()));
        writer.indent();
        writer.writeLine("margin=" + Integer.parseInt(getViewOrViewSetProperty(view, BOUNDARY_PADDING_PROPERTY_NAME, DEFAULT_CLUSTER_INTERNAL_PADDING)));
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
        writer.writeLine(String.format("subgraph cluster_%s {", container.getId()));
        writer.indent();
        writer.writeLine("margin=" + Integer.parseInt(getViewOrViewSetProperty(view, BOUNDARY_PADDING_PROPERTY_NAME, DEFAULT_CLUSTER_INTERNAL_PADDING)));
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        writer.writeLine(String.format("subgraph cluster_%s {", deploymentNode.getId()));
        writer.indent();
        writer.writeLine("margin=" + Integer.parseInt(getViewOrViewSetProperty(view, DEPLOYMENT_NODE_PADDING_PROPERTY_NAME, DEFAULT_CLUSTER_INTERNAL_PADDING)));
    }

    @Override
    protected void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        writer.writeLine(String.format(locale, "%s [width=%f,height=%f,fixedsize=true,id=%s,label=\"%s: %s\"]",
                element.getId(),
                getElementWidth(view, element.getId()) / Constants.STRUCTURIZR_DPI, // convert Structurizr dimensions to inches
                getElementHeight(view, element.getId()) / Constants.STRUCTURIZR_DPI, // convert Structurizr dimensions to inches
                element.getId(),
                element.getId(),
                escape(element.getName())
        ));
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        if (relationshipView.getRelationship().getSource() instanceof DeploymentNode || relationshipView.getRelationship().getDestination() instanceof DeploymentNode) {
            Element source = relationshipView.getRelationship().getSource();
            if (source instanceof DeploymentNode) {
                source = findElementInside((DeploymentNode)source, view);
            }

            Element destination = relationshipView.getRelationship().getDestination();
            if (destination instanceof DeploymentNode) {
                destination = findElementInside((DeploymentNode)destination, view);
            }

            if (source != null && destination != null) {
                String clusterConfig = "";

                if (relationshipView.getRelationship().getSource() instanceof DeploymentNode) {
                    clusterConfig += ",ltail=cluster_" + relationshipView.getRelationship().getSource().getId();
                }

                if (relationshipView.getRelationship().getDestination() instanceof DeploymentNode) {
                    clusterConfig += ",lhead=cluster_" + relationshipView.getRelationship().getDestination().getId();
                }

                writer.writeLine(String.format(locale, "%s -> %s [id=%s%s]",
                        source.getId(),
                        destination.getId(),
                        relationshipView.getId(),
                        clusterConfig
                ));
            }
        } else {
            Element source = relationshipView.getRelationship().getSource();
            Element destination = relationshipView.getRelationship().getDestination();

            if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
                source = relationshipView.getRelationship().getDestination();
                destination = relationshipView.getRelationship().getSource();
            }

            writer.writeLine(String.format(locale, "%s -> %s [id=%s]",
                    source.getId(),
                    destination.getId(),
                    relationshipView.getId()
            ));
        }
    }

    @Override
    protected Diagram createDiagram(ModelView view, String definition) {
        return new DOTDiagram(view, definition);
    }

    private String escape(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return s;
        } else {
            return s.replaceAll("\"", "\\\\\"");
        }
    }

    private Element findElementInside(DeploymentNode deploymentNode, ModelView view) {
        for (SoftwareSystemInstance softwareSystemInstance : deploymentNode.getSoftwareSystemInstances()) {
            if (view.isElementInView(softwareSystemInstance)) {
                return softwareSystemInstance;
            }
        }

        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
            if (view.isElementInView(containerInstance)) {
                return containerInstance;
            }
        }

        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
            if (view.isElementInView(infrastructureNode)) {
                return infrastructureNode;
            }
        }

        if (deploymentNode.hasChildren()) {
            for (DeploymentNode child : deploymentNode.getChildren()) {
                Element element = findElementInside(child, view);

                if (element != null) {
                    return element;
                }
            }
        }

        return null;
    }

    private int getElementWidth(ModelView view, String elementId) {
        Element element = view.getModel().getElement(elementId);
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getWidth();
    }

    private int getElementHeight(ModelView view, String elementId) {
        Element element = view.getModel().getElement(elementId);
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getHeight();
    }

}