package com.structurizr.export.plantuml;

import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.export.Legend;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.*;

import static java.lang.String.format;

public class StructurizrPlantUMLExporter extends AbstractPlantUMLExporter {

    public static final String PLANTUML_SHADOW = "plantuml.shadow";

    private static final int DEFAULT_STROKE_WIDTH = 2;
    private static final double METADATA_FONT_SIZE_RATIO = 0.7;
    private static final int MAX_ICON_SIZE_RATIO = 3;

    private Set<PlantUMLStyle> plantUMLStyles;

    public StructurizrPlantUMLExporter() {
        this(ColorScheme.Light);
    }

    public StructurizrPlantUMLExporter(ColorScheme colorScheme) {
        super(colorScheme);
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        plantUMLStyles = new HashSet<>();
        super.writeHeader(view, writer);

        if (renderAsSequenceDiagram(view)) {
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
        }

        writer.writeLine("hide stereotype");
        writer.writeLine();

        writer.writeLine("<style></style>");
        writer.writeLine();

        String fontName = null;
        Font font = view.getViewSet().getConfiguration().getBranding().getFont();
        if (font != null) {
            fontName = font.getName();
            if (!StringUtils.isNullOrEmpty(fontName)) {
                writer.writeLine("FontName: " + fontName);
            }
        }
        if (colorScheme == ColorScheme.Dark) {
            plantUMLStyles.add(new PlantUMLRootStyle(
                    Styles.DEFAULT_BACKGROUND_DARK,
                    Styles.DEFAULT_COLOR_DARK,
                    fontName));
        } else {
            plantUMLStyles.add(new PlantUMLRootStyle(
                    Styles.DEFAULT_BACKGROUND_LIGHT,
                    Styles.DEFAULT_COLOR_LIGHT,
                    fontName));
        }

        writeIncludes(view, writer);
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        super.writeFooter(view, writer);
        writeStyles(writer);
    }

    private void writeStyles(IndentingWriter writer) {
        StringBuilder styles = new StringBuilder();
        List<PlantUMLStyle> sortedStyles = plantUMLStyles.stream().sorted(Comparator.comparing(PlantUMLStyle::getName)).toList();

        sortedStyles.stream().filter(style -> style instanceof PlantUMLRootStyle).forEach(style -> styles.append(style.toString()));
        sortedStyles.stream().filter(style -> style instanceof PlantUMLElementStyle).forEach(style -> styles.append(style.toString()));
        sortedStyles.stream().filter(style -> style instanceof PlantUMLRelationshipStyle).forEach(style -> styles.append(style.toString()));
        sortedStyles.stream().filter(style -> style instanceof PlantUMLBoundaryStyle).forEach(style -> styles.append(style.toString()));
        sortedStyles.stream().filter(style -> style instanceof PlantUMLGroupStyle).forEach(style -> styles.append(style.toString()));
        sortedStyles.stream().filter(style -> style instanceof PlantUMLLegendStyle).forEach(style -> styles.append(style.toString()));

        writer.replace("<style></style>", "<style>\n" + styles + "</style>");
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
        String groupName = group;

        String groupSeparator = view.getModel().getProperties().get(GROUP_SEPARATOR_PROPERTY_NAME);
        if (!StringUtils.isNullOrEmpty(groupSeparator)) {
            groupName = group.substring(group.lastIndexOf(groupSeparator) + groupSeparator.length());
        }

        ElementStyle elementStyle = findGroupStyle(view, group);
        PlantUMLGroupStyle plantUMLBoundaryStyle = new PlantUMLGroupStyle(
                group,
                elementStyle.getBackground(),
                elementStyle.getColor(),
                elementStyle.getStroke(),
                elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH,
                elementStyle.getBorder(),
                elementStyle.getFontSize(),
                "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"))
        );
        plantUMLStyles.add(plantUMLBoundaryStyle);

        if (!renderAsSequenceDiagram(view)) {
            String icon = elementStyle.getIcon();
            if (!StringUtils.isNullOrEmpty(icon)) {
                double scale = calculateIconScale(icon, elementStyle.getFontSize() * MAX_ICON_SIZE_RATIO);
                icon = "\\n\\n<img:" + icon + "{scale=" + scale + "}>";
            } else {
                icon = "";
            }

            writer.writeLine(
                    String.format(
                            "rectangle \"%s%s\" <<%s>> as group%s {",
                            groupName,
                            icon,
                            classSelectorForGroup(group),
                            Base64.getEncoder().encodeToString(group.getBytes()))
            );
            writer.indent();
        }
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            writer.outdent();
            writer.writeLine("}");
            writer.writeLine();
        }
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        if (!renderAsSequenceDiagram(view)) {
            ElementStyle elementStyle = findBoundaryStyle(view, softwareSystem);
            PlantUMLBoundaryStyle plantUMLBoundaryStyle = new PlantUMLBoundaryStyle(
                    softwareSystem.getName(),
                    elementStyle.getBackground(),
                    elementStyle.getColor(),
                    elementStyle.getStroke(),
                    elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH,
                    elementStyle.getBorder(),
                    elementStyle.getFontSize(),
                    "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"))
            );
            plantUMLStyles.add(plantUMLBoundaryStyle);

            writer.writeLine(
                    String.format(
                            "rectangle \"%s\\n<size:%s>%s</size>\" <<%s>> {",
                            softwareSystem.getName(),
                            calculateMetadataFontSize(elementStyle.getFontSize()),
                            typeOf(view, softwareSystem, true),
                            plantUMLBoundaryStyle.getClassSelector()
                    )
                );
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
            ElementStyle elementStyle = findBoundaryStyle(view, container);
            PlantUMLBoundaryStyle plantUMLBoundaryStyle = new PlantUMLBoundaryStyle(
                    container.getName(),
                    elementStyle.getBackground(),
                    elementStyle.getColor(),
                    elementStyle.getStroke(),
                    elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH,
                    elementStyle.getBorder(),
                    elementStyle.getFontSize(),
                    "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"))
            );
            plantUMLStyles.add(plantUMLBoundaryStyle);

            writer.writeLine(
                    String.format(
                            "rectangle \"%s\\n<size:%s>%s</size>\" <<%s>> {",
                            container.getName(),
                            calculateMetadataFontSize(findBoundaryStyle(view, container).getFontSize()),                            typeOf(view, container, true),
                            plantUMLBoundaryStyle.getClassSelector()));
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

        PlantUMLElementStyle plantUMLElementStyle = new PlantUMLElementStyle(
                elementStyle.getTag(),
                elementStyle.getShape(),
                elementStyle.getWidth(),
                elementStyle.getBackground(),
                elementStyle.getColor(),
                elementStyle.getStroke(),
                elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH,
                elementStyle.getBorder(),
                elementStyle.getFontSize(),
                elementStyle.getIcon(),
                "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"))
        );
        plantUMLStyles.add(plantUMLElementStyle);

        String icon = "";
        if (isSupportedIcon(elementStyle.getIcon())) {
            double scale = calculateIconScale(elementStyle.getIcon(), elementStyle.getFontSize() * MAX_ICON_SIZE_RATIO);
            icon = "\\n\\n<img:" + elementStyle.getIcon() + "{scale=" + scale + "}>";
        }

        String url = deploymentNode.getUrl();
        if (!StringUtils.isNullOrEmpty(url)) {
            url = " [[" + url + "]]";
        } else {
            url = "";
        }

        writer.writeLine(
                format(
                        "rectangle \"%s\\n<size:%s>%s</size>%s\" <<%s>> as %s%s {",
                        deploymentNode.getName() + (!"1".equals(deploymentNode.getInstances()) ? " (x" + deploymentNode.getInstances() + ")" : ""),
                        calculateMetadataFontSize(findElementStyle(view, deploymentNode).getFontSize()),
                        typeOf(view, deploymentNode, true),
                        icon,
                        classSelectorFor(elementStyle),
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
        if (renderAsSequenceDiagram(view)) {
            IndentingWriter writer = new IndentingWriter();
            writeHeader(view, writer);

            Set<Element> elements = new LinkedHashSet<>();
            for (RelationshipView relationshipView : view.getRelationships()) {
                elements.add(relationshipView.getRelationship().getSource());
                elements.add(relationshipView.getRelationship().getDestination());
            }

            for (Element element : elements) {
                writeElement(view, element, writer);
            }

            if (!elements.isEmpty()) {
                writer.writeLine();
            }

            writeRelationships(view, writer);
            writeFooter(view, writer);

            Diagram diagram = createDiagram(view, writer.toString());
            diagram.setLegend(createLegend(view));

            return diagram;
        } else {
            return super.export(view);
        }
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        ElementStyle elementStyle = findElementStyle(view, element);
        String sequenceDiagramShape = plantumlSequenceType(view, element);

        if (renderAsSequenceDiagram(view)) {
            // actor and database require special treatment because the label sits outside the shape
            if ("actor".equals(sequenceDiagramShape) || "database".equals(sequenceDiagramShape)) {
                elementStyle.color(elementStyle.getStroke());
            }
        }

        PlantUMLElementStyle plantUMLElementStyle = new PlantUMLElementStyle(
                elementStyle.getTag(),
                elementStyle.getShape(),
                elementStyle.getWidth(),
                elementStyle.getBackground(),
                elementStyle.getColor(),
                elementStyle.getStroke(),
                renderAsSequenceDiagram(view) ? DEFAULT_STROKE_WIDTH : elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH,
                elementStyle.getBorder(),
                elementStyle.getFontSize(),
                elementStyle.getIcon(),
                "true".equalsIgnoreCase(elementStyle.getProperties().getOrDefault(PLANTUML_SHADOW, "false"))
        );
        plantUMLStyles.add(plantUMLElementStyle);

        int metadataFontSize = calculateMetadataFontSize(elementStyle.getFontSize());

        if (renderAsSequenceDiagram(view)) {
            writer.writeLine(String.format("%s \"%s\\n<size:%s>%s</size>\" as %s <<%s>>",
                    sequenceDiagramShape,
                    element.getName(),
                    metadataFontSize,
                    typeOf(view, element, true),
                    idOf(element),
                    plantUMLElementStyle.getClassSelector()
            ));
        } else {
            String shape = plantUMLShapeOf(view, element);
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
                type = String.format("\\n<size:%s>%s</size>", metadataFontSize, type);
            }

            if (isSupportedIcon(elementStyle.getIcon())) {
                double scale = calculateIconScale(elementStyle.getIcon(), elementStyle.getFontSize() * MAX_ICON_SIZE_RATIO);
                icon = "\\n\\n<img:" + elementStyle.getIcon() + "{scale=" + scale + "}>";
            }

            String classSelector = plantUMLElementStyle.getClassSelector();
            String id = idOf(element);

            writer.writeLine(format("%s \"==%s%s%s%s\" <<%s>> as %s%s",
                    shape,
                    name,
                    type,
                    icon,
                    description,
                    classSelector,
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

        PlantUMLRelationshipStyle plantUMLRelationshipStyle = new PlantUMLRelationshipStyle(
                style.getTag(),
                style.getColor(),
                style.getStyle(),
                renderAsSequenceDiagram(view) ? DEFAULT_STROKE_WIDTH : style.getThickness(),
                style.getFontSize()
        );
        plantUMLStyles.add(plantUMLRelationshipStyle);

        int metadataFontSize = calculateMetadataFontSize(style.getFontSize());
        String description = "";
        String technology = relationship.getTechnology();

        if (!StringUtils.isNullOrEmpty(relationshipView.getOrder())) {
            description = relationshipView.getOrder() + ": ";
        }

        description += (hasValue(relationshipView.getDescription()) ? relationshipView.getDescription() : hasValue(relationshipView.getRelationship().getDescription()) ? relationshipView.getRelationship().getDescription() : "");

        if (renderAsSequenceDiagram(view)) {
            String arrowStart = "-";
            String arrowEnd = ">";

            if (relationshipView.isResponse() != null && relationshipView.isResponse() == true) {
                arrowStart = "<-";
                arrowEnd = "-";
            }

            writer.writeLine(
                    String.format("%s %s%s %s <<%s>> : %s%s",
                            idOf(relationship.getSource()),
                            arrowStart,
                            arrowEnd,
                            idOf(relationship.getDestination()),
                            plantUMLRelationshipStyle.getClassSelector(),
                            description,
                            (StringUtils.isNullOrEmpty(technology) ? "" : "\\n<size:" + metadataFontSize + ">[" + technology + "]</size>")));
        } else {
            String arrow;

            if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
                arrow = "<--";
            } else {
                arrow = "-->";
            }

            // 1 --> 2 : "...\n<size:..>...</size>
            writer.writeLine(format("%s %s %s <<%s>> : \"%s%s\"",
                    idOf(relationship.getSource()),
                    arrow,
                    idOf(relationship.getDestination()),
                    plantUMLRelationshipStyle.getClassSelector(),
                    description,
                    (StringUtils.isNullOrEmpty(technology) ? "" : "\\n<size:" + metadataFontSize + ">[" + technology + "]</size>")
            ));
        }
    }

    @Override
    protected Legend createLegend(ModelView view) {
        IndentingWriter writer = new IndentingWriter();

        writer.writeLine("@startuml");
        writer.writeLine();
        writer.writeLine("set separator none");
        writer.writeLine("hide stereotype");
        writer.writeLine();

        writer.writeLine("<style></style>");
        writer.writeLine();

        plantUMLStyles.stream().sorted(Comparator.comparing(PlantUMLStyle::getName)).filter(style -> style instanceof PlantUMLElementStyle).map(style -> (PlantUMLElementStyle)style).forEach(style -> {
            style.setWidth(200);
            String description = style.getName();
            if (description.startsWith("Element,")) {
                description = description.substring("Element,".length());
            }
            description = description.replaceAll(",", ", ");

            String icon = "";
            if (isSupportedIcon(style.getIcon())) {
                double scale = calculateIconScale(style.getIcon(), style.getFontSize() * MAX_ICON_SIZE_RATIO);
                icon = "\\n\\n<img:" + style.getIcon() + "{scale=" + scale + "}>";
            }

            writer.writeLine(format("%s \"==%s%s\" <<%s>>",
                    plantUMLShapeOf(style.getShape()),
                    description,
                    icon,
                    style.getClassSelector())
            );
            writer.writeLine();
        });

        int id = 0;
        List<PlantUMLRelationshipStyle> relationshipStyles = plantUMLStyles.stream().sorted(Comparator.comparing(PlantUMLStyle::getName)).filter(style -> style instanceof PlantUMLRelationshipStyle).map(style -> (PlantUMLRelationshipStyle)style).toList();
        for (PlantUMLRelationshipStyle relationshipStyle : relationshipStyles) {
            id++;
            String description = relationshipStyle.getName();
            if (description.startsWith("Relationship,")) {
                description = description.substring("Relationship,".length());
            }
            description = description.replaceAll(",", ", ");

            // id --> id : "..."
            writer.writeLine(format("rectangle \".\" <<.Element-Transparent>> as %s", id));
            writer.writeLine(format("%s --> %s <<%s>> : \"%s\"",
                    id,
                    id,
                    relationshipStyle.getClassSelector(),
                    description)
            );

            writer.writeLine();
        };

        writer.writeLine("@enduml");

        plantUMLStyles.add(new PlantUMLLegendStyle());
        writeStyles(writer);

        return new Legend(writer.toString());
    }

    protected boolean renderAsSequenceDiagram(ModelView view) {
        return view instanceof DynamicView && "true".equalsIgnoreCase(getViewOrViewSetProperty(view, PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "false"));
    }

    private String classSelectorFor(ElementStyle elementStyle) {
        return "Element-" + Base64.getEncoder().encodeToString(elementStyle.getTag().getBytes());
    }

    private String classSelectorForBoundary(Element element) {
        return "Boundary-" + Base64.getEncoder().encodeToString(element.getName().getBytes());
    }

    private ElementStyle findBoundaryStyle(ModelView view, Element element) {
        String background = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_BACKGROUND_DARK : Styles.DEFAULT_BACKGROUND_LIGHT;
        String stroke = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_COLOR_DARK : Styles.DEFAULT_COLOR_LIGHT;
        int strokeWidth = DEFAULT_STROKE_WIDTH;
        Border border = Border.Dotted;
        String color = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_COLOR_DARK : Styles.DEFAULT_COLOR_LIGHT;
        String icon = "";
        int fontSize = DEFAULT_FONT_SIZE;

        String type = element instanceof SoftwareSystem ? "SoftwareSystem" : "Container";

        ElementStyle style = new ElementStyle("");
        ElementStyle elementStyleForBoundary = findElementStyle(view, "Boundary:" + type);
        ElementStyle elementStyleForAllBoundaries = findElementStyle(view, "Boundary");
        ElementStyle elementStyleForElement = findElementStyle(view, element);

        if (elementStyleForBoundary != null && !StringUtils.isNullOrEmpty(elementStyleForBoundary.getBackground())) {
            background = elementStyleForBoundary.getBackground();
        } else if (elementStyleForAllBoundaries != null && !StringUtils.isNullOrEmpty(elementStyleForAllBoundaries.getBackground())) {
            background = elementStyleForAllBoundaries.getBackground();
        }
        style.setBackground(background);

        if (elementStyleForBoundary != null && !StringUtils.isNullOrEmpty(elementStyleForBoundary.getStroke())) {
            stroke = elementStyleForBoundary.getStroke();
        } else if (elementStyleForAllBoundaries != null && !StringUtils.isNullOrEmpty(elementStyleForAllBoundaries.getStroke())) {
            stroke = elementStyleForAllBoundaries.getStroke();
        } else if (!StringUtils.isNullOrEmpty(elementStyleForElement.getStroke())) {
            stroke = elementStyleForElement.getStroke();
        }
        style.setStroke(stroke);

        if (elementStyleForBoundary != null && elementStyleForBoundary.getStrokeWidth() != null) {
            strokeWidth = elementStyleForBoundary.getStrokeWidth();
        } else if (elementStyleForAllBoundaries != null && elementStyleForAllBoundaries.getStrokeWidth() != null) {
            strokeWidth = elementStyleForAllBoundaries.getStrokeWidth();
        } else if (elementStyleForElement.getStrokeWidth() != null) {
            strokeWidth = elementStyleForElement.getStrokeWidth();
        }
        style.setStrokeWidth(strokeWidth);

        if (elementStyleForBoundary != null && !StringUtils.isNullOrEmpty(elementStyleForBoundary.getColor())) {
            color = elementStyleForBoundary.getColor();
        } else if (elementStyleForAllBoundaries != null && !StringUtils.isNullOrEmpty(elementStyleForAllBoundaries.getColor())) {
            color = elementStyleForAllBoundaries.getColor();
        } else if (!StringUtils.isNullOrEmpty(elementStyleForElement.getColor())) {
            color = elementStyleForElement.getColor();
        }
        style.setColor(color);

        if (elementStyleForBoundary != null && elementStyleForBoundary.getBorder() != null) {
            border = elementStyleForBoundary.getBorder();
        } else if (elementStyleForAllBoundaries != null && elementStyleForAllBoundaries.getBorder() != null) {
            border = elementStyleForAllBoundaries.getBorder();
        } else if (elementStyleForElement.getBorder() != null) {
            border = elementStyleForElement.getBorder();
        }
        style.setBorder(border);

        if (elementStyleForBoundary != null && isSupportedIcon(elementStyleForBoundary.getIcon())) {
            icon = elementStyleForBoundary.getIcon();
        } else if (elementStyleForAllBoundaries != null && isSupportedIcon(elementStyleForAllBoundaries.getIcon())) {
            icon = elementStyleForAllBoundaries.getIcon();
        } else if (isSupportedIcon(elementStyleForElement.getIcon())) {
            icon = elementStyleForElement.getIcon();
        }
        style.setIcon(icon);

        if (elementStyleForBoundary != null && elementStyleForBoundary.getFontSize() != null) {
            fontSize = elementStyleForBoundary.getFontSize();
        } else if (elementStyleForAllBoundaries != null && elementStyleForAllBoundaries.getFontSize() != null) {
            fontSize = elementStyleForAllBoundaries.getFontSize();
        } else if (elementStyleForElement.getFontSize() != null) {
            fontSize = elementStyleForElement.getFontSize();
        }
        style.setFontSize(fontSize);

        return style;
    }

    private String classSelectorForGroup(String group) {
        return "Group-" + Base64.getEncoder().encodeToString(group.getBytes());
    }

    private ElementStyle findGroupStyle(ModelView view, String group) {
        String background = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_BACKGROUND_DARK : Styles.DEFAULT_BACKGROUND_LIGHT;
        String stroke = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_COLOR_DARK : Styles.DEFAULT_COLOR_LIGHT;
        int strokeWidth = DEFAULT_STROKE_WIDTH;
        Border border = Border.Dotted;
        String color = colorScheme == ColorScheme.Dark ? Styles.DEFAULT_COLOR_DARK : Styles.DEFAULT_COLOR_LIGHT;
        String icon = "";
        int fontSize = DEFAULT_FONT_SIZE;

        ElementStyle style = new ElementStyle("");
        ElementStyle elementStyleForGroup = findElementStyle(view, "Group:" + group);
        ElementStyle elementStyleForAllGroups = findElementStyle(view, "Group");

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getBackground())) {
            background = elementStyleForGroup.getBackground();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getBackground())) {
            background = elementStyleForAllGroups.getBackground();
        }
        style.setBackground(background);

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getStroke())) {
            stroke = elementStyleForGroup.getStroke();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getStroke())) {
            stroke = elementStyleForAllGroups.getStroke();
        }
        style.setStroke(stroke);

        if (elementStyleForGroup != null && elementStyleForGroup.getStrokeWidth() != null) {
            strokeWidth = elementStyleForGroup.getStrokeWidth();
        } else if (elementStyleForAllGroups != null && elementStyleForAllGroups.getStrokeWidth() != null) {
            strokeWidth = elementStyleForAllGroups.getStrokeWidth();
        }
        style.setStrokeWidth(strokeWidth);

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getColor())) {
            color = elementStyleForGroup.getColor();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getColor())) {
            color = elementStyleForAllGroups.getColor();
        }
        style.setColor(color);

        if (elementStyleForGroup != null && elementStyleForGroup.getBorder() != null) {
            border = elementStyleForGroup.getBorder();
        } else if (elementStyleForAllGroups != null && elementStyleForAllGroups.getBorder() != null) {
            border = elementStyleForAllGroups.getBorder();
        }
        style.setBorder(border);

        if (elementStyleForGroup != null && isSupportedIcon(elementStyleForGroup.getIcon())) {
            icon = elementStyleForGroup.getIcon();
        } else if (elementStyleForAllGroups != null && isSupportedIcon(elementStyleForAllGroups.getIcon())) {
            icon = elementStyleForAllGroups.getColor();
        }
        style.setIcon(icon);

        if (elementStyleForGroup != null && elementStyleForGroup.getFontSize() != null) {
            fontSize = elementStyleForGroup.getFontSize();
        } else if (elementStyleForAllGroups != null && elementStyleForAllGroups.getFontSize() != null) {
            fontSize = elementStyleForAllGroups.getFontSize();
        }
        style.setFontSize(fontSize);

        return style;
    }

    private int calculateMetadataFontSize(int fontSize) {
        return (int)Math.floor(fontSize * METADATA_FONT_SIZE_RATIO);
    }

    private String toLineStyle(ElementStyle elementStyle) {
        int strokeWidth = elementStyle.getStrokeWidth() != null ? elementStyle.getStrokeWidth() : DEFAULT_STROKE_WIDTH;
        switch (elementStyle.getBorder()) {
            case Dotted:
                return (strokeWidth * 1) + "-" + (strokeWidth * 1);
            case Dashed:
                return (strokeWidth * 5) + "-" + (strokeWidth * 5);
            default:
                return "0";
        }
    }

}