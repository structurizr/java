package com.structurizr.export.websequencediagrams;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Exports dynamic diagram definitions that can be copy-pasted
 * into https://www.websequencediagrams.com
 *
 * This implementation only supports a basic sequence of interactions,
 * both synchronous and asynchronous. It doesn't support return messages,
 * parallel behaviour, etc.
 */
public class WebSequenceDiagramsExporter extends AbstractDiagramExporter {

    private static final String SYNCHRONOUS_INTERACTION = "->";
    private static final String ASYNCHRONOUS_INTERACTION = "->>";
    private static final String SYNCHRONOUS_INTERACTION_RETURN = "-->";
    private static final String ASYNCHRONOUS_INTERACTION_RETURN = "-->>";

    @Override
    public Diagram export(SystemLandscapeView view) {
        return null;
    }

    @Override
    public Diagram export(SystemContextView view) {
        return null;
    }

    @Override
    public Diagram export(ContainerView view) {
        return null;
    }

    @Override
    public Diagram export(ComponentView view) {
        return null;
    }

    @Override
    public Diagram export(DynamicView view) {
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

        writer.writeLine();

        writeRelationships(view, writer);
        writeFooter(view, writer);

        return createDiagram(view, writer.toString());
    }

    @Override
    public Diagram export(DeploymentView view) {
        return null;
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        if (!StringUtils.isNullOrEmpty(view.getDescription())) {
            writer.writeLine("title " + view.getName() + "\n" + view.getDescription());
        } else {
            writer.writeLine("title " + view.getName());
        }
        writer.writeLine();
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
    }

    @Override
    protected void startGroupBoundary(ModelView view, String group, IndentingWriter writer) {
    }

    @Override
    protected void endGroupBoundary(ModelView view, IndentingWriter writer) {
    }

    @Override
    protected void startSoftwareSystemBoundary(ModelView view, SoftwareSystem softwareSystem, IndentingWriter writer) {
    }

    @Override
    protected void endSoftwareSystemBoundary(ModelView view, IndentingWriter writer) {
    }

    @Override
    protected void startContainerBoundary(ModelView view, Container container, IndentingWriter writer) {
    }

    @Override
    protected void endContainerBoundary(ModelView view, IndentingWriter writer) {
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
    }

    @Override
    protected void endDeploymentNodeBoundary(ModelView view, IndentingWriter writer) {
    }

    @Override
    protected void writeElement(ModelView view, Element element, IndentingWriter writer) {
        if (element instanceof Person) {
            writer.writeLine(String.format("actor <<%s>>\\n%s as %s",
                    view.getViewSet().getConfiguration().getTerminology().findTerminology(element),
                    element.getName(),
                    element.getName())
            );
        } else {
            writer.writeLine(String.format("participant <<%s>>\\n%s as %s",
                    view.getViewSet().getConfiguration().getTerminology().findTerminology(element),
                    element.getName(),
                    element.getName())
            );
        }
    }

    @Override
    protected void writeRelationship(ModelView view, RelationshipView relationshipView, IndentingWriter writer) {
        Relationship r = relationshipView.getRelationship();

        Element source = r.getSource();
        Element destination = r.getDestination();
        String description = relationshipView.getDescription();
        String arrow = r.getInteractionStyle() == InteractionStyle.Asynchronous ? ASYNCHRONOUS_INTERACTION : SYNCHRONOUS_INTERACTION;

        if (relationshipView.isResponse() != null && relationshipView.isResponse()) {
            source = r.getDestination();
            destination = r.getSource();
            arrow = r.getInteractionStyle() == InteractionStyle.Asynchronous ? ASYNCHRONOUS_INTERACTION_RETURN : SYNCHRONOUS_INTERACTION_RETURN;
        }

        if (StringUtils.isNullOrEmpty(description)) {
            description = relationshipView.getRelationship().getDescription();
        }

        // Thing A->Thing B: Description
        writer.writeLine(String.format("%s%s%s: %s",
                source.getName(),
                arrow,
                destination.getName(),
                description
        ));
    }

    @Override
    protected Diagram createDiagram(ModelView view, String definition) {
        return new WebSequenceDiagramsDiagram(view, definition);
    }

}