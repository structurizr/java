package com.structurizr.export.mermaid;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * Exports diagram definitions that can be used to create diagrams
 * using mermaid (https://mermaidjs.github.io).
 *
 * System landscape, system context, container, component, dynamic and deployment diagrams are supported.
 * Deployment node -&gt; deployment node relationships are not rendered.
 */
public class MermaidDiagramExporter extends AbstractDiagramExporter {

    public static final String MERMAID_TITLE_PROPERTY = "mermaid.title";
    public static final String MERMAID_SEQUENCE_DIAGRAM_PROPERTY = "mermaid.sequenceDiagram";
    public static final String MERMAID_ICONS_PROPERTY = "mermaid.icons";

    private int groupId = 0;

    public MermaidDiagramExporter() {
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        groupId = 0;
        String direction = "TB";

        if (view.getAutomaticLayout() != null) {
            switch (view.getAutomaticLayout().getRankDirection()) {
                case TopBottom:
                    direction = "TB";
                    break;
                case BottomTop:
                    direction = "BT";
                    break;
                case LeftRight:
                    direction = "LR";
                    break;
                case RightLeft:
                    direction = "RL";
                    break;
            }
        }

        writer.writeLine("graph " + direction);
        writer.indent();
        writer.writeLine("linkStyle default fill:#ffffff");
        writer.writeLine();

        String viewTitle = " ";
        if (includeTitle(view)) {
            viewTitle = view.getTitle();
            if (StringUtils.isNullOrEmpty(viewTitle)) {
                viewTitle = view.getName();
            }
        }

        writer.writeLine("subgraph diagram [\"" + viewTitle + "\"]");
        writer.indent();
        writer.writeLine("style diagram fill:#ffffff,stroke:#ffffff");
        writer.writeLine();
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.outdent();
    }

    @Override
    protected void startEnterpriseBoundary(ModelView view, String enterpriseName, IndentingWriter writer) {
        writer.writeLine("subgraph enterprise [\"" + enterpriseName + "\"]");
        writer.indent();
        writer.writeLine("style enterprise fill:#ffffff,stroke:#444444,color:#444444");
        writer.writeLine();
    }

    @Override
    protected void endEnterpriseBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.writeLine();
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
        groupId++;

        String groupName = group;

        String groupSeparator = view.getModel().getProperties().get(GROUP_SEPARATOR_PROPERTY_NAME);
        if (!StringUtils.isNullOrEmpty(groupSeparator)) {
            groupName = group.substring(group.lastIndexOf(groupSeparator) + groupSeparator.length());
        }

        String color = "#cccccc";

        // is there a style for the group?
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group:" + group);

        if (elementStyle == null || StringUtils.isNullOrEmpty(elementStyle.getColor())) {
            // no, so is there a default group style?
            elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group");
        }

        if (elementStyle != null && !StringUtils.isNullOrEmpty(elementStyle.getColor())) {
            color = elementStyle.getColor();
        }

        writer.writeLine(String.format("subgraph group%s [\"" + groupName + "\"]", groupId));
        writer.indent();
        writer.writeLine(String.format("style group%s fill:#ffffff,stroke:%s,color:%s,stroke-dasharray:5", groupId, color, color));
        writer.writeLine();
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.writeLine();
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(softwareSystem);
        String color = elementStyle.getStroke();

        writer.writeLine(String.format("subgraph %s [\"%s\"]", softwareSystem.getId(), softwareSystem.getName()));
        writer.indent();
        writer.writeLine(String.format("style %s fill:#ffffff,stroke:%s,color:%s", softwareSystem.getId(), color, color));
        writer.writeLine();
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.writeLine();
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(container);
        String color = elementStyle.getStroke();

        writer.writeLine(String.format("subgraph %s [\"%s\"]", container.getId(), container.getName()));
        writer.indent();
        writer.writeLine(String.format("style %s fill:#ffffff,stroke:%s,color:%s", container.getId(), color, color));
        writer.writeLine();
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.writeLine();
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(deploymentNode);

        writer.writeLine(String.format("subgraph %s [\"%s\"]", deploymentNode.getId(), deploymentNode.getName()));
        writer.indent();
        writer.writeLine(String.format("style %s fill:#ffffff,stroke:%s,color:%s", deploymentNode.getId(), elementStyle.getStroke(), elementStyle.getColor()));
        writer.writeLine();
    }

    @Override
    protected void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("end");
        writer.writeLine();
    }

    @Override
    public Diagram export(DynamicView view) {
        if (renderAsSequenceDiagram(view)) {
            IndentingWriter writer = new IndentingWriter();
            writer.writeLine("sequenceDiagram");
            writer.writeLine();
            writer.indent();

            Set<Element> elements = new LinkedHashSet<>();
            for (RelationshipView relationshipView : view.getRelationships()) {
                elements.add(relationshipView.getRelationship().getSource());
                elements.add(relationshipView.getRelationship().getDestination());
            }

            for (Element element : elements) {
                ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);
                String shape = "participant";
                if (elementStyle.getShape() == Shape.Person) {
                    shape = "actor";
                }

                String type = typeOf(view, element, true);

                if (StringUtils.isNullOrEmpty(type) || false == elementStyle.getMetadata()) {
                    type = "";
                } else {
                    type = "<br />" + type;
                }

                writer.writeLine(String.format("%s %s as %s%s", shape, element.getId(), element.getName(), type));
            }

            writer.writeLine();

            for (RelationshipView relationshipView : view.getRelationships()) {
                Relationship relationship = relationshipView.getRelationship();
                RelationshipStyle style = view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship);

                String description = relationshipView.getDescription();
                if (StringUtils.isNullOrEmpty(description)) {
                    description = relationship.getDescription();
                }

                String sourceId = relationship.getSourceId();
                String destinationId = relationship.getDestinationId();

                if (relationshipView.isResponse()) {
                    sourceId = relationship.getDestinationId();
                    destinationId = relationship.getSourceId();
                }

                String technology = !StringUtils.isNullOrEmpty(relationship.getTechnology()) ? "<br />[" + relationship.getTechnology() + "]" : "";

                String arrow;

                if (!relationshipView.isResponse()) {
                    arrow = "->>";
                } else {
                    arrow = "-->>";
                }

                writer.writeLine(String.format("%s%s%s: %s%s",
                        sourceId,
                        arrow,
                        destinationId,
                        description,
                        technology));
            }

            return createDiagram(view, writer.toString());
        } else {
            return super.export(view);
        }
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);

        String name = element.getName();
        String description = element.getDescription();
        String type = typeOf(view, element, true);
        String icon = "";

        if (element instanceof StaticStructureElementInstance) {
            StaticStructureElementInstance elementInstance = (StaticStructureElementInstance)element;
            name = elementInstance.getElement().getName();
            description = elementInstance.getElement().getDescription();
            type = typeOf(view, elementInstance.getElement(), true);
        }

        String nodeOpeningSymbol = "[";
        String nodeClosingSymbol = "]";

        if (elementStyle.getShape() == Shape.RoundedBox) {
            nodeOpeningSymbol = "(";
            nodeClosingSymbol = ")";
        } else if (elementStyle.getShape() == Shape.Cylinder) {
            nodeOpeningSymbol = "[(";
            nodeClosingSymbol = ")]";
        }

        if (StringUtils.isNullOrEmpty(description) || false == elementStyle.getDescription()) {
            description = "";
        } else {
            description = String.format("<div style='font-size: 80%%; margin-top:10px'>%s</div>", lines(description));
        }

        if (false == elementStyle.getMetadata()) {
            type = "";
        } else {
            type = String.format("<div style='font-size: 70%%; margin-top: 0px'>%s</div>", type);
        }

        if ("true".equals(getViewOrViewSetProperty(view, MERMAID_ICONS_PROPERTY, "false")) && elementStyleHasSupportedIcon(elementStyle)) {
            icon = "<div><img src='" + elementStyle.getIcon() + "' style='max-height: 50px; margin: auto; margin-top:10px'/></div>";
        }

        writer.writeLine(format("%s%s\"<div style='font-weight: bold'>%s</div>%s%s%s\"%s",
                element.getId(),
                nodeOpeningSymbol,
                name,
                type,
                description,
                icon,
                nodeClosingSymbol
        ));

        if (!StringUtils.isNullOrEmpty(element.getUrl())) {
            writer.writeLine(format("click %s %s \"%s\"", element.getId(), element.getUrl(), element.getUrl()));
        }

        if (element instanceof StaticStructureElementInstance) {
            Element e = ((StaticStructureElementInstance)element).getElement();
            writer.writeLine(format("style %s fill:%s,stroke:%s,color:%s", element.getId(), elementStyle.getBackground(), elementStyle.getStroke(), elementStyle.getColor()));
        } else {
            writer.writeLine(format("style %s fill:%s,stroke:%s,color:%s", element.getId(), elementStyle.getBackground(), elementStyle.getStroke(), elementStyle.getColor()));
        }
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        Relationship relationship = relationshipView.getRelationship();
        RelationshipStyle style = view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship);

        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (source instanceof DeploymentNode || destination instanceof DeploymentNode) {
            return;
        }

        if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
            source = relationship.getDestination();
            destination = relationship.getSource();
        }

        boolean solid = style.getStyle() == LineStyle.Solid || false == style.getDashed();
        // solid: A-- text -->B
        // dotted: A-. text .->B

        String description = relationshipView.getDescription();
        if (StringUtils.isNullOrEmpty(description)) {
            description = relationshipView.getRelationship().getDescription();
        }

        if (!StringUtils.isNullOrEmpty(relationshipView.getOrder())) {
            description = relationshipView.getOrder() + ". " + description;
        }

        writer.writeLine(
                format("%s-%s \"<div>%s</div><div style='font-size: 70%%'>%s</div>\" %s->%s",
                        source.getId(),
                        solid ? "-" : ".",
                        lines(description),
                        !StringUtils.isNullOrEmpty(relationship.getTechnology()) ? "[" + relationship.getTechnology() + "]" : "",
                        solid ? "-" : ".",
                        destination.getId()
                )
        );
    }

    private String lines(final String text) {
        StringBuilder buf = new StringBuilder();
        if (text != null) {
            final String[] words = text.trim().split("\\s+");

            final StringBuilder line = new StringBuilder();
            for (final String word : words) {
                if (line.length() == 0) {
                    line.append(word);
                } else if (line.length() + word.length() + 1 < 30) {
                    line.append(' ').append(word);
                } else {
                    buf.append(line.toString());
                    buf.append("<br />");
                    line.setLength(0);
                    line.append(word);
                }
            }
            if (line.length() > 0) {
                buf.append(line.toString());
            }
        }

        return buf.toString();
    }

    @Override
    protected Diagram createDiagram(ModelView view, String definition) {
        return new MermaidDiagram(view, definition);
    }

    protected boolean includeTitle(ModelView view) {
        return "true".equals(getViewOrViewSetProperty(view, MERMAID_TITLE_PROPERTY, "true"));
    }

    protected boolean renderAsSequenceDiagram(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, MERMAID_SEQUENCE_DIAGRAM_PROPERTY, "false"));
    }

    private boolean elementStyleHasSupportedIcon(ElementStyle elementStyle) {
        return !StringUtils.isNullOrEmpty(elementStyle.getIcon()) && elementStyle.getIcon().startsWith("http");
    }

}