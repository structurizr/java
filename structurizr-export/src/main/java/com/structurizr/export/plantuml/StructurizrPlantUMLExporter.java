package com.structurizr.export.plantuml;

import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.export.Legend;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class StructurizrPlantUMLExporter extends AbstractPlantUMLExporter {

    public static final String PLANTUML_SHADOW = "plantuml.shadow";

    private int groupId = 0;

    public StructurizrPlantUMLExporter() {
        addSkinParam("arrowFontSize", "10");
        addSkinParam("defaultTextAlignment", "center");
        addSkinParam("wrapWidth", "200");
        addSkinParam("maxMessageSize", "100");
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        super.writeHeader(view, writer);
        if (view instanceof DynamicView && renderAsTeozDiagram(view)) {
            writer.writeLine("!pragma teoz true");
        }
        groupId = 0;

        if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
            // do nothing
        } else {
            if (view.getAutomaticLayout() != null) {
                switch (view.getAutomaticLayout().getRankDirection()) {
                    case LeftRight:
                        writer.writeLine("left to right direction");
                        break;
                    default:
                        writer.writeLine("top to bottom direction");
                        break;
                }

                // the default 300px rank separation in the Structurizr UI is equivalent to a default of 60 in PlantUML
                writer.writeLine("skinparam ranksep " + view.getAutomaticLayout().getRankSeparation() / (300/60));

                // the default 300px node separation in the Structurizr UI is equivalent to a default of 30 in PlantUML
                writer.writeLine("skinparam nodesep " + view.getAutomaticLayout().getNodeSeparation() / (300/30));
            } else {
                writer.writeLine("top to bottom direction");
            }

            writer.writeLine();
        }

        Font font = view.getViewSet().getConfiguration().getBranding().getFont();
        if (font != null) {
            String fontName = font.getName();
            if (!StringUtils.isNullOrEmpty(fontName)) {
                addSkinParam("defaultFontName", "\"" + fontName + "\"");
            }
        }

        writeSkinParams(writer);
        writeIncludes(view, writer);

        writer.writeLine();
        writer.writeLine("hide stereotype");
        writer.writeLine();

        List<Element> elements = view.getElements().stream().map(ElementView::getElement).sorted(Comparator.comparing(Element::getName)).collect(Collectors.toList());
        for (Element element : elements) {
            String id = idOf(element);

            String type = plantUMLShapeOf(view, element);
            if ("actor".equals(type)) {
                type = "rectangle"; // the actor shape is not supported in this implementation
            }

            ElementStyle elementStyle = findElementStyle(view, element);

            String background = elementStyle.getBackground();
            String stroke = elementStyle.getStroke();
            String color = elementStyle.getColor();
            Shape shape = elementStyle.getShape();

            if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
                type = "sequenceParticipant";
            }

            writer.writeLine(format("skinparam %s<<%s>> {", type, id));
            writer.indent();
            if (element instanceof DeploymentNode) {
                writer.writeLine("BackgroundColor #ffffff");
            } else {
                writer.writeLine(String.format("BackgroundColor %s", background));
            }
            writer.writeLine(String.format("FontColor %s", color));
            writer.writeLine(String.format("BorderColor %s", stroke));

            if (shape == Shape.RoundedBox) {
                writer.writeLine("roundCorner 20");
            }

            boolean shadow = "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"));
            writer.writeLine(String.format("shadowing %s", shadow));

            writer.outdent();
            writer.writeLine("}");
        }

        if (!renderAsSequenceDiagram(view)) {
            // boundaries
            List<Element> boundaryElements = new ArrayList<>();
            if (view instanceof ContainerView) {
                boundaryElements.addAll(getBoundarySoftwareSystems(view));
            } else if (view instanceof ComponentView) {
                boundaryElements.addAll(getBoundaryContainers(view));
            } else if (view instanceof DynamicView) {
                DynamicView dynamicView = (DynamicView) view;
                if (dynamicView.getElement() instanceof SoftwareSystem) {
                    boundaryElements.addAll(getBoundarySoftwareSystems(view));
                } else if (dynamicView.getElement() instanceof Container) {
                    boundaryElements.addAll(getBoundaryContainers(view));
                }
            }

            for (Element boundaryElement : boundaryElements) {
                ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(boundaryElement);
                String id = idOf(boundaryElement);
                String color = elementStyle.getStroke();
                boolean shadow = "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"));

                writer.writeLine(format("skinparam rectangle<<%s>> {", id));
                writer.indent();
                writer.writeLine(String.format("BorderColor %s", color));
                writer.writeLine(String.format("FontColor %s", color));
                writer.writeLine(String.format("shadowing %s", shadow));
                writer.outdent();
                writer.writeLine("}");
            }
        }

        writer.writeLine();
    }

    @Override
    protected void startEnterpriseBoundary(ModelView view, String enterpriseName, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("rectangle \"%s\" <<enterprise>> {", enterpriseName));
            writer.indent();
            writer.writeLine("skinparam RectangleBorderColor<<enterprise>> #444444");
            writer.writeLine("skinparam RectangleFontColor<<enterprise>> #444444");
            writer.writeLine();
        }
    }

    @Override
    protected void endEnterpriseBoundary(ModelView view, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.outdent();
            writer.writeLine("}");
            writer.writeLine();
        }
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
        String icon = "";

        ElementStyle elementStyleForGroup = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group:" + group);
        ElementStyle elementStyleForAllGroups = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group");

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getColor())) {
            color = elementStyleForGroup.getColor();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getColor())) {
            color = elementStyleForAllGroups.getColor();
        }

        if (elementStyleForGroup != null && elementStyleHasSupportedIcon(elementStyleForGroup)) {
            icon = elementStyleForGroup.getIcon();
        } else if (elementStyleForAllGroups != null && elementStyleHasSupportedIcon(elementStyleForAllGroups)) {
            icon = elementStyleForAllGroups.getColor();
        }

        if (!StringUtils.isNullOrEmpty(icon)) {
            double scale = calculateIconScale(icon);
            icon = "\\n\\n<img:" + icon + "{scale=" + scale + "}>";
        }
        if (!renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("rectangle \"%s%s\" <<group%s>> as group%s {", groupName, icon, groupId, groupId));
            writer.indent();
            writer.writeLine(String.format("skinparam RectangleBorderColor<<group%s>> %s", groupId, color));
            writer.writeLine(String.format("skinparam RectangleFontColor<<group%s>> %s", groupId, color));
            writer.writeLine(String.format("skinparam RectangleBorderStyle<<group%s>> dashed", groupId));

            writer.writeLine();
        } else {
            //writer.writeLine(String.format("box \"%s%s\" <<group%s>> as group%s {", groupName, icon, groupId, groupId));
            writer.writeLine(String.format("box \"%s%s\"", groupName, icon));

            writer.indent();
        }
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.outdent();
            writer.writeLine("}");
            writer.writeLine();
        } else {
            writer.outdent();
            writer.writeLine("end box");
            writer.writeLine();
        }
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("rectangle \"%s\\n<size:10>%s</size>\" <<%s>> {", softwareSystem.getName(), typeOf(view, softwareSystem, true), idOf(softwareSystem)));
            writer.indent();
        } else {
            writer.writeLine(String.format("box \"%s\n%s\"", softwareSystem.getName(), typeOf(view, softwareSystem, true)));

            writer.indent();
        }
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.outdent();
            writer.writeLine("}");
            writer.writeLine();
        }
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("rectangle \"%s\\n<size:10>%s</size>\" <<%s>> {", container.getName(), typeOf(view, container, true), idOf(container)));
            writer.indent();
        } else {
            writer.writeLine(String.format("box \"%s\n%s\"", container.getName(), typeOf(view, container, true)));

            writer.indent();
        }
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.outdent();
            writer.writeLine("}");
            writer.writeLine();
        }
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        ElementStyle elementStyle = findElementStyle(view, deploymentNode);

        String icon = "";
        if (elementStyleHasSupportedIcon(elementStyle)) {
            double scale = calculateIconScale(elementStyle.getIcon());
            icon = "\\n\\n<img:" + elementStyle.getIcon() + "{scale=" + scale + "}>";
        }

        String url = deploymentNode.getUrl();
        if (!StringUtils.isNullOrEmpty(url)) {
            url = " [[" + url + "]]";
        } else {
            url = "";
        }

        writer.writeLine(
                format("rectangle \"%s\\n<size:10>%s</size>%s\" <<%s>> as %s%s {",
                        deploymentNode.getName() + (!"1".equals(deploymentNode.getInstances()) ? " (x" + deploymentNode.getInstances() + ")" : ""),
                        typeOf(view, deploymentNode, true),
                        icon,
                        idOf(deploymentNode),
                        idOf(deploymentNode),
                        url
                )
        );
        writer.indent();

        if (!isVisible(view, deploymentNode)) {
            writer.writeLine("hide " + idOf(deploymentNode));
        }
    }

    @Override
    protected void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    public Diagram export(DynamicView view) {
        return super.export(view);
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        ElementStyle elementStyle = findElementStyle(view, element);

        if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("%s \"%s\\n<size:10>%s</size>\" as %s <<%s>> %s",
                    plantumlSequenceType(view, element),
                    element.getName(),
                    typeOf(view, element, true),
                    idOf(element),
                    idOf(element),
                    elementStyle.getBackground()));
        } else {
            String shape = plantUMLShapeOf(view, element);
            if ("actor".equals(shape)) {
                shape = "rectangle";
            }
            String name = element.getName();
            String description = element.getDescription();
            String type = typeOf(view, element, true);
            String icon = "";
            String url = element.getUrl();

            if (element instanceof StaticStructureElementInstance) {
                StaticStructureElementInstance elementInstance = (StaticStructureElementInstance) element;
                name = elementInstance.getElement().getName();
                description = elementInstance.getElement().getDescription();
                type = typeOf(view, elementInstance.getElement(), true);
                shape = plantUMLShapeOf(view, elementInstance.getElement());
                url = elementInstance.getUrl();

                if (StringUtils.isNullOrEmpty(url)) {
                    url = elementInstance.getElement().getUrl();
                }
            }

            if (!StringUtils.isNullOrEmpty(url)) {
                url = " [[" + url + "]]";
            } else {
                url = "";
            }

            if (StringUtils.isNullOrEmpty(description) || false == elementStyle.getDescription()) {
                description = "";
            } else {
                description = "\\n\\n" + description;
            }

            if (StringUtils.isNullOrEmpty(type) || false == elementStyle.getMetadata()) {
                type = "";
            } else {
                type = String.format("\\n<size:10>%s</size>", type);
            }

            if (elementStyleHasSupportedIcon(elementStyle)) {
                double scale = calculateIconScale(elementStyle.getIcon());
                icon = "\\n\\n<img:" + elementStyle.getIcon() + "{scale=" + scale + "}>";
            }

            String id = idOf(element);

            writer.writeLine(format("%s \"==%s%s%s%s\" <<%s>> as %s%s",
                    shape,
                    name,
                    type,
                    description,
                    icon,
                    id,
                    id,
                    url)
            );

            if (!isVisible(view, element)) {
                writer.writeLine("hide " + id);
            }
        }
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        Relationship relationship = relationshipView.getRelationship();
        RelationshipStyle style = findRelationshipStyle(view, relationship);

        String description = "";
        String technology = relationship.getTechnology();

        if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
            // do nothing - sequence diagrams don't need the order
        } else {
            if (!StringUtils.isNullOrEmpty(relationshipView.getOrder())) {
                description = relationshipView.getOrder() + ". ";
            }
        }

        description += (hasValue(relationshipView.getDescription()) ? relationshipView.getDescription() : hasValue(relationshipView.getRelationship().getDescription()) ? relationshipView.getRelationship().getDescription() : "");

        if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
            String arrowStart = "-";
            String arrowEnd = ">";

            if (relationshipView.isResponse() != null && relationshipView.isResponse() == true) {
                arrowStart = "<-";
                arrowEnd = "-";
            }

            writer.writeLine(
                    String.format("%s %s[%s]%s %s : %s%s",
                            idOf(relationship.getSource()),
                            arrowStart,
                            style.getColor(),
                            arrowEnd,
                            idOf(relationship.getDestination()),
                            description,
                            (StringUtils.isNullOrEmpty(technology) ? "" : "\\n<color:" + style.getColor() + "><size:8>[" + technology + "]</size>")));
        } else {
            boolean solid = style.getStyle() == LineStyle.Solid || false == style.getDashed();

            String arrowStart;
            String arrowEnd;
            String relationshipStyle = style.getColor();

            if (style.getThickness() != null) {
                relationshipStyle += ",thickness=" + style.getThickness();
            }

            if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
                arrowStart = solid ? "<-" : "<.";
                arrowEnd = solid ? "-" : ".";
            } else {
                arrowStart = solid ? "-" : ".";
                arrowEnd = solid ? "->" : ".>";
            }

            if (!isVisible(view, relationshipView)) {
                relationshipStyle = "hidden";
            }

            // 1 .[#rrggbb,thickness=n].> 2 : "...\n<size:8>...</size>
            writer.writeLine(format("%s %s[%s]%s %s : \"<color:%s>%s%s\"",
                    idOf(relationship.getSource()),
                    arrowStart,
                    relationshipStyle,
                    arrowEnd,
                    idOf(relationship.getDestination()),
                    style.getColor(),
                    description,
                    (StringUtils.isNullOrEmpty(technology) ? "" : "\\n<color:" + style.getColor() + "><size:8>[" + technology + "]</size>")
            ));
        }
    }

    @Override
    protected Legend createLegend(ModelView view) {
        IndentingWriter writer = new IndentingWriter();
        int id = 0;

        writer.writeLine("@startuml");
        writer.writeLine("set separator none");
        writer.writeLine();

        writer.writeLine("skinparam {");
        writer.indent();
        writer.writeLine("shadowing false");
        writer.writeLine("arrowFontSize 15");
        writer.writeLine("defaultTextAlignment center");
        writer.writeLine("wrapWidth 100");
        writer.writeLine("maxMessageSize 100");
        Font font = view.getViewSet().getConfiguration().getBranding().getFont();
        if (font != null) {
            String fontName = font.getName();
            if (!StringUtils.isNullOrEmpty(fontName)) {
                writer.writeLine("defaultFontName \"" + fontName + "\"");
            }
        }
        writer.outdent();
        writer.writeLine("}");

        writer.writeLine("hide stereotype");
        writer.writeLine();

        writer.writeLine("skinparam rectangle<<_transparent>> {");
        writer.indent();
        writer.writeLine("BorderColor transparent");
        writer.writeLine("BackgroundColor transparent");
        writer.writeLine("FontColor transparent");
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();

        Map<String,ElementStyle> elementStyles = new HashMap<>();
        List<Element> elements = view.getElements().stream().map(ElementView::getElement).collect(Collectors.toList());
        for (Element element : elements) {
            ElementStyle elementStyle = findElementStyle(view, element);

            if (element instanceof DeploymentNode) {
                // deployment node backgrounds are always white
                elementStyle.setBackground("#ffffff");
            }

            if (!StringUtils.isNullOrEmpty(elementStyle.getTag()) ) {
                elementStyles.put(elementStyle.getTag(), elementStyle);
            };
        }

        List<ElementStyle> sortedElementStyles = elementStyles.values().stream().sorted(Comparator.comparing(ElementStyle::getTag)).collect(Collectors.toList());;
        for (ElementStyle elementStyle : sortedElementStyles) {
            id++;
            Shape shape = elementStyle.getShape();
            String type = plantUMLShapeOf(elementStyle.getShape());
            if ("actor".equals(type)) {
                type = "rectangle"; // the actor shape is not supported in this implementation
            }

            String background = elementStyle.getBackground();
            String stroke = elementStyle.getStroke();
            String color = elementStyle.getColor();

            if (view instanceof DynamicView && renderAsSequenceDiagram(view)) {
                type = "sequenceParticipant";
            }

            writer.writeLine(format("skinparam %s<<%s>> {", type, id));
            writer.indent();
            writer.writeLine(String.format("BackgroundColor %s", background));
            writer.writeLine(String.format("FontColor %s", color));
            writer.writeLine(String.format("BorderColor %s", stroke));

            if (shape == Shape.RoundedBox) {
                writer.writeLine("roundCorner 20");
            }
            writer.outdent();
            writer.writeLine("}");

            String description = elementStyle.getTag();
            if (description.startsWith("Element,")) {
                description = description.substring("Element,".length());
            }
            description = description.replaceAll(",", ", ");

            String icon = "";
            if (elementStyleHasSupportedIcon(elementStyle)) {
                double scale = calculateIconScale(elementStyle.getIcon());
                icon = "\\n\\n<img:" + elementStyle.getIcon() + "{scale=" + scale + "}>";
            }

            writer.writeLine(format("%s \"==%s%s\" <<%s>>",
                    type,
                    description,
                    icon,
                    id)
            );
            writer.writeLine();
        }

        Map<String,RelationshipStyle> relationshipStyles = new HashMap<>();
        List<Relationship> relationships = view.getRelationships().stream().map(RelationshipView::getRelationship).collect(Collectors.toList());
        for (Relationship relationship : relationships) {
            RelationshipStyle relationshipStyle = findRelationshipStyle(view, relationship);

            if (!StringUtils.isNullOrEmpty(relationshipStyle.getTag())) {
                relationshipStyles.put(relationshipStyle.getTag(), relationshipStyle);
            }
        }

        List<RelationshipStyle> sortedRelationshipStyles = relationshipStyles.values().stream().sorted(Comparator.comparing(RelationshipStyle::getTag)).collect(Collectors.toList());;
        for (RelationshipStyle relationshipStyle : sortedRelationshipStyles) {
            id++;

            String description = relationshipStyle.getTag();
            if (description.startsWith("Relationship,")) {
                description = description.substring("Relationship,".length());
            }
            description = description.replaceAll(",", ", ");

            writer.writeLine(format("rectangle \".\" <<_transparent>> as %s", id));

            boolean solid = relationshipStyle.getStyle() == LineStyle.Solid || false == relationshipStyle.getDashed();

            String arrowStart = solid ? "-" : ".";
            String arrowEnd = solid ? "->" : ".>";
            String buf = relationshipStyle.getColor();

            if (relationshipStyle.getThickness() != null) {
                buf += ",thickness=" + relationshipStyle.getThickness();
            }

            // 1 .[#rrggbb,thickness=n].> 2 : "..."
            writer.writeLine(format("%s %s[%s]%s %s : \"<color:%s>%s\"",
                    id,
                    arrowStart,
                    buf,
                    arrowEnd,
                    id,
                    relationshipStyle.getColor(),
                    description)
            );

            writer.writeLine();
        }

        writer.writeLine();

        writer.writeLine("@enduml");

        return new Legend(writer.toString());
    }

    protected boolean renderAsSequenceDiagram(ModelView view) {
        return view instanceof DynamicView && "true".equalsIgnoreCase(getViewOrViewSetProperty(view, PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "false"));
    }

    protected boolean renderAsTeozDiagram(ModelView view) {
        return view instanceof DynamicView && "true".equalsIgnoreCase(getViewOrViewSetProperty(view, PLANTUML_TEOZ_PROPERTY, "false"));
    }

}
