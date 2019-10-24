package com.structurizr.io.mermaid;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * A writer that outputs diagram definitions that can be used to create diagrams
 * using mermaid (https://mermaidjs.github.io).
 *
 * System landscape, system context, container, component, dynamic and deployment diagrams are supported.
 * Deployment node -> deployment node relationships are not rendered.
 */
public class MermaidWriter {

    private boolean useSequenceDiagrams = false;

    /**
     * Creates a new PlantUMLWriter, with some default skin params.
     */
    public MermaidWriter() {
    }

    public boolean isUseSequenceDiagrams() {
        return useSequenceDiagrams;
    }

    public void setUseSequenceDiagrams(boolean useSequenceDiagrams) {
        this.useSequenceDiagrams = useSequenceDiagrams;
    }

    /**
     * Writes the views in the given workspace as PlantUML definitions, to the specified writer.
     *
     * @param workspace     the workspace containing the views to be written
     * @param writer        the Writer to write to
     */
    public void write(Workspace workspace, Writer writer) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        if (writer == null) {
            throw new IllegalArgumentException("A writer must be provided.");
        }

        workspace.getViews().getSystemLandscapeViews().forEach(v -> write(v, writer));
        workspace.getViews().getSystemContextViews().forEach(v -> write(v, writer));
        workspace.getViews().getContainerViews().forEach(v -> write(v, writer));
        workspace.getViews().getComponentViews().forEach(v -> write(v, writer));
        workspace.getViews().getDynamicViews().forEach(v -> write(v, writer));
        workspace.getViews().getDeploymentViews().forEach(v -> write(v, writer));
    }

    /**
     * Write the views in the given workspace as PlantUML definitions, to stdout.
     *
     * @param workspace     the workspace containing the views to be written
     */
    public void toStdOut(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        StringWriter stringWriter = new StringWriter();
        write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

    /**
     * Creates PlantUML diagram definitions based upon the specified workspace, returning them as strings.
     *
     * @param workspace     the workspace containing the views to be written
     * @return  an array of PlantUML diagram definitions, one per view
     */
    public String[] toString(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        StringWriter stringWriter = new StringWriter();
        write(workspace, stringWriter);

        String diagrams = stringWriter.toString();
        if (diagrams != null && diagrams.contains("graph TB")) {
            return stringWriter.toString().split("(?=graph TB)");
        } else {
            return new String[0];
        }
    }

    /**
     * Gets a single view as a mermaid diagram definition.
     *
     * @param view      the view to write
     */
    public String toString(View view) {
        StringWriter stringWriter = new StringWriter();
        write(view, stringWriter);
        return stringWriter.toString();
    }

    /**
     * Writes a single view as a PlantUML diagram definition, to the specified writer.
     *
     * @param view      the view to write
     * @param writer    the Writer to write the PlantUML definition to
     */
    public void write(View view, Writer writer) {
        if (view == null) {
            throw new IllegalArgumentException("A view must be provided.");
        }

        if (writer == null) {
            throw new IllegalArgumentException("A writer must be provided.");
        }

        if (SystemLandscapeView.class.isAssignableFrom(view.getClass())) {
            write((SystemLandscapeView) view, writer);
        } else if (SystemContextView.class.isAssignableFrom(view.getClass())) {
            write((SystemContextView) view, writer);
        } else if (ContainerView.class.isAssignableFrom(view.getClass())) {
            write((ContainerView) view, writer);
        } else if (ComponentView.class.isAssignableFrom(view.getClass())) {
            write((ComponentView) view, writer);
        } else if (DynamicView.class.isAssignableFrom(view.getClass())) {
            write((DynamicView) view, writer);
        } else if (DeploymentView.class.isAssignableFrom(view.getClass())) {
            write((DeploymentView) view, writer);
        }
    }

    protected void write(SystemLandscapeView view, Writer writer) {
        writeSystemLandscapeOrContextView(view, writer, view.isEnterpriseBoundaryVisible());
    }

    protected void write(SystemContextView view, Writer writer) {
        writeSystemLandscapeOrContextView(view, writer, view.isEnterpriseBoundaryVisible());
    }

    private void writeSystemLandscapeOrContextView(View view, Writer writer, boolean showEnterpriseBoundary) {
        try {
            writeHeader(view, writer);

            boolean enterpriseBoundaryVisible;
            enterpriseBoundaryVisible =
                    showEnterpriseBoundary &&
                    (view.getElements().stream().map(ElementView::getElement).anyMatch(e -> e instanceof Person && ((Person)e).getLocation() == Location.Internal) ||
                    view.getElements().stream().map(ElementView::getElement).anyMatch(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.Internal));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() != Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 0));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() != Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 0));

            if (enterpriseBoundaryVisible) {
                String name = view.getModel().getEnterprise() != null ? view.getModel().getEnterprise().getName() : "Enterprise";
                writer.write("  subgraph \"" + name + "\"");
                writer.write(System.lineSeparator());
            }

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, enterpriseBoundaryVisible ? 1 : 0));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, enterpriseBoundaryVisible ? 1 : 0));

            if (enterpriseBoundaryVisible) {
                writer.write("  end");
                writer.write(System.lineSeparator());
            }

            writeRelationships(view, writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(ContainerView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Container))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 0));

            writer.write("  subgraph \"" + view.getSoftwareSystem().getName() + "\"");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Container)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 1));

            writer.write("  end");
            writer.write(System.lineSeparator());

            writeRelationships(view, writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(ComponentView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Component))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 0));

            writer.write("  subgraph \"" + view.getContainer().getName() + "\"");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Component)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 1));

            writer.write("  end");
            writer.write(System.lineSeparator());

            writeRelationships(view, writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(DynamicView view, Writer writer) {
        try {
            writeHeader(view, writer);

            if (useSequenceDiagrams) {
                throw new UnsupportedOperationException("Not implemented yet.");
            } else {
                view.getElements().stream()
                        .map(ElementView::getElement)
                        .sorted((e1, e2) -> e1.getName().compareTo(e2.getName())).
                        forEach(e -> write(view, e, writer, 0));
            }
            view.getRelationships().stream()
                    .sorted((rv1, rv2) -> (rv1.getOrder().compareTo(rv2.getOrder())))
                    .forEach(relationship -> {
                        try {
                            writer.write(
                                    format("  %s-->|\"<b>%s. %s</b><br /><sup>%s</sup>\"|%s",
                                            idOf(relationship.getRelationship().getSource()),
                                            relationship.getOrder(),
                                            lines(hasValue(relationship.getDescription()) ? relationship.getDescription() : hasValue(relationship.getRelationship().getDescription()) ? relationship.getRelationship().getDescription() : ""),
                                            !StringUtils.isNullOrEmpty(relationship.getRelationship().getTechnology()) ? "[" + relationship.getRelationship().getTechnology() + "]" : "",
                                            idOf(relationship.getRelationship().getDestination())
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

    protected void write(DeploymentView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof DeploymentNode && ev.getElement().getParent() == null)
                    .map(ev -> (DeploymentNode)ev.getElement())
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 1));

            writeRelationships(view, writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(View view, DeploymentNode deploymentNode, Writer writer, int indent) {
        try {
            writer.write(
                    format("%ssubgraph \"%s\"",
                            calculateIndent(indent),
                            deploymentNode.getName()
                    )
            );

            writer.write(System.lineSeparator());

            for (DeploymentNode child : deploymentNode.getChildren()) {
                write(view, child, writer, indent+1);
            }

            for (ContainerInstance containerInstance : deploymentNode.getContainerInstances()) {
                write(view, containerInstance, writer, indent+1);
            }

            writer.write(
                    format("%send", calculateIndent(indent))
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String calculateIndent(int indent) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < indent; i++) {
            buf.append("  ");
        }

        return buf.toString();
    }

    protected void write(View view, Element element, Writer writer, int indent) {
        try {
            final String separator = System.lineSeparator();

            if (element instanceof ContainerInstance) {
                Container container = ((ContainerInstance) element).getContainer();
                writer.write(format("  %s%s(\"<div style='background: %s; color: %s'><b>%s</b><br /><sup>[%s]</sup><br />%s</div>\")",
                        calculateIndent(indent),
                        idOf(element),
                        backgroundOf(view, container),
                        colorOf(view, container),
                        container.getName(), typeOf(container), lines(container.getDescription())
                ));
            } else {
                writer.write(format("  %s%s(\"<div style='background: %s; color: %s'><b>%s</b><br /><sup>[%s]</sup><br />%s</div>\")",
                        calculateIndent(indent),
                        idOf(element),
                        backgroundOf(view, element),
                        colorOf(view, element),
                        element.getName(), typeOf(element), lines(element.getDescription())
                ));
            }
            writer.write(format("%s", separator));

            if (!StringUtils.isNullOrEmpty(element.getUrl())) {
                writer.write(format("click %s %s \"%s\"", idOf(element), element.getUrl(), element.getUrl()));
                writer.write(format("%s", separator));
            }

            if (element instanceof ContainerInstance) {
                Container container = ((ContainerInstance) element).getContainer();
                writer.write(format("  %sstyle %s fill:%s", calculateIndent(indent),idOf(element), backgroundOf(view, container)));
            } else {
                writer.write(format("  %sstyle %s fill:%s", calculateIndent(indent),idOf(element), backgroundOf(view, element)));
            }

            writer.write(format("%s", separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeSequenceElement(View view, Element element, Writer writer, boolean indent, String type) throws IOException {
        writer.write(format("%s%s \"%s\" as %s <<%s>> %s%s",
                indent ? "  " : "",
                type,
                element.getName(),
                idOf(element),
                typeOf(element),
                backgroundOf(view, element),
                System.lineSeparator()));
    }

    protected String lines(final String text) {
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

    protected String backgroundOf(View view, Element element) {
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getBackground();
    }

    protected String colorOf(View view, Element element) {
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getColor();
    }

    protected RelationshipStyle relationshipStyleOf(View view, Relationship relationship) {
        return view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship);
    }

    protected void writeRelationships(View view, Writer writer) {
        view.getRelationships().stream()
                .map(RelationshipView::getRelationship)
                .filter(r -> !(r.getSource() instanceof DeploymentNode || r.getDestination() instanceof DeploymentNode))
                .sorted((r1, r2) -> (r1.getSource().getName() + r1.getDestination().getName()).compareTo(r2.getSource().getName() + r2.getDestination().getName()))
                .forEach(r -> writeRelationship(view, r, writer));
    }

    protected void writeRelationship(View view, Relationship relationship, Writer writer) {
        try {
            RelationshipStyle style = relationshipStyleOf(view, relationship);
            // solid: A-- text -->B
            // dotted: A-. text .->B

            if (style.getDashed() == null) {
                style.setDashed(true);
            }

            writer.write(
                    format("  %s-%s \"<b>%s</b><br /><sup>%s</sup>\" %s->%s",
                            idOf(relationship.getSource()),
                            style.getDashed() ? "." : "-",
                            lines(relationship.getDescription()),
                            !StringUtils.isNullOrEmpty(relationship.getTechnology()) ? "[" + relationship.getTechnology() + "]" : "",
                            style.getDashed() ? "." : "-",
                            idOf(relationship.getDestination())
                    )
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String idOf(Element e) {
        return e.getId();
    }

    protected String typeOf(Element e) {
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

    protected boolean hasValue(String s) {
        return s != null && s.trim().length() > 0;
    }

    protected void writeHeader(View view, Writer writer) throws IOException {
        writer.write("graph TB");
        writer.write(System.lineSeparator());
    }

    protected void writeFooter(Writer writer) throws IOException {
        writer.write(System.lineSeparator());
    }

}
