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

    private static final int CLUSTER_INTERNAL_MARGIN = 25;

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
    protected void startEnterpriseBoundary(ModelView view, String enterpriseName, IndentingWriter writer) {
        writer.writeLine("subgraph cluster_enterprise {");
        writer.indent();
        writer.writeLine("margin=" + CLUSTER_INTERNAL_MARGIN);
    }

    @Override
    protected void endEnterpriseBoundary(ModelView view, IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
        writer.writeLine();
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
        writer.writeLine("subgraph \"cluster_group_" + (groupId++) + "\" {");

        writer.indent();
        writer.writeLine("margin=" + CLUSTER_INTERNAL_MARGIN);
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
        writer.writeLine("margin=" + CLUSTER_INTERNAL_MARGIN);
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
        writer.writeLine("margin=" + CLUSTER_INTERNAL_MARGIN);
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
        writer.writeLine("margin=" + CLUSTER_INTERNAL_MARGIN);
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

//    private void write(ModelView view, boolean enterpriseBoundaryIsVisible) throws Exception {
//        File file = new File(path, view.getKey() + ".dot");
//        FileWriter fileWriter = new FileWriter(file);
//        writeHeader(fileWriter, view);
//
//        if (enterpriseBoundaryIsVisible) {
//            fileWriter.write("  subgraph cluster_enterprise {\n");
//            fileWriter.write("    margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//            Set<GroupableElement> elementsInsideEnterpriseBoundary = new LinkedHashSet<>();
//            for (ElementView elementView : view.getElements()) {
//                if (elementView.getElement() instanceof Person && ((Person)elementView.getElement()).getLocation() == Location.Internal) {
//                    elementsInsideEnterpriseBoundary.add((StaticStructureElement)elementView.getElement());
//                }
//                if (elementView.getElement() instanceof SoftwareSystem && ((SoftwareSystem)elementView.getElement()).getLocation() == Location.Internal) {
//                    elementsInsideEnterpriseBoundary.add((StaticStructureElement)elementView.getElement());
//                }
//            }
//            writeElements(view, "    ", elementsInsideEnterpriseBoundary, fileWriter);
//            fileWriter.write("  }\n\n");
//
//            Set<GroupableElement> elementsOutsideEnterpriseBoundary = new LinkedHashSet<>();
//            for (ElementView elementView : view.getElements()) {
//                if (elementView.getElement() instanceof Person && ((Person)elementView.getElement()).getLocation() != Location.Internal) {
//                    elementsOutsideEnterpriseBoundary.add((StaticStructureElement)elementView.getElement());
//                }
//                if (elementView.getElement() instanceof SoftwareSystem && ((SoftwareSystem)elementView.getElement()).getLocation() != Location.Internal) {
//                    elementsOutsideEnterpriseBoundary.add((StaticStructureElement)elementView.getElement());
//                }
//                if (elementView.getElement() instanceof CustomElement) {
//                    elementsOutsideEnterpriseBoundary.add((CustomElement)elementView.getElement());
//                }
//            }
//
//            writeElements(view, "  ", elementsOutsideEnterpriseBoundary, fileWriter);
//        } else {
//            Set<GroupableElement> elements = new LinkedHashSet<>();
//            for (ElementView elementView : view.getElements()) {
//                elements.add((GroupableElement)elementView.getElement());
//            }
//            writeElements(view, "  ", elements, fileWriter);
//        }
//
//        writeRelationships(view, fileWriter);
//        writeFooter(fileWriter);
//        fileWriter.close();
//    }
//
//    void write(ContainerView view) throws Exception {
//        File file = new File(path, view.getKey() + ".dot");
//        FileWriter fileWriter = new FileWriter(file);
//        writeHeader(fileWriter, view);
//
//        Set<SoftwareSystem> softwareSystems = new HashSet<>();
//        for (ElementView elementView : view.getElements()) {
//            if (elementView.getElement().getParent() instanceof SoftwareSystem) {
//                softwareSystems.add((SoftwareSystem)elementView.getElement().getParent());
//            }
//        }
//        List<SoftwareSystem> sortedSoftwareSystems = new ArrayList<>(softwareSystems);
//        sortedSoftwareSystems.sort(Comparator.comparing(Element::getId));
//
//        for (SoftwareSystem softwareSystem : sortedSoftwareSystems) {
//            fileWriter.write(String.format(locale, "  subgraph cluster_%s {\n", softwareSystem.getId()));
//            fileWriter.write("    margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//
//            Set<GroupableElement> scopedElements = new LinkedHashSet<>();
//            for (ElementView elementView : view.getElements()) {
//                if (elementView.getElement().getParent() == softwareSystem) {
//                    scopedElements.add((StaticStructureElement) elementView.getElement());
//                }
//            }
//            writeElements(view, "    ", scopedElements, fileWriter);
//            fileWriter.write("  }\n");
//
//        }
//
//        for (ElementView elementView : view.getElements()) {
//            if (elementView.getElement().getParent() == null) {
//                writeElement(view, "  ", elementView.getElement(), fileWriter);
//            }
//        }
//
//        writeRelationships(view, fileWriter);
//
//        writeFooter(fileWriter);
//        fileWriter.close();
//    }
//
//    void write(ComponentView view) throws Exception {
//        File file = new File(path, view.getKey() + ".dot");
//        FileWriter fileWriter = new FileWriter(file);
//        writeHeader(fileWriter, view);
//
//        Set<Container> containers = new HashSet<>();
//        for (ElementView elementView : view.getElements()) {
//            if (elementView.getElement().getParent() instanceof Container) {
//                containers.add((Container)elementView.getElement().getParent());
//            }
//        }
//        List<Container> sortedContainers = new ArrayList<>(containers);
//        sortedContainers.sort(Comparator.comparing(Element::getId));
//
//        for (Container container : sortedContainers) {
//            fileWriter.write(String.format(locale, "  subgraph cluster_%s {\n", container.getId()));
//            fileWriter.write("    margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//
//            Set<GroupableElement> scopedElements = new LinkedHashSet<>();
//            for (ElementView elementView : view.getElements()) {
//                if (elementView.getElement().getParent() == container) {
//                    scopedElements.add((StaticStructureElement) elementView.getElement());
//                }
//            }
//            writeElements(view, "    ", scopedElements, fileWriter);
//            fileWriter.write("  }\n");
//        }
//
//        for (ElementView elementView : view.getElements()) {
//            if (!(elementView.getElement().getParent() instanceof Container)) {
//                writeElement(view, "  ", elementView.getElement(), fileWriter);
//            }
//        }
//
//        writeRelationships(view, fileWriter);
//
//        writeFooter(fileWriter);
//        fileWriter.close();
//    }
//
//    void write(DynamicView view) throws Exception {
//        File file = new File(path, view.getKey() + ".dot");
//        FileWriter fileWriter = new FileWriter(file);
//        writeHeader(fileWriter, view);
//
//        Element element = view.getElement();
//
//        if (element == null) {
//            for (ElementView elementView : view.getElements()) {
//                writeElement(view, "  ", elementView.getElement(), fileWriter);
//            }
//        } else if (element instanceof SoftwareSystem) {
//                List<SoftwareSystem> softwareSystems = new ArrayList<>(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof Container).map(c -> ((Container)c).getSoftwareSystem()).collect(Collectors.toSet()));
//                softwareSystems.sort(Comparator.comparing(Element::getId));
//
//                for (SoftwareSystem softwareSystem : softwareSystems) {
//                    fileWriter.write(String.format(locale, "  subgraph cluster_%s {\n", softwareSystem.getId()));
//                    fileWriter.write("    margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//                    for (ElementView elementView : view.getElements()) {
//                        if (elementView.getElement().getParent() == softwareSystem) {
//                            writeElement(view, "    ", elementView.getElement(), fileWriter);
//                        }
//                    }
//                    fileWriter.write("  }\n");
//                }
//
//                for (ElementView elementView : view.getElements()) {
//                    if (elementView.getElement().getParent() == null) {
//                        writeElement(view, "  ", elementView.getElement(), fileWriter);
//                    }
//                }
//        } else if (element instanceof Container) {
//            List<Container> containers = new ArrayList<>(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof Component).map(c -> ((Component)c).getContainer()).collect(Collectors.toSet()));
//            containers.sort(Comparator.comparing(Element::getId));
//
//            for (Container container : containers) {
//                fileWriter.write(String.format(locale, "  subgraph cluster_%s {\n", container.getId()));
//                fileWriter.write("    margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//                for (ElementView elementView : view.getElements()) {
//                    if (elementView.getElement().getParent() == container) {
//                        writeElement(view, "    ", elementView.getElement(), fileWriter);
//                    }
//                }
//                fileWriter.write("  }\n");
//            }
//
//            for (ElementView elementView : view.getElements()) {
//                if (!(elementView.getElement().getParent() instanceof Container)) {
//                    writeElement(view, "  ", elementView.getElement(), fileWriter);
//                }
//            }
//        }
//
//        writeRelationships(view, fileWriter);
//
//        writeFooter(fileWriter);
//        fileWriter.close();
//    }
//
//     void write(DeploymentView view) throws Exception {
//        File file = new File(path, view.getKey() + ".dot");
//        FileWriter fileWriter = new FileWriter(file);
//        writeHeader(fileWriter, view);
//
//        for (ElementView elementView : view.getElements()) {
//            if (elementView.getElement() instanceof DeploymentNode && elementView.getElement().getParent() == null) {
//                write(view, (DeploymentNode)elementView.getElement(), fileWriter, "");
//            } else if (elementView.getElement() instanceof CustomElement) {
//                writeElement(view, "  ", elementView.getElement(), fileWriter);
//            }
//        }
//
//        writeRelationships(view, fileWriter);
//
//        writeFooter(fileWriter);
//        fileWriter.close();
//    }
//
//    private void write(DeploymentView view, DeploymentNode deploymentNode, FileWriter fileWriter, String indent) throws Exception {
//        fileWriter.write(String.format(locale, indent + "subgraph cluster_%s {\n", deploymentNode.getId()));
//        fileWriter.write(indent + "  margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//        fileWriter.write(String.format(locale, indent + "  label=\"%s: %s\"\n", deploymentNode.getId(), deploymentNode.getName()));
//
//        for (DeploymentNode child : deploymentNode.getChildren()) {
//            if (view.isElementInView(child)) {
//                write(view, child, fileWriter, indent + "  ");
//
//            }
//        }
//
//        for (InfrastructureNode infrastructureNode : deploymentNode.getInfrastructureNodes()) {
//            if (view.isElementInView(infrastructureNode)) {
//                writeElement(view, indent + "  ", infrastructureNode, fileWriter);
//            }
//        }
//
//        for (SoftwareSystemInstance softwareSystemInstance : deploymentNode.getSoftwareSystemInstances()) {
//            if (view.isElementInView(softwareSystemInstance)) {
//                writeElement(view, indent + "  ", softwareSystemInstance, fileWriter);
//            }
//        }
//
//        for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
//            if (view.isElementInView(containerInstance)) {
//                writeElement(view, indent + "  ", containerInstance, fileWriter);
//            }
//        }
//
//        fileWriter.write(indent + "}\n");
//    }
//
//    private void writeElements(ModelView view, String padding, Set<GroupableElement> elements, Writer writer) throws Exception {
//        String groupSeparator = view.getModel().getProperties().get(GROUP_SEPARATOR_PROPERTY_NAME);
//        boolean nested = !StringUtils.isNullOrEmpty(groupSeparator);
//
//        Set<String> groups = new HashSet<>();
//        for (GroupableElement element : elements) {
//            String group = element.getGroup();
//
//            if (!StringUtils.isNullOrEmpty(group)) {
//                groups.add(group);
//
//                if (nested) {
//                    while (group.contains(groupSeparator)) {
//                        group = group.substring(0, group.lastIndexOf(groupSeparator));
//                        groups.add(group);
//                    }
//                }
//            }
//        }
//
//        List<String> sortedGroups = new ArrayList<>(groups);
//        sortedGroups.sort(String::compareTo);
//
//        // first render grouped elements
//        if (nested) {
//            if (groups.size() > 0) {
//                String context = "";
//                for (String group : sortedGroups) {
//                    int groupCount = group.split(groupSeparator).length;
//                    int contextCount = context.split(groupSeparator).length;
//
//                    if (groupCount > contextCount) {
//                        // moved from a to a/b
//                        // - increase padding
//                        padding = padding + INDENT;
//                    } else if (groupCount == contextCount) {
//                        // moved from a/b to a/c
//                        // - close off previous subgraph
//                        if (context.length() > 0) {
//                            writer.write(padding + "}\n");
//                        }
//                    } else {
//                        // moved from a/b/c to a/b or a
//                        // - close off previous subgraphs
//                        // - close off current subgraph
//                        for (int i = 0; i < (contextCount - groupCount); i++) {
//                            writer.write(padding + "}\n");
//                            padding = padding.substring(0, padding.length() - INDENT.length());
//                        }
//                        writer.write(padding + "}\n");
//                    }
//
//                    writer.write(padding + "subgraph cluster_group_" + groupId + " {\n");
////                    writer.write(padding + "  // " + group + "\n");
//                    writer.write(padding + "  margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//                    for (GroupableElement element : elements) {
//                        if (group.equals(element.getGroup())) {
//                            writeElement(view, padding + INDENT, element, writer);
//                        }
//                    }
//                    groupId++;
//                    context = group;
//                }
//
//                int contextCount = context.split(groupSeparator).length;
//                for (int i = 0; i < contextCount; i++) {
//                    writer.write(padding + "}\n");
//                    padding = padding.substring(0, padding.length() - INDENT.length());
//                }
//            }
//        } else {
//            for (String group : sortedGroups) {
//                writer.write(padding + "subgraph cluster_group_" + groupId + " {\n");
//                writer.write(padding + "  margin=" + CLUSTER_INTERNAL_MARGIN + "\n");
//                for (GroupableElement element : elements) {
//                    if (group.equals(element.getGroup())) {
//                        writeElement(view, padding + INDENT, element, writer);
//                    }
//                }
//                writer.write(padding + "}\n");
//                groupId++;
//            }
//        }
//
//        // then render ungrouped elements
//        for (GroupableElement element : elements) {
//            if (StringUtils.isNullOrEmpty(element.getGroup())) {
//                writeElement(view, padding, element, writer);
//            }
//        }
//    }
//
//    private void writeElement(ModelView view, String padding, Element element, Writer writer) throws Exception {
//        writer.write(String.format(locale, "%s%s [width=%f,height=%f,fixedsize=true,id=%s,label=\"%s: %s\"]",
//                padding,
//                element.getId(),
//                getElementWidth(view, element.getId()) / Constants.STRUCTURIZR_DPI, // convert Structurizr dimensions to inches
//                getElementHeight(view, element.getId()) / Constants.STRUCTURIZR_DPI, // convert Structurizr dimensions to inches
//                element.getId(),
//                element.getId(),
//                escape(element.getName())
//        ));
//        writer.write("\n");
//    }
//
    private String escape(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return s;
        } else {
            return s.replaceAll("\"", "\\\\\"");
        }
    }
//
//    private void writeRelationships(ModelView view, Writer writer) throws Exception {
//        writer.write("\n");
//
//        for (RelationshipView relationshipView : view.getRelationships()) {
//        }
//    }

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