package com.structurizr.export.dot;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

/**
 * Exports Structurizr views to Graphviz DOT definitions.
 */
public class DOTExporter extends AbstractDiagramExporter {

    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FONT = "Arial";

    private int clusterInternalMargin = 25;

    public DOTExporter() {
    }

    public void setClusterInternalMargin(int clusterInternalMargin) {
        this.clusterInternalMargin = clusterInternalMargin;
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        String title = view.getTitle();
        if (StringUtils.isNullOrEmpty(title)) {
            title = view.getName();
        }

        String description = view.getDescription();
        if (StringUtils.isNullOrEmpty(description)) {
            description = "";
        } else {
            description = String.format("<br /><font point-size=\"24\">%s</font>", description);
        }

        String fontName = DEFAULT_FONT;
        Font font = view.getViewSet().getConfiguration().getBranding().getFont();
        if (font != null) {
            fontName = font.getName();
        }

        RankDirection rankDirection = RankDirection.TopBottom;

        if (view.getAutomaticLayout() != null) {
            switch (view.getAutomaticLayout().getRankDirection()) {
                case TopBottom:
                    rankDirection = RankDirection.TopBottom;
                    break;
                case BottomTop:
                    rankDirection = RankDirection.BottomTop;
                    break;
                case LeftRight:
                    rankDirection = RankDirection.LeftRight;
                    break;
                case RightLeft:
                    rankDirection = RankDirection.RightLeft;
                    break;
            }
        }

        writer.writeLine("digraph {");
        writer.indent();
        writer.writeLine("compound=true");
        writer.writeLine(String.format("graph [fontname=\"%s\", rankdir=%s, ranksep=1.0, nodesep=1.0]", fontName, rankDirection.getCode()));
        writer.writeLine(String.format("node [fontname=\"%s\", shape=box, margin=\"0.4,0.3\"]", fontName));
        writer.writeLine(String.format("edge [fontname=\"%s\"]", fontName));
        writer.writeLine(String.format("label=<<br /><font point-size=\"34\">%s</font>%s>", title, description));
        writer.writeLine();
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
        String color = "#cccccc";

        String groupName = group;

        String groupSeparator = view.getModel().getProperties().get(GROUP_SEPARATOR_PROPERTY_NAME);
        if (!StringUtils.isNullOrEmpty(groupSeparator)) {
            groupName = group.substring(group.lastIndexOf(groupSeparator) + groupSeparator.length());
        }

        // is there a style for the group?
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group:" + group);

        if (elementStyle == null || StringUtils.isNullOrEmpty(elementStyle.getColor())) {
            // no, so is there a default group style?
            elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group");
        }

        if (elementStyle != null && !StringUtils.isNullOrEmpty(elementStyle.getColor())) {
            color = elementStyle.getColor();
        }

        writer.writeLine("subgraph \"cluster_group_" + groupName + "\" {");

        writer.indent();
        writer.writeLine("margin=" + clusterInternalMargin);
        writer.writeLine(String.format("label=<<font point-size=\"24\"><br />%s</font>>", groupName));
        writer.writeLine("labelloc=b");
        writer.writeLine(String.format("color=\"%s\"", color));
        writer.writeLine(String.format("fontcolor=\"%s\"", color));
        writer.writeLine("fillcolor=\"#ffffff\"");
        writer.writeLine("style=\"dashed\"");
        writer.writeLine();
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        String color;
        if (softwareSystem.equals(view.getSoftwareSystem())) {
            color = "#444444";
        } else {
            color = "#cccccc";
        }

        writer.writeLine(String.format("subgraph cluster_%s {", softwareSystem.getId()));
        writer.indent();
        writer.writeLine("margin=" + clusterInternalMargin);
        writer.writeLine(String.format("label=<<font point-size=\"24\"><br />%s</font><br /><font point-size=\"19\">%s</font>>", softwareSystem.getName(), typeOf(view, softwareSystem, true)));
        writer.writeLine("labelloc=b");
        writer.writeLine(String.format("color=\"%s\"", color));
        writer.writeLine(String.format("fontcolor=\"%s\"", color));
        writer.writeLine(String.format("fillcolor=\"%s\"", color));
        writer.writeLine();
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
        String color = "#444444";
        if (view instanceof ComponentView) {
            if (container.equals(((ComponentView)view).getContainer())) {
                color = "#444444";
            } else {
                color = "#cccccc";
            }
        } else if (view instanceof DynamicView) {
            if (container.equals(((DynamicView)view).getElement())) {
                color = "#444444";
            } else {
                color = "#cccccc";
            }
        }

        writer.writeLine(String.format("subgraph cluster_%s {", container.getId()));
        writer.indent();
        writer.writeLine("margin=" + clusterInternalMargin);
        writer.writeLine(String.format("label=<<font point-size=\"24\"><br />%s</font><br /><font point-size=\"19\">%s</font>>", container.getName(), typeOf(view, container, true)));
        writer.writeLine("labelloc=b");
        writer.writeLine(String.format("color=\"%s\"", color));
        writer.writeLine(String.format("fontcolor=\"%s\"", color));
        writer.writeLine(String.format("fillcolor=\"%s\"", color));
        writer.writeLine();
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(deploymentNode);

        writer.writeLine(String.format("subgraph cluster_%s {", deploymentNode.getId()));
        writer.indent();
        writer.writeLine("margin=" + clusterInternalMargin);
        writer.writeLine(String.format("label=<<font point-size=\"24\">%s</font><br /><font point-size=\"19\">%s</font>>", deploymentNode.getName(), typeOf(view, deploymentNode, true)));
        writer.writeLine("labelloc=b");
        writer.writeLine(String.format("color=\"%s\"", elementStyle.getStroke()));
        writer.writeLine(String.format("fontcolor=\"%s\"", elementStyle.getColor()));
        writer.writeLine("fillcolor=\"#ffffff\"");
        writer.writeLine();
    }

    @Override
    protected void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);

        if (elementStyle.getWidth() == null) {
            elementStyle.setWidth(DEFAULT_WIDTH);
        }

        if (elementStyle.getHeight() == null) {
            elementStyle.setHeight(DEFAULT_HEIGHT);
        }

        int nameFontSize = elementStyle.getFontSize() + 10;
        int metadataFontSize = elementStyle.getFontSize() - 5;
        int descriptionFontSize = elementStyle.getFontSize();


        String shape = shapeOf(view, element);
        String name = element.getName();
        String description = element.getDescription();
        String type = typeOf(view, element, true);

        if (element instanceof StaticStructureElementInstance) {
            StaticStructureElementInstance elementInstance = (StaticStructureElementInstance)element;
            name = elementInstance.getElement().getName();
            description = elementInstance.getElement().getDescription();
            type = typeOf(view, elementInstance.getElement(), true);
            shape = shapeOf(view, elementInstance.getElement());
        }

        if (StringUtils.isNullOrEmpty(name)) {
            name = "";
        } else {
            name = String.format("<font point-size=\"%s\">%s</font>", nameFontSize, breakText(elementStyle.getWidth(), nameFontSize, escape(name)));
        }

        if (StringUtils.isNullOrEmpty(description) || false == elementStyle.getDescription()) {
            description = "";
        } else {
            description = String.format("<br /><br /><font point-size=\"%s\">%s</font>", descriptionFontSize, breakText(elementStyle.getWidth(), descriptionFontSize, escape(description)));
        }

        if (StringUtils.isNullOrEmpty(type) || false == elementStyle.getMetadata()) {
            type = "";
        } else {
            type = String.format("<br /><font point-size=\"%s\">%s</font>", metadataFontSize, type);
        }

        writer.writeLine(String.format("%s [id=%s,shape=%s, label=<%s%s%s>, style=filled, color=\"%s\", fillcolor=\"%s\", fontcolor=\"%s\"]",
                element.getId(),
                element.getId(),
                shape,
                name,
                type,
                description,
                elementStyle.getStroke(),
                elementStyle.getBackground(),
                elementStyle.getColor()
        ));
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        Element source;
        Element destination;

        RelationshipStyle relationshipStyle = view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationshipView.getRelationship());
        relationshipStyle.setWidth(400);
        int descriptionFontSize = relationshipStyle.getFontSize();
        int metadataFontSize = relationshipStyle.getFontSize() - 5;

        String description = relationshipView.getDescription();
        if (StringUtils.isNullOrEmpty(description)) {
            description = relationshipView.getRelationship().getDescription();
        }

        if (!StringUtils.isNullOrEmpty(relationshipView.getOrder())) {
            description = relationshipView.getOrder() + ". " + description;
        }

        if (StringUtils.isNullOrEmpty(description)) {
            description = "";
        } else {
            description = breakText(relationshipStyle.getWidth(), descriptionFontSize, description);
            description = String.format("<font point-size=\"%s\">%s</font>", descriptionFontSize, description);
        }

        String technology = relationshipView.getRelationship().getTechnology();
        if (StringUtils.isNullOrEmpty(technology)) {
            technology = "";
        } else {
            technology = String.format("<br /><font point-size=\"%s\">[%s]</font>", metadataFontSize, technology);
        }

        String clusterConfig = "";

        if (relationshipView.getRelationship().getSource() instanceof DeploymentNode || relationshipView.getRelationship().getDestination() instanceof DeploymentNode) {
            source = relationshipView.getRelationship().getSource();
            if (source instanceof DeploymentNode) {
                source = findElementInside((DeploymentNode)source, view);
            }

            destination = relationshipView.getRelationship().getDestination();
            if (destination instanceof DeploymentNode) {
                destination = findElementInside((DeploymentNode)destination, view);
            }

            if (source != null && destination != null) {

                if (relationshipView.getRelationship().getSource() instanceof DeploymentNode) {
                    clusterConfig += ",ltail=cluster_" + relationshipView.getRelationship().getSource().getId();
                }

                if (relationshipView.getRelationship().getDestination() instanceof DeploymentNode) {
                    clusterConfig += ",lhead=cluster_" + relationshipView.getRelationship().getDestination().getId();
                }
            }
        } else {
            source = relationshipView.getRelationship().getSource();
            destination = relationshipView.getRelationship().getDestination();

            if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
                source = relationshipView.getRelationship().getDestination();
                destination = relationshipView.getRelationship().getSource();
            }
        }

        boolean solid = relationshipStyle.getStyle() == LineStyle.Solid || false == relationshipStyle.getDashed();

        writer.writeLine(String.format("%s -> %s [id=%s, label=<%s%s>, style=\"%s\", color=\"%s\", fontcolor=\"%s\"%s]",
                source.getId(),
                destination.getId(),
                relationshipView.getId(),
                description,
                technology,
                solid ? "solid" : "dashed",
                relationshipStyle.getColor(),
                relationshipStyle.getColor(),
                clusterConfig
        ));
    }

    private String escape(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return s;
        } else {
            return s.replaceAll("\"", "\\\\\"");
        }
    }

    private String shapeOf(ModelView view, Element element) {
        if (element instanceof DeploymentNode) {
            return "node";
        }

        Shape shape = view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getShape();
        switch(shape) {
            case Circle:
                return "circle";
            case Component:
                return "component";
            case Cylinder:
                return "cylinder";
            case Ellipse:
                return "ellipse";
            case Folder:
                return "folder";
            case Hexagon:
                return "hexagon";
            case Diamond:
                return "diamond";
            default:
                return "rect";
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

    @Override
    protected Diagram createDiagram(ModelView view, String definition) {
        return new DOTDiagram(view, definition);
    }

}