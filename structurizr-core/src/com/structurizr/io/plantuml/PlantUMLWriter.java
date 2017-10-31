package com.structurizr.io.plantuml;

import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriter;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.ContainerInstance;
import com.structurizr.model.DeploymentNode;
import com.structurizr.model.Element;
import com.structurizr.model.Location;
import com.structurizr.model.Person;
import com.structurizr.model.Relationship;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.DeploymentView;
import com.structurizr.view.DynamicView;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.ElementView;
import com.structurizr.view.EnterpriseContextView;
import com.structurizr.view.RelationshipView;
import com.structurizr.view.Shape;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.View;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * A simple PlantUML writer that outputs diagram definitions that can be copy-pasted
 * into http://plantuml.com/plantuml/ ... it supports enterprise context, system context,
 * container, component and dynamic diagrams.
 *
 * Note: This won't work if you have two elements named the same on a diagram.
 */
public final class PlantUMLWriter implements WorkspaceWriter {

    private final List<String> skinParams = new ArrayList<>();

    public void addSkinParam(final String skinParam) {
        skinParams.add(skinParam);
    }

    @Override
    public void write(Workspace workspace, Writer writer) throws WorkspaceWriterException {
        if (workspace != null && writer != null) {
            workspace.getViews().getEnterpriseContextViews().forEach(v -> write(workspace, v, writer));
            workspace.getViews().getSystemContextViews().forEach(v -> write(workspace, v, writer));
            workspace.getViews().getContainerViews().forEach(v -> write(workspace, v, writer));
            workspace.getViews().getComponentViews().forEach(v -> write(workspace, v, writer));
            workspace.getViews().getDynamicViews().forEach(v -> write(workspace, v, writer));
            workspace.getViews().getDeploymentViews().forEach(v -> write(workspace, v, writer));
        }
    }

    public void write(Workspace workspace, View view, Writer writer) {
        if (view != null && writer != null) {
            if (EnterpriseContextView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (EnterpriseContextView) view, writer);
            } else if (SystemContextView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (SystemContextView) view, writer);
            } else if (ContainerView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (ContainerView) view, writer);
            } else if (ComponentView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (ComponentView) view, writer);
            } else if (DynamicView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (DynamicView) view, writer);
            } else if (DeploymentView.class.isAssignableFrom(view.getClass())) {
                write(workspace, (DeploymentView) view, writer);
            }
        }
    }

    private void write(Workspace workspace, EnterpriseContextView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.External)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.External)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));

            writer.write("package " + nameOf(view.getModel().getEnterprise().getName()) + " {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, true));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, SystemContextView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));
            write(view.getRelationships(), writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, ContainerView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Container))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));

            writer.write("package " + nameOf(view.getSoftwareSystem()) + " " + colorOf(workspace, view.getSoftwareSystem()) + " {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Container)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, ComponentView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Component))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));

            writer.write("package " + nameOf(view.getContainer()) + " " + colorOf(workspace, view.getContainer()) +" {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Component)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, DynamicView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, false));

            view.getRelationships().stream()
                    .sorted((rv1, rv2) -> (rv1.getOrder().compareTo(rv2.getOrder())))
                    .forEach(relationship -> {
                        try {
                            writer.write(
                                    format("%s -> %s : %s",
                                            idOf(relationship.getRelationship().getSource()),
                                            idOf(relationship.getRelationship().getDestination()),
                                            hasValue(relationship.getDescription()) ? relationship.getDescription() : hasValue(relationship.getRelationship().getDescription()) ? relationship.getRelationship().getDescription() : ""
                                    )
                            );
                            writer.write(System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, DeploymentView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof DeploymentNode && ev.getElement().getParent() == null)
                    .map(ev -> (DeploymentNode)ev.getElement())
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(workspace, e, writer, 0));

            write(view.getRelationships(), writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, DeploymentNode deploymentNode, Writer writer, int indent) {
        try {
            writer.write(
                    format("%snode \"%s\" <<%s>> as %s %s {",
                            calculateIndent(indent),
                            deploymentNode.getName() + (deploymentNode.getInstances() > 1 ? " (x" + deploymentNode.getInstances() + ")" : ""),
                            typeOf(deploymentNode),
                            idOf(deploymentNode),
                            colorOf(workspace, deploymentNode)
                    )
            );

            writer.write(System.lineSeparator());

            for (DeploymentNode child : deploymentNode.getChildren()) {
                write(workspace, child, writer, indent+1);
            }

            for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
                write(workspace, containerInstance, writer, indent+1);
            }

            writer.write(
                    format("%s}", calculateIndent(indent))
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Workspace workspace, ContainerInstance containerInstance, Writer writer, int indent) {
        try {
            writer.write(
                    format("%sartifact \"%s\" <<%s>> as %s %s",
                            calculateIndent(indent),
                            containerInstance.getContainer().getName(),
                            typeOf(containerInstance),
                            idOf(containerInstance),
                            colorOf(workspace, containerInstance.getContainer())
                    )
            );


            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String calculateIndent(int indent) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < indent; i++) {
            buf.append("  ");
        }

        return buf.toString();
    }

    private void write(Workspace workspace, Element element, Writer writer, boolean indent) {
        try {
            final String type = plantumlType(workspace, element);
            final List<String> description = lines(element.getDescription());

            if(description.isEmpty() || "actor".equals(type)) {
                writeSimpleElement(workspace, element, writer, indent, type);
                writeDescriptionAsNote(element, writer, indent, description);
            }
            else {
                final String prefix = indent ? "  " : "";
                final String separator = System.lineSeparator();
                final String id = idOf(element);

                writer.write(format("%s%s %s <<%s>> %s [%s",
                        prefix, type, id, typeOf(element), colorOf(workspace, element), separator));
                writer.write(format("%s  %s%s", prefix, element.getName(), separator));
                writer.write(format("%s  --%s", prefix, separator));
                for (final String line : description) {
                    writer.write(format("%s  %s%s", prefix, line, separator));
                }
                writer.write(format("%s]%s", prefix, separator));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSimpleElement(Workspace workspace, Element element, Writer writer, boolean indent, String type) throws IOException {
        writer.write(format("%s%s \"%s\" <<%s>> as %s %s%s",
                indent ? "  " : "",
                type,
                element.getName(),
                typeOf(element),
                idOf(element),
                colorOf(workspace, element),
                System.lineSeparator()));
    }

    private void writeDescriptionAsNote(Element element, Writer writer, boolean indent, List<String> description) throws IOException {
        if (!description.isEmpty()) {
            final String prefix = indent ? "  " : "";
            final String separator = System.lineSeparator();
            final String id = idOf(element);
            writer.write(format("%snote right of %s%s", prefix, id, separator));
            for (final String line : description) {
                writer.write(format("%s  %s%s", prefix, line, separator));
            }
            writer.write(format("%send note%s", prefix, separator));
        }
    }

    private List<String> lines(final String text) {
        if(text==null) {
            return emptyList();
        }
        final String[] words = text.trim().split("\\s+");
        final List<String> lines = new ArrayList<>();
        final StringBuilder line = new StringBuilder();
        for (final String word : words) {
            if(line.length()==0) {
                line.append(word);
            }
            else if(line.length() + word.length() + 1 < 30) {
                line.append(' ').append(word);
            }
            else {
                lines.add(line.toString());
                line.setLength(0);
                line.append(word);
            }
        }
        if(line.length()>0) {
            lines.add(line.toString());
        }
        return lines;
    }

    private Object colorOf(Workspace workspace, Element element) {
        return workspace
                .getViews()
                .getConfiguration()
                .getStyles()
                .getElements()
                .stream()
                .filter(es -> element.getTags().contains(es.getTag()))
                .map(ElementStyle::getBackground)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
    }

    private String plantumlType(Workspace workspace, Element element) {
        final Optional<Shape> shape = workspace
                .getViews()
                .getConfiguration()
                .getStyles()
                .getElements()
                .stream()
                .filter(es -> element.getTags().contains(es.getTag()))
                .map(ElementStyle::getShape)
                .filter(Objects::nonNull)
                .findFirst();

        if(shape.isPresent()) {
            switch(shape.get()) {
                case Box:
                    return "rectangle";
                case Person:
                    return "actor";
                case Cylinder:
                    return "database";
                case Folder:
                    return "folder";
                case Ellipse:
                case Circle:
                    return "storage";
            }
        }
        return element instanceof Person ? "actor" : "component";
    }

    private void write(Set<RelationshipView> relationships, Writer writer) {
        relationships.stream()
                .map(RelationshipView::getRelationship)
                .sorted((r1, r2) -> (r1.getSource().getName() + r1.getDestination().getName()).compareTo(r2.getSource().getName() + r2.getDestination().getName()))
                .forEach(r -> write(r, writer));
    }

    private void write(Relationship relationship, Writer writer) {
        try {
            writer.write(
                    format("%s ..> %s %s%s",
                            idOf(relationship.getSource()),
                            idOf(relationship.getDestination()),
                            hasValue(relationship.getDescription()) ? ": " + relationship.getDescription() : "",
                            hasValue(relationship.getTechnology()) ? " <<" + relationship.getTechnology() + ">>" : ""
                    )
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String idOf(Element e) {
        return e.getId();
    }

    private String nameOf(Element e) {
        return nameOf(e.getName());
    }

    private String nameOf(String s) {
        if (s != null) {
            return s.replaceAll(" ", "")
                    .replaceAll("-", "");
        } else {
            return "";
        }
    }

    private String typeOf(Element e) {
        if (e instanceof SoftwareSystem) {
            return "Software System";
        } else if (e instanceof Component) {
            Component component = (Component)e;
            return hasValue(component.getTechnology()) ? component.getTechnology() : "Component";
        } else if (e instanceof DeploymentNode) {
            DeploymentNode deploymentNode = (DeploymentNode)e;
            return hasValue(deploymentNode.getTechnology()) ? deploymentNode.getTechnology() : "Deployment Node";
        } else if (e instanceof ContainerInstance) {
            return "Container";
        } else {
            return e.getClass().getSimpleName();
        }
    }

    private boolean hasValue(String s) {
        return s != null && s.trim().length() > 0;
    }

    private void writeHeader(View view, Writer writer) throws IOException {
        writer.write("@startuml");
        writer.write(System.lineSeparator());

        writer.write("title " + view.getName());
        writer.write(System.lineSeparator());

        if (view.getDescription() != null && view.getDescription().trim().length() > 0) {
            writer.write("caption " + view.getDescription());
            writer.write(System.lineSeparator());
        }

        if (!skinParams.isEmpty()) {
            writer.write(format("skinparam {%s", System.lineSeparator()));
            for (final String skinParam : skinParams) {
                writer.write(format("  %s%s", skinParam, System.lineSeparator()));
            }
            writer.write(format("}%s", System.lineSeparator()));
        }
    }

    private void writeFooter(Writer writer) throws IOException {
        writer.write("@enduml");
        writer.write(System.lineSeparator());
        writer.write(System.lineSeparator());
    }

}