package com.structurizr.export.plantuml;

import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class C4PlantUMLExporter extends AbstractPlantUMLExporter {

    private static final String STRUCTURIZR_PROPERTY_NAME = "structurizr.";

    public static final String C4PLANTUML_LEGEND_PROPERTY = "c4plantuml.legend";
    public static final String C4PLANTUML_STEREOTYPES_PROPERTY = "c4plantuml.stereotypes";
    public static final String C4PLANTUML_TAGS_PROPERTY = "c4plantuml.tags";
    public static final String C4PLANTUML_STANDARD_LIBRARY_PROPERTY = "c4plantuml.stdlib";
    public static final String C4PLANTUML_SPRITE = "c4plantuml.sprite";
    public static final String C4PLANTUML_SHADOW = "c4plantuml.shadow";

    /**
     * <p>Set this property to <code>true</code> by calling {@link Configuration#addProperty(String, String)} in your
     * {@link ViewSet} in order to have all {@link ModelItem#getProperties()} for {@link Component}s
     * being printed in the PlantUML diagrams.</p>
     *
     * <p>The default value is <code>false</code>.</p>
     *
     * @see ViewSet#getConfiguration()
     * @see Configuration#getProperties()
     */
    public static final String C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY = "c4plantuml.elementProperties";

    /**
     * <p>Set this property to <code>true</code> by calling {@link Configuration#addProperty(String, String)} in your
     * {@link ViewSet} in order to have all {@link ModelItem#getProperties()} for {@link Relationship}s being
     * printed in the PlantUML diagrams.</p>
     *
     * <p>The default value is <code>false</code>.</p>
     *
     * @see ViewSet#getConfiguration()
     * @see Configuration#getProperties()
     */
    public static final String C4PLANTUML_RELATIONSHIP_PROPERTIES_PROPERTY = "c4plantuml.relationshipProperties";

    private int groupId = 0;

    public C4PlantUMLExporter() {
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        super.writeHeader(view, writer);
        groupId = 0;

        Font font = view.getViewSet().getConfiguration().getBranding().getFont();
        if (font != null) {
            String fontName = font.getName();
            if (!StringUtils.isNullOrEmpty(fontName)) {
                addSkinParam("defaultFontName", "\"" + fontName + "\"");
            }
        }

        writeSkinParams(writer);

        if (renderAsSequenceDiagram(view)) {
            if (usePlantUMLStandardLibrary(view)) {
                writer.writeLine("!include <C4/C4_Sequence>");
            } else {
                writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Sequence.puml");
            }
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
            } else {
                writer.writeLine("top to bottom direction");
            }

            writer.writeLine();

            if (usePlantUMLStandardLibrary(view)) {
                writer.writeLine("!include <C4/C4>");
                writer.writeLine("!include <C4/C4_Context>");
            } else {
                writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4.puml");
                writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml");
            }

            if (view.getElements().stream().map(ElementView::getElement).anyMatch(e -> e instanceof Container || e instanceof ContainerInstance)) {
                if (usePlantUMLStandardLibrary(view)) {
                    writer.writeLine("!include <C4/C4_Container>");
                } else {
                    writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml");
                }
            }

            if (view.getElements().stream().map(ElementView::getElement).anyMatch(e -> e instanceof Component)) {
                if (usePlantUMLStandardLibrary(view)) {
                    writer.writeLine("!include <C4/C4_Component>");
                } else {
                    writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml");
                }
            }

            if (view instanceof DeploymentView) {
                if (usePlantUMLStandardLibrary(view)) {
                    writer.writeLine("!include <C4/C4_Deployment>");
                } else {
                    writer.writeLine("!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Deployment.puml");
                }
            }
        }

        writeIncludes(view, writer);

        if (includeTags(view)) {
            Map<String,ElementStyle> elementStyles = new HashMap<>();
            Map<String,RelationshipStyle> relationshipStyles = new HashMap<>();
            Map<String,ElementStyle> boundaryStyles = new HashMap<>();

            // elements
            for (ElementView elementView : view.getElements()) {
                Element element = elementView.getElement();
                ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);

                elementStyles.put(elementStyle.getTag(), elementStyle);
            }

            // relationships
            for (RelationshipView relationshipView : view.getRelationships()) {
                Relationship relationship = relationshipView.getRelationship();
                RelationshipStyle relationshipStyle = view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship);

                relationshipStyles.put(relationshipStyle.getTag(), relationshipStyle);
            }

            if (renderAsSequenceDiagram(view)) {
                // no boundaries, do nothing
            } else {
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
                    boundaryStyles.put(elementStyle.getTag(), elementStyle);
                }
            }

            if (!elementStyles.isEmpty()) {
                writer.writeLine();

                for (String tagList : elementStyles.keySet()) {
                    ElementStyle elementStyle = elementStyles.get(tagList);
                    tagList = tagList.replaceFirst("Element,", "");

                    String sprite = "";
                    if (elementStyleHasSupportedIcon(elementStyle)) {
                        double scale = calculateIconScale(elementStyle.getIcon());
                        sprite = "img:" + elementStyle.getIcon() + "{scale=" + scale + "}";
                    }
                    sprite = elementStyle.getProperties().getOrDefault(C4PLANTUML_SPRITE, sprite);

                    int borderThickness = 1;
                    if (elementStyle.getStrokeWidth() != null) {
                        borderThickness = elementStyle.getStrokeWidth();
                    }

                    writer.writeLine(String.format("AddElementTag(\"%s\", $bgColor=\"%s\", $borderColor=\"%s\", $fontColor=\"%s\", $sprite=\"%s\", $shadowing=\"%s\", $borderStyle=\"%s\", $borderThickness=\"%s\")",
                            tagList,
                            elementStyle.getBackground(),
                            elementStyle.getStroke(),
                            elementStyle.getColor(),
                            sprite,
                            elementStyle.getProperties().getOrDefault(C4PLANTUML_SHADOW, ""),
                            elementStyle.getBorder(),
                            borderThickness
                    ));
                }
            }

            if (!relationshipStyles.isEmpty()) {
                writer.writeLine();

                for (String tagList : relationshipStyles.keySet()) {
                    RelationshipStyle relationshipStyle = relationshipStyles.get(tagList);
                    tagList = tagList.replaceFirst("Relationship,", "");

                    String lineStyle = "\"\"";
                    if (relationshipStyle.getStyle() == LineStyle.Dashed) {
                        lineStyle = "DashedLine()";
                    } else if (relationshipStyle.getStyle() == LineStyle.Dotted) {
                        lineStyle = "DottedLine()";
                    }

                    writer.writeLine(String.format("AddRelTag(\"%s\", $textColor=\"%s\", $lineColor=\"%s\", $lineStyle = %s)",
                            tagList,
                            relationshipStyle.getColor(),
                            relationshipStyle.getColor(),
                            lineStyle
                    ));
                }
            }

            if (!boundaryStyles.isEmpty()) {
                writer.writeLine();

                for (String tagList : boundaryStyles.keySet()) {
                    ElementStyle elementStyle = boundaryStyles.get(tagList);
                    tagList = tagList.replaceFirst("Element,", "");

                    int borderThickness = 1;
                    if (elementStyle.getStrokeWidth() != null) {
                        borderThickness = elementStyle.getStrokeWidth();
                    }

                    writer.writeLine(String.format("AddBoundaryTag(\"%s\", $bgColor=\"%s\", $borderColor=\"%s\", $fontColor=\"%s\", $shadowing=\"%s\", $borderStyle=\"%s\", $borderThickness=\"%s\")",
                            tagList,
                            "#ffffff",
                            elementStyle.getStroke(),
                            elementStyle.getStroke(),
                            elementStyle.getProperties().getOrDefault(C4PLANTUML_SHADOW, ""),
                            elementStyle.getBorder(),
                            borderThickness
                    ));
                }
            }
        }

        writer.writeLine();
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        if (includeLegend(view)) {
            writer.writeLine();
            writer.writeLine("SHOW_LEGEND(" + !(includeStereotypes(view)) + ")");
        } else {
            writer.writeLine();
            writer.writeLine((includeStereotypes(view) ? "show" : "hide") + " stereotypes");
        }

        super.writeFooter(view, writer);
    }

    @Override
    protected void startEnterpriseBoundary(ModelView view, String enterpriseName, IndentingWriter writer) {
        writer.writeLine(String.format("Enterprise_Boundary(enterprise, \"%s\") {", enterpriseName));
        writer.indent();
    }

    @Override
    protected void endEnterpriseBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
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
        String borderStyle = "Dashed";
        int borderThickness = 1;
//        String icon = "";

        ElementStyle elementStyleForGroup = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group:" + group);
        ElementStyle elementStyleForAllGroups = view.getViewSet().getConfiguration().getStyles().findElementStyle("Group");

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getColor())) {
            color = elementStyleForGroup.getColor();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getColor())) {
            color = elementStyleForAllGroups.getColor();
        }

        if (elementStyleForGroup != null && !StringUtils.isNullOrEmpty(elementStyleForGroup.getStroke())) {
            borderStyle = elementStyleForGroup.getStroke();
        } else if (elementStyleForAllGroups != null && !StringUtils.isNullOrEmpty(elementStyleForAllGroups.getStroke())) {
            borderStyle = elementStyleForAllGroups.getStroke();
        }

        if (elementStyleForGroup != null && elementStyleForGroup.getStrokeWidth() != null) {
            borderThickness = elementStyleForGroup.getStrokeWidth();
        } else if (elementStyleForAllGroups != null && elementStyleForAllGroups.getStrokeWidth() != null) {
            borderThickness = elementStyleForAllGroups.getStrokeWidth();
        }


// todo: $sprite doesn't seem to be supported for boundary styles
//        if (elementStyleForGroup != null && elementStyleHasSupportedIcon(elementStyleForGroup)) {
//            icon = elementStyleForGroup.getIcon();
//        } else if (elementStyleForAllGroups != null && elementStyleHasSupportedIcon(elementStyleForAllGroups)) {
//            icon = elementStyleForAllGroups.getColor();
//        }
//
//        if (!StringUtils.isNullOrEmpty(icon)) {
//            double scale = calculateIconScale(icon);
//            icon = "\\n\\n<img:" + icon + "{scale=" + scale + "}>";
//        }

        writer.writeLine(String.format("AddBoundaryTag(\"%s\", $borderColor=\"%s\", $fontColor=\"%s\", $borderStyle=\"%s\", $borderThickness=\"%s\")",
                group,
                color,
                color,
                borderStyle,
                borderThickness)
        );

        writer.writeLine(String.format("Boundary(group_%s, \"%s\", $tags=\"%s\") {", groupId, groupName, group));
        writer.indent();
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        writer.writeLine(String.format("System_Boundary(\"%s_boundary\", \"%s\", $tags=\"%s\") {", idOf(softwareSystem), softwareSystem.getName(), tagsOf(view, softwareSystem)));
        writer.indent();
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
        writer.writeLine(String.format("Container_Boundary(\"%s_boundary\", \"%s\", $tags=\"%s\") {", idOf(container), container.getName(), tagsOf(view,container)));
        writer.indent();
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        String url = deploymentNode.getUrl();
        if (StringUtils.isNullOrEmpty(url)) {
            url = "";
        }

        if (Boolean.TRUE.toString().equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, Boolean.FALSE.toString()))) {
            addProperties(view, writer, deploymentNode);
        }

        String technology = deploymentNode.getTechnology();
        if (StringUtils.isNullOrEmpty(technology)) {
            technology = "";
        }
        String description = deploymentNode.getDescription();
        if (StringUtils.isNullOrEmpty(description)) {
            description = "";
        }

        // Deployment_Node(alias, label, ?type, ?descr, ?sprite, ?tags, ?link)
        writer.writeLine(
                format("Deployment_Node(%s, \"%s\", $type=\"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\") {",
                        idOf(deploymentNode),
                        deploymentNode.getName() + (!"1".equals(deploymentNode.getInstances()) ? " (x" + deploymentNode.getInstances() + ")" : ""),
                        technology,
                        description,
                        tagsOf(view, deploymentNode),
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
    public Diagram export(CustomView view) {
        return null;
    }

    @Override
    public Diagram export(DynamicView view, String order) {
        if (renderAsSequenceDiagram(view)) {
            IndentingWriter writer = new IndentingWriter();
            writeHeader(view, writer);

            boolean elementsWritten = false;

            Set<Element> elements = new LinkedHashSet<>();
            for (RelationshipView relationshipView : view.getRelationships()) {
                elements.add(relationshipView.getRelationship().getSource());
                elements.add(relationshipView.getRelationship().getDestination());
            }

            for (Element element : elements) {
                writeElement(view, element, writer);
                elementsWritten = true;
            }

            if (elementsWritten) {
                writer.writeLine();
            }

            writeRelationships(view, writer);
            writeFooter(view, writer);

            return createDiagram(view, writer.toString());
        } else {
            return super.export(view, order);
        }
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        if (element instanceof CustomElement) {
            return;
        }

        Element elementToWrite = element;
        ElementStyle elementStyle = view.getViewSet().getConfiguration().getStyles().findElementStyle(element);
        String id = idOf(element);

        String url = element.getUrl();
        if (StringUtils.isNullOrEmpty(url)) {
            url = "";
        }

        if (Boolean.TRUE.toString().equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, Boolean.FALSE.toString()))) {
            addProperties(view, writer, element);
        }

        if (element instanceof StaticStructureElementInstance) {
            StaticStructureElementInstance elementInstance = (StaticStructureElementInstance)element;
            element = elementInstance.getElement();

            if (StringUtils.isNullOrEmpty(url)) {
                url = element.getUrl();
                if (StringUtils.isNullOrEmpty(url)) {
                    url = "";
                }
            }
        }

        String name = element.getName();
        String description = element.getDescription();

        if (StringUtils.isNullOrEmpty(description)) {
            description = "";
        }

        if (element instanceof Person) {
            Person person = (Person)element;
            String location = "";
            if (person.getLocation() == Location.External) {
                location = "_Ext";
            }

            // Person(alias, label, ?descr, ?sprite, ?tags, ?link, ?type)
            writer.writeLine(
                    String.format("Person%s(%s, \"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\")",
                        location, id, name, description, tagsOf(view, elementToWrite), url)
            );
        } else if (element instanceof SoftwareSystem) {
            SoftwareSystem softwareSystem = (SoftwareSystem)element;
            String location = "";
            if (softwareSystem.getLocation() == Location.External) {
                location = "_Ext";
            }

            // System(alias, label, ?descr, ?sprite, ?tags, ?link, ?type)
            writer.writeLine(
                    String.format("System%s(%s, \"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\")",
                        location, id, name, description, tagsOf(view, elementToWrite), url)
            );
        } else if (element instanceof Container) {
            Container container = (Container)element;
            String shape = "";
            if (elementStyle.getShape() == Shape.Cylinder) {
                shape = "Db";
            } else if (elementStyle.getShape() == Shape.Pipe) {
                shape = "Queue";
            }

            String technology = container.getTechnology();
            if (StringUtils.isNullOrEmpty(technology)) {
                technology = "";
            }

            // Container(alias, label, ?techn, ?descr, ?sprite, ?tags, ?link)
            writer.writeLine(
                    String.format("Container%s(%s, \"%s\", $techn=\"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\")",
                            shape, id, name, technology, description, tagsOf(view, elementToWrite), url)
            );
        } else if (element instanceof Component) {
            Component component = (Component)element;
            String shape = "";

            if (elementStyle.getShape() == Shape.Cylinder) {
                shape = "Db";
            } else if (elementStyle.getShape() == Shape.Pipe) {
                shape = "Queue";
            }

            String technology = component.getTechnology();
            if (StringUtils.isNullOrEmpty(technology)) {
                technology = "";
            }

            // Component(alias, label, ?techn, ?descr, ?sprite, ?tags, ?link)
            writer.writeLine(
                    String.format("Component%s(%s, \"%s\", $techn=\"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\")",
                        shape, id, name, technology, description, tagsOf(view, elementToWrite), url)
            );
        } else if (element instanceof InfrastructureNode) {
            InfrastructureNode infrastructureNode = (InfrastructureNode)element;
            String technology = infrastructureNode.getTechnology();
            if (StringUtils.isNullOrEmpty(technology)) {
                technology = "";
            }

            // Deployment_Node(alias, label, ?type, ?descr, ?sprite, ?tags, ?link)
            writer.writeLine(
                    String.format("Deployment_Node(%s, \"%s\", $type=\"%s\", $descr=\"%s\", $tags=\"%s\", $link=\"%s\")",
                        idOf(infrastructureNode), name, technology, description, tagsOf(view, elementToWrite), url)
            );
        }

        if (!isVisible(view, elementToWrite)) {
            writer.writeLine("hide " + id);
        }
    }

    private String tagsOf(ModelView view, Element element) {
        if (includeTags(view)) {
            return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getTag().replaceFirst("Element,", "");
        } else {
            return "";
        }
    }

    private String tagsOf(ModelView view, Relationship relationship) {
        if (includeTags(view)) {
            return view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship).getTag().replaceFirst("Relationship,", "");
        } else {
            return "";
        }
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        Relationship relationship = relationshipView.getRelationship();
        Element source = relationship.getSource();
        Element destination = relationship.getDestination();

        if (source instanceof CustomElement || destination instanceof CustomElement) {
            return;
        }

        if (Boolean.TRUE.toString().equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_RELATIONSHIP_PROPERTIES_PROPERTY, Boolean.FALSE.toString()))) {
            addProperties(view, writer, relationship);
        }

        if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
            source = relationship.getDestination();
            destination = relationship.getSource();
        }

        String description = "";

        if (renderAsSequenceDiagram(view)) {
            // do nothing - sequence diagrams don't need the order
        } else {
            if (!StringUtils.isNullOrEmpty(relationshipView.getOrder())) {
                description = relationshipView.getOrder() + ". ";
            }
        }

        description += (hasValue(relationshipView.getDescription()) ? relationshipView.getDescription() : hasValue(relationshipView.getRelationship().getDescription()) ? relationshipView.getRelationship().getDescription() : "");

        String technology = relationship.getTechnology();
        if (StringUtils.isNullOrEmpty(technology)) {
            technology = "";
        }

        String url = relationship.getUrl();
        if (StringUtils.isNullOrEmpty(url)) {
            url = "";
        }

        // Rel(from, to, label, ?techn, ?descr, ?sprite, ?tags, ?link)
        writer.writeLine(
                format("Rel(%s, %s, \"%s\", $techn=\"%s\", $tags=\"%s\", $link=\"%s\")",
                        idOf(source), idOf(destination), description, technology, tagsOf(view, relationship), url)
        );
    }

    private void addProperties(ModelView view, IndentingWriter writer, ModelItem element) {
        Map<String, String> properties = new HashMap<>();
        for (String key : element.getProperties().keySet()) {
            // don't include any internal Structurizr properties (e.g. structurizr.dsl.identifier)
            if (!key.startsWith(STRUCTURIZR_PROPERTY_NAME)) {
                properties.put(key, element.getProperties().get(key));
            }
        }

        if (!properties.isEmpty()) {
            writer.writeLine("WithoutPropertyHeader()");
            properties.keySet().stream().sorted().forEach(key ->
                    writer.writeLine(String.format("AddProperty(\"%s\",\"%s\")", key, properties.get(key)))
            );
        }
    }

    @Override
    protected boolean isAnimationSupported(ModelView view) {
        return !(view instanceof DynamicView) && super.isAnimationSupported(view);
    }

    protected boolean includeLegend(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_LEGEND_PROPERTY, "true"));
    }

    protected boolean includeStereotypes(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_STEREOTYPES_PROPERTY, "false"));
    }

    protected boolean includeTags(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_TAGS_PROPERTY, "false"));
    }

    protected boolean usePlantUMLStandardLibrary(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, C4PLANTUML_STANDARD_LIBRARY_PROPERTY, "true"));
    }

    protected boolean renderAsSequenceDiagram(ModelView view) {
        return view instanceof DynamicView && "true".equalsIgnoreCase(getViewOrViewSetProperty(view, PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "false"));
    }

}