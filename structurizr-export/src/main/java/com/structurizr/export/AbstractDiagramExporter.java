package com.structurizr.export;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractDiagramExporter extends AbstractExporter implements DiagramExporter {

    protected static final String GROUP_SEPARATOR_PROPERTY_NAME = "structurizr.groupSeparator";

    private Object frame = null;

    /**
     * Exports all views in the workspace.
     *
     * @param workspace     the workspace containing the views to be written
     * @return  a collection of diagram definitions, one per view
     */
    public final Collection<Diagram> export(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        Collection<Diagram> diagrams = new ArrayList<>();

        for (CustomView view : workspace.getViews().getCustomViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (SystemLandscapeView view : workspace.getViews().getSystemLandscapeViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (SystemContextView view : workspace.getViews().getSystemContextViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (ContainerView view : workspace.getViews().getContainerViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (ComponentView view : workspace.getViews().getComponentViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (DynamicView view : workspace.getViews().getDynamicViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        for (DeploymentView view : workspace.getViews().getDeploymentViews()) {
            Diagram diagram = export(view);
            if (diagram != null) {
                diagrams.add(diagram);
            }
        }

        return diagrams;
    }

    public Diagram export(ModelView view) {
        if (view instanceof SystemLandscapeView) {
            return export((SystemLandscapeView)view);
        } else if (view instanceof SystemContextView) {
            return export((SystemContextView)view);
        } else if (view instanceof ContainerView) {
            return export((ContainerView)view);
        } else if (view instanceof ComponentView) {
            return export((ComponentView)view);
        } else if (view instanceof DynamicView) {
            return export((DynamicView)view);
        } else if (view instanceof DeploymentView) {
            return export((DeploymentView)view);
        } else if (view instanceof CustomView) {
            return export((CustomView)view);
        } else {
            throw new RuntimeException(view.getClass().getName() + " is not supported");
        }
    }

    public Diagram export(CustomView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    private Diagram export(CustomView view, Integer animationStep) {
        this.frame = animationStep;

        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        List<GroupableElement> elements = new ArrayList<>();
        for (ElementView elementView : view.getElements()) {
            elements.add((CustomElement)elementView.getElement());
        }

        writeElements(view, elements, writer);

        writer.writeLine();
        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    public Diagram export(SystemLandscapeView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    private Diagram export(SystemLandscapeView view, Integer animationStep) {
        this.frame = animationStep;

        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        List<GroupableElement> elements = new ArrayList<>();
        for (ElementView elementView : view.getElements()) {
            elements.add((GroupableElement)elementView.getElement());
        }
        writeElements(view, elements, writer);

        writer.writeLine();
        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    public Diagram export(SystemContextView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    private Diagram export(SystemContextView view, Integer animationStep) {
        this.frame = animationStep;

        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        List<GroupableElement> elements = new ArrayList<>();
        for (ElementView elementView : view.getElements()) {
            elements.add((GroupableElement)elementView.getElement());
        }
        writeElements(view, elements, writer);

        writer.writeLine();
        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    public Diagram export(ContainerView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    public Diagram export(ContainerView view, Integer animationStep) {
        this.frame = animationStep;
        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        boolean elementsWritten = false;
        for (ElementView elementView : view.getElements()) {
            if (!(elementView.getElement() instanceof Container)) {
                writeElement(view, elementView.getElement(), writer);
                elementsWritten = true;
            }
        }

        if (elementsWritten) {
            writer.writeLine();
        }

        List<SoftwareSystem> softwareSystems = getBoundarySoftwareSystems(view);
        for (SoftwareSystem softwareSystem : softwareSystems) {
            startSoftwareSystemBoundary(view, softwareSystem, writer);

            List<GroupableElement> scopedElements = new ArrayList<>();
            for (ElementView elementView : view.getElements()) {
                if (elementView.getElement().getParent() == softwareSystem) {
                    scopedElements.add((StaticStructureElement) elementView.getElement());
                }
            }

            writeElements(view, scopedElements, writer);

            endSoftwareSystemBoundary(view, writer);
        }

        writeRelationships(view, writer);

        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    protected List<SoftwareSystem> getBoundarySoftwareSystems(ModelView view) {
        List<SoftwareSystem> softwareSystems = new ArrayList<>(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof Container).map(c -> ((Container)c).getSoftwareSystem()).collect(Collectors.toSet()));
        softwareSystems.sort(Comparator.comparing(Element::getId));

        return softwareSystems;
    }

    public Diagram export(ComponentView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    public Diagram export(ComponentView view, Integer animationStep) {
        this.frame = animationStep;
        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        boolean elementsWritten = false;
        for (ElementView elementView : view.getElements()) {
            if (!(elementView.getElement() instanceof Component)) {
                writeElement(view, elementView.getElement(), writer);
                elementsWritten = true;
            }
        }

        if (elementsWritten) {
            writer.writeLine();
        }

        boolean includeSoftwareSystemBoundaries = "true".equals(view.getProperties().getOrDefault("structurizr.softwareSystemBoundaries", "false"));

        List<Container> containers = getBoundaryContainers(view);
        Set<SoftwareSystem> softwareSystems = containers.stream().map(Container::getSoftwareSystem).collect(Collectors.toCollection(LinkedHashSet::new));
        for (SoftwareSystem softwareSystem : softwareSystems) {

            if (includeSoftwareSystemBoundaries) {
                startSoftwareSystemBoundary(view, softwareSystem, writer);
                writer.indent();
            }

            for (Container container : containers) {
                if (container.getSoftwareSystem() == softwareSystem) {
                    startContainerBoundary(view, container, writer);

                    List<GroupableElement> scopedElements = new ArrayList<>();
                    for (ElementView elementView : view.getElements()) {
                        if (elementView.getElement().getParent() == container) {
                            scopedElements.add((StaticStructureElement) elementView.getElement());
                        }
                    }
                    writeElements(view, scopedElements, writer);

                    endContainerBoundary(view, writer);
                }
            }

            if (includeSoftwareSystemBoundaries) {
                endSoftwareSystemBoundary(view, writer);
                writer.outdent();
            }
        }

        writeRelationships(view, writer);

        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    protected List<Container> getBoundaryContainers(ModelView view) {
        List<Container> containers = new ArrayList<>(view.getElements().stream().map(ElementView::getElement).filter(e -> e instanceof Component).map(c -> ((Component)c).getContainer()).collect(Collectors.toSet()));
        containers.sort(Comparator.comparing(Element::getId));

        return containers;
    }

    public Diagram export(DynamicView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view)) {
            LinkedHashSet<String> orders = new LinkedHashSet<>();
            for (RelationshipView relationshipView : view.getRelationships()) {
                orders.add(relationshipView.getOrder());
            }

            for (String order : orders) {
                Diagram frame = export(view, order);
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    public Diagram export(DynamicView view, String order) {
        this.frame = order;
        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        boolean elementsWritten = false;

        Element element = view.getElement();

        if (element == null) {
            // dynamic view with no scope
            List<GroupableElement> elements = new ArrayList<>();
            for (ElementView elementView : view.getElements()) {
                elements.add((StaticStructureElement) elementView.getElement());
            }
            writeElements(view, elements, writer);
        } else {
            if (element instanceof SoftwareSystem) {
                // dynamic view with software system scope
                List<SoftwareSystem> softwareSystems = getBoundarySoftwareSystems(view);
                for (SoftwareSystem softwareSystem : softwareSystems) {
                    startSoftwareSystemBoundary(view, softwareSystem, writer);

                    List<GroupableElement> scopedElements = new ArrayList<>();
                    for (ElementView elementView : view.getElements()) {
                        if (elementView.getElement().getParent() == softwareSystem) {
                            scopedElements.add((StaticStructureElement) elementView.getElement());
                        }
                    }

                    writeElements(view, scopedElements, writer);

                    endSoftwareSystemBoundary(view, writer);
                }

                for (ElementView elementView : view.getElements()) {
                    if (elementView.getElement().getParent() == null) {
                        writeElement(view, elementView.getElement(), writer);
                        elementsWritten = true;
                    }
                }
            } else if (element instanceof Container) {
                // dynamic view with container scope
                boolean includeSoftwareSystemBoundaries = "true".equals(view.getProperties().getOrDefault("structurizr.softwareSystemBoundaries", "false"));

                List<Container> containers = getBoundaryContainers(view);
                Set<SoftwareSystem> softwareSystems = containers.stream().map(Container::getSoftwareSystem).collect(Collectors.toCollection(LinkedHashSet::new));
                for (SoftwareSystem softwareSystem : softwareSystems) {

                    if (includeSoftwareSystemBoundaries) {
                        startSoftwareSystemBoundary(view, softwareSystem, writer);
                        writer.indent();
                    }

                    for (Container container : containers) {
                        if (container.getSoftwareSystem() == softwareSystem) {
                            startContainerBoundary(view, container, writer);

                            List<GroupableElement> scopedElements = new ArrayList<>();
                            for (ElementView elementView : view.getElements()) {
                                if (elementView.getElement().getParent() == container) {
                                    scopedElements.add((StaticStructureElement) elementView.getElement());
                                }
                            }
                            writeElements(view, scopedElements, writer);

                            endContainerBoundary(view, writer);
                        }
                    }

                    if (includeSoftwareSystemBoundaries) {
                        endSoftwareSystemBoundary(view, writer);
                        writer.outdent();
                    }
                }

                for (ElementView elementView : view.getElements()) {
                    if (!(elementView.getElement().getParent() instanceof Container)) {
                        writeElement(view, elementView.getElement(), writer);
                        elementsWritten = true;
                    }
                }
            }
        }

        if (elementsWritten) {
            writer.writeLine();
        }

        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    public Diagram export(DeploymentView view) {
        Diagram diagram = export(view, null);

        if (isAnimationSupported(view) && !view.getAnimations().isEmpty()) {
            for (Animation animation : view.getAnimations()) {
                Diagram frame = export(view, animation.getOrder());
                diagram.addFrame(frame);
            }
        }

        diagram.setLegend(createLegend(view));
        return diagram;
    }

    public Diagram export(DeploymentView view, Integer animationStep) {
        this.frame = animationStep;
        IndentingWriter writer = new IndentingWriter();
        writeHeader(view, writer);

        List<GroupableElement> elements = new ArrayList<>();

        for (ElementView elementView : view.getElements()) {
            if (elementView.getElement() instanceof DeploymentNode && elementView.getElement().getParent() == null) {
                elements.add((DeploymentNode)elementView.getElement());
            }
        }

        writeElements(view, elements, writer);

        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    protected void writeElements(ModelView view, List<GroupableElement> elements, IndentingWriter writer) {
        String groupSeparator = view.getModel().getProperties().get(GROUP_SEPARATOR_PROPERTY_NAME);
        boolean nested = !StringUtils.isNullOrEmpty(groupSeparator);

        elements.sort(Comparator.comparing(Element::getId));

        Set<String> groupsAsSet = new HashSet<>();
        for (GroupableElement element : elements) {
            String group = element.getGroup();

            if (!StringUtils.isNullOrEmpty(group)) {
                groupsAsSet.add(group);

                if (nested) {
                    while (group.contains(groupSeparator)) {
                        group = group.substring(0, group.lastIndexOf(groupSeparator));
                        groupsAsSet.add(group);
                    }
                }
            }
        }

        List<String> groupsAsList = new ArrayList<>(groupsAsSet);
        Collections.sort(groupsAsList);

        // first render grouped elements
        if (groupsAsList.size() > 0) {
            if (nested) {
                String context = "";

                for (String group : groupsAsList) {
                    int groupCount = group.split(Pattern.quote(groupSeparator)).length;
                    int contextCount = context.split(Pattern.quote(groupSeparator)).length;

                    if (groupCount > contextCount) {
                        // moved from a to a/b
                        // - increase padding
                        writer.indent();
                    } else if (groupCount == contextCount) {
                        // moved from a/b to a/c
                        // - close off previous subgraph
                        if (context.length() > 1) {
                            endGroupBoundary(view, writer);
                        }
                    } else {
                        // moved from a/b/c to a/b or a
                        // - close off previous subgraphs
                        // - close off current subgraph
                        for (int i = 0; i < (contextCount - groupCount); i++) {
                            endGroupBoundary(view, writer);
                            writer.outdent();
                        }
                        endGroupBoundary(view, writer);
                    }

                    startGroupBoundary(view, group, writer);

                    for (GroupableElement element : elements) {
                        if (group.equals(element.getGroup())) {
                            write(view, element, writer);
                        }
                    }

                    context = group;
                }

                int contextCount = context.split(Pattern.quote(groupSeparator)).length;
                for (int i = 0; i < contextCount; i++) {
                    endGroupBoundary(view, writer);

                    if (i < contextCount-1) {
                        writer.outdent();
                    }
                }
            } else {
                for (String group : groupsAsList) {
                    startGroupBoundary(view, group, writer);

                    for (GroupableElement element : elements) {
                        if (group.equals(element.getGroup())) {
                            write(view, element, writer);
                        }
                    }

                    endGroupBoundary(view, writer);
                }
            }
        }

        // then render ungrouped elements
        for (GroupableElement element : elements) {
            if (StringUtils.isNullOrEmpty(element.getGroup())) {
                write(view, element, writer);
            }
        }
    }

    protected void writeRelationships(ModelView view, IndentingWriter writer) {
        Collection<RelationshipView> relationshipList;

        if (view instanceof DynamicView) {
            relationshipList = view.getRelationships();
        } else {
            relationshipList = view.getRelationships().stream().sorted(Comparator.comparing(rv -> rv.getRelationship().getId())).collect(Collectors.toList());
        }

        for (RelationshipView relationshipView : relationshipList) {
            writeRelationship(view, relationshipView, writer);
        }
    }

    protected abstract void writeHeader(ModelView view, IndentingWriter writer);
    protected abstract void writeFooter(ModelView view, IndentingWriter writer);

    protected abstract void startEnterpriseBoundary(ModelView view, String enterpriseName, IndentingWriter writer);
    protected abstract void endEnterpriseBoundary(ModelView view, IndentingWriter writer);

    protected abstract void startGroupBoundary(ModelView view, String group, IndentingWriter writer);
    protected abstract void endGroupBoundary(ModelView view, IndentingWriter writer);

    protected abstract void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer);
    protected abstract void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer);

    protected abstract void startContainerBoundary(ModelView view, Container container, IndentingWriter writer);
    protected abstract void endContainerBoundary(ModelView view, IndentingWriter writer);

    protected abstract void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer);
    protected abstract void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer);

    private void write(ModelView view, Element element, IndentingWriter writer) {
        if (view instanceof DeploymentView && element instanceof DeploymentNode) {
            writeDeploymentNode((DeploymentView)view, (DeploymentNode)element, writer);
        } else {
            writeElement(view, element, writer);
        }
    }

    private void writeDeploymentNode(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        startDeploymentNodeBoundary(view, deploymentNode, writer);

        List<GroupableElement> elements = new ArrayList<>();

        List<DeploymentNode> children = new ArrayList<>(deploymentNode.getChildren());
        children.sort(Comparator.comparing(DeploymentNode::getName));
        for (DeploymentNode child : children) {
            if (view.isElementInView(child)) {
                elements.add(child);
            }
        }

        List<InfrastructureNode> infrastructureNodes = new ArrayList<>(deploymentNode.getInfrastructureNodes());
        infrastructureNodes.sort(Comparator.comparing(InfrastructureNode::getName));
        for (InfrastructureNode infrastructureNode : infrastructureNodes) {
            if (view.isElementInView(infrastructureNode)) {
                elements.add(infrastructureNode);
            }
        }

        List<SoftwareSystemInstance> softwareSystemInstances = new ArrayList<>(deploymentNode.getSoftwareSystemInstances());
        softwareSystemInstances.sort(Comparator.comparing(SoftwareSystemInstance::getName));
        for (SoftwareSystemInstance softwareSystemInstance : softwareSystemInstances) {
            if (view.isElementInView(softwareSystemInstance)) {
                elements.add(softwareSystemInstance);
            }
        }

        List<ContainerInstance> containerInstances = new ArrayList<>(deploymentNode.getContainerInstances());
        containerInstances.sort(Comparator.comparing(ContainerInstance::getName));
        for (ContainerInstance containerInstance : containerInstances) {
            if (view.isElementInView(containerInstance)) {
                elements.add(containerInstance);
            }
        }

        writeElements(view, elements, writer);

        endDeploymentNodeBoundary(view, writer);
    }

    protected abstract void writeElement(ModelView view, Element element, IndentingWriter writer);
    protected abstract void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer);

    protected boolean isAnimationSupported(ModelView view) {
        return false;
    }

    protected boolean isVisible(ModelView view, Element element) {
        if (frame != null) {
            Set<String> elementIds = new HashSet<>();

            if (view instanceof StaticView) {
                int step = (int)frame;
                if (step > 0) {
                    StaticView staticView = (StaticView) view;
                    staticView.getAnimations().stream().filter(a -> a.getOrder() <= step).forEach(a -> {
                        elementIds.addAll(a.getElements());
                    });

                    return elementIds.contains(element.getId());
                }
            } else if (view instanceof DeploymentView) {
                int step = (int)frame;
                if (step > 0) {
                    DeploymentView deploymentView = (DeploymentView) view;
                    deploymentView.getAnimations().stream().filter(a -> a.getOrder() <= step).forEach(a -> {
                        elementIds.addAll(a.getElements());
                    });

                    return elementIds.contains(element.getId());
                }
            } else if (view instanceof DynamicView) {
                String order = (String)frame;
                view.getRelationships().stream().filter(rv -> order.equals(rv.getOrder())).forEach(rv -> {
                    elementIds.add(rv.getRelationship().getSourceId());
                    elementIds.add(rv.getRelationship().getDestinationId());
                });

                return elementIds.contains(element.getId());
            }
        }

        return true;
    }

    protected boolean isVisible(ModelView view, RelationshipView relationshipView) {
        if (view instanceof DynamicView && frame != null) {
            return frame.equals(relationshipView.getOrder());
        }

        return true;
    }

    protected abstract Diagram createDiagram(ModelView view, String definition);

    protected Legend createLegend(ModelView view) {
        return null;
    }

    protected String getViewOrViewSetProperty(ModelView view, String name, String defaultValue) {
        ViewSet views = view.getViewSet();

        return
            view.getProperties().getOrDefault(name,
                    views.getConfiguration().getProperties().getOrDefault(name, defaultValue)
            );
    }

}