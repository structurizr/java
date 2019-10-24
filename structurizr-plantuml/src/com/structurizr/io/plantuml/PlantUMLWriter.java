package com.structurizr.io.plantuml;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * A writer that outputs diagram definitions that can be used to create diagrams
 * using PlantUML (http://plantuml.com/plantuml/).
 *
 * System landscape, system context, container, component, dynamic and deployment diagrams are supported.
 *
 * Note: This won't work if you have two elements named the same on a diagram.
 */
public class PlantUMLWriter {

    private final Map<String, String> skinParams = new LinkedHashMap<>();
    private final List<String> includes = new ArrayList<>();

    /** maximum diagram width or height; defaults to 2000px to match public plantuml.com installation */
    private int sizeLimit = 2000;
    private boolean includeNotesForActors = true;
    private boolean useSequenceDiagrams = false;
    private String direction;

    /**
     * Creates a new PlantUMLWriter, with some default skin params.
     */
    public PlantUMLWriter() {
        // add some default skin params
        addSkinParam("shadowing", "false");
        addSkinParam("arrowColor", "#707070");
        addSkinParam("actorBorderColor", "#707070");
        addSkinParam("componentBorderColor", "#707070");
        addSkinParam("rectangleBorderColor", "#707070");
        addSkinParam("noteBackgroundColor", "#ffffff");
        addSkinParam("noteBorderColor", "#707070");
    }

    protected List<String> getIncludes() {
        return includes;
    }

    public void addIncludeFile(String file) {
        addIncludeFile(file, null);
    }

    public void addIncludeFile(String file, int id) {
        addIncludeFile(file, String.valueOf(id));
    }

    public void addIncludeFile(String file, String id) {
        if (id==null) {
            includes.add(format("!include %s", file));
        } else {
            includes.add(format("!include %s!%s", file, id));
        }
    }

    public void addIncludeURL(URI file) {
        addIncludeURL(file, null);
    }

    public void addIncludeURL(URI file, int id) {
        addIncludeURL(file, String.valueOf(id));
    }

    public void addIncludeURL(URI file, String id) {
        if (id==null) {
            includes.add(format("!includeurl %s", file));
        } else {
            includes.add(format("!includeurl %s!%s", file, id));
        }
    }

    public void clearIncludes() {
        includes.clear();
    }

    protected Map<String, String> getSkinParams() {
        return skinParams;
    }

    public void addSkinParam(String name, String value) {
        skinParams.put(name, value);
    }

    public void clearSkinParams() {
        skinParams.clear();
    }

    protected boolean isIncludeNotesForActors() {
        return includeNotesForActors;
    }

    public void setIncludeNotesForActors(boolean includeNotesForActors) {
        this.includeNotesForActors = includeNotesForActors;
    }

    public boolean isUseSequenceDiagrams() {
        return useSequenceDiagrams;
    }

    public void setUseSequenceDiagrams(boolean useSequenceDiagrams) {
        this.useSequenceDiagrams = useSequenceDiagrams;
    }

    protected int getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
        if (diagrams != null && diagrams.contains("@startuml")) {
            return stringWriter.toString().split("(?=@startuml)");
        } else {
            return new String[0];
        }
    }

    /**
     * Gets a single view as a PlantUML diagram definition.
     *
     * @param view      the view to write
     * @return          the PlantUML definition as a String
     */
    public String toString(View view) {
        StringWriter stringWriter = new StringWriter();
        write(view, stringWriter);
        return stringWriter.toString();
    }

    /**
     * Creates PlantUML diagram definitions based upon the specified workspace.
     *
     * @param workspace     the workspace containing the views to be written
     * @return  a collection of PlantUML diagram definitions, one per view
     */
    public Collection<PlantUMLDiagram> toPlantUMLDiagrams(Workspace workspace) {
        if (workspace == null) {
            throw new IllegalArgumentException("A workspace must be provided.");
        }

        Collection<PlantUMLDiagram> diagrams = new ArrayList<>();

        for (View view : workspace.getViews().getViews()) {
            StringWriter stringWriter = new StringWriter();
            write(view, stringWriter);

            diagrams.add(new PlantUMLDiagram(view.getKey(), view.getName(), stringWriter.toString()));
        }

        return diagrams;
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
                    .forEach(e -> write(view, e, writer, false));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() != Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, false));

            if (enterpriseBoundaryVisible) {
                String name = view.getModel().getEnterprise() != null ? view.getModel().getEnterprise().getName() : "Enterprise";
                writer.write("package \"" + name + "\" {");
                writer.write(System.lineSeparator());
            }

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, true));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, true));

            if (enterpriseBoundaryVisible) {
                writer.write("}");
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
                    .forEach(e -> write(view, e, writer, false));

            writer.write("package \"" + view.getSoftwareSystem().getName() + "\" <<" + typeOf(view.getSoftwareSystem()) + ">> {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Container)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, true));

            writer.write("}");
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
                    .forEach(e -> write(view, e, writer, false));

            writer.write("package \"" + view.getContainer().getName() + "\" <<" + typeOf(view.getContainer()) + ">> {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Component)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, true));

            writer.write("}");
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
                view.getElements().stream()
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getId().compareTo(e2.getId()))
                    .forEach(e -> {
                                   try {
                                       writeSequenceElement(view, e, writer, false, plantumlSequenceType(view, e));
                                   } catch (IOException e3) {
                                       e3.printStackTrace();
                                   }
                             });
            } else {
                view.getElements().stream()
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName())).
                    forEach(e -> write(view, e, writer, false));
            }
            view.getRelationships()
                    .forEach(relationship -> {
                        try {
                            writer.write(
                                    format("%s -[%s]> %s : %s. %s",
                                            idOf(relationship.getRelationship().getSource()),
                                            view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship.getRelationship()).getColor(),
                                            idOf(relationship.getRelationship().getDestination()),
                                            relationship.getOrder(),
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

    protected void write(DeploymentView view, Writer writer) {
        try {
            writeHeader(view, writer);

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof DeploymentNode && ev.getElement().getParent() == null)
                    .map(ev -> (DeploymentNode)ev.getElement())
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(view, e, writer, 0));

            writeRelationships(view, writer);

            writeFooter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(View view, DeploymentNode deploymentNode, Writer writer, int indent) {
        try {
            writer.write(
                    format("%snode \"%s\" <<%s>> as %s {",
                            calculateIndent(indent),
                            deploymentNode.getName() + (deploymentNode.getInstances() > 1 ? " (x" + deploymentNode.getInstances() + ")" : ""),
                            typeOf(deploymentNode),
                            idOf(deploymentNode)
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
                    format("%s}", calculateIndent(indent))
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void write(View view, ContainerInstance containerInstance, Writer writer, int indent) {
        try {
            writer.write(
                    format("%s%s \"%s\" <<%s>> as %s %s",
                            calculateIndent(indent),
                            plantumlType(view, containerInstance.getContainer()),
                            containerInstance.getContainer().getName(),
                            typeOf(containerInstance),
                            idOf(containerInstance),
                            backgroundOf(view, containerInstance.getContainer())
                    )
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

    protected void write(View view, Element element, Writer writer, boolean indent) {
        try {
            final String type = plantumlType(view, element);
            final List<String> description = lines(element.getDescription());

            if(description.isEmpty() || "actor".equals(type)) {
                writeSimpleElement(view, element, writer, indent, type);

                if (includeNotesForActors) {
                    writeDescriptionAsNote(element, writer, indent, description);
                }
            }
            else {
                final String prefix = indent ? "  " : "";
                final String separator = System.lineSeparator();
                final String id = idOf(element);

                writer.write(format("%s%s %s <<%s>> %s [%s",
                        prefix, type, id, typeOf(element), backgroundOf(view, element), separator));
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

    protected void writeSimpleElement(View view, Element element, Writer writer, boolean indent, String type) throws IOException {
        writer.write(format("%s%s \"%s\" <<%s>> as %s %s%s",
                indent ? "  " : "",
                type,
                element.getName(),
                typeOf(element),
                idOf(element),
                backgroundOf(view, element),
                System.lineSeparator()));
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

    protected void writeDescriptionAsNote(Element element, Writer writer, boolean indent, List<String> description) throws IOException {
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

    protected List<String> lines(final String text) {
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

    protected String backgroundOf(View view, Element element) {
        return view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getBackground();
    }

    protected String plantumlType(View view, Element element) {
        Shape shape = view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getShape();

        switch(shape) {
            case Box:
                return element instanceof Component ? "component" : "rectangle";
            case Person:
                return "actor";
            case Cylinder:
                return "database";
            case Folder:
                return "folder";
            case Ellipse:
            case Circle:
                return "storage";
            default:
                return "rectangle";
        }
    }

    protected String plantumlSequenceType(View view, Element element) {
        Shape shape = view.getViewSet().getConfiguration().getStyles().findElementStyle(element).getShape();

        switch(shape) {
            case Box:
                return "participant";
            case Person:
                return "actor";
            case Cylinder:
                return "database";
            case Folder:
                return "collections";
            case Ellipse:
            case Circle:
                return "entity";
            default:
                return "participant";
        }
    }

    protected void writeRelationships(View view, Writer writer) {
        view.getRelationships().stream()
                .map(RelationshipView::getRelationship)
                .sorted((r1, r2) -> (r1.getSource().getName() + r1.getDestination().getName()).compareTo(r2.getSource().getName() + r2.getDestination().getName()))
                .forEach(r -> writeRelationship(view, r, writer));
    }

    protected void writeRelationship(View view, Relationship relationship, Writer writer) {
        try {
            String stereotypeAndDescription =
                (hasValue(relationship.getTechnology()) ? "<<" + relationship.getTechnology() + ">>\\n" : "") +
                (hasValue(relationship.getDescription()) ? relationship.getDescription() : "");

            writer.write(
                    format("%s .[%s].> %s %s",
                            idOf(relationship.getSource()),
                            view.getViewSet().getConfiguration().getStyles().findRelationshipStyle(relationship).getColor(),
                            idOf(relationship.getDestination()),
                            hasValue(stereotypeAndDescription) ? ": " + stereotypeAndDescription : ""
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
        // Spaces in PlantUML ids can cause issues. Alternatively, id can be surrounded with double quotes
        writer.write(format("@startuml(id=%s)", view.getKey().replace(' ', '_')));
        writer.write(System.lineSeparator());

        for (String include : includes) {
            writer.write(include);
            writer.write(System.lineSeparator());
        }

        PaperSize size = view.getPaperSize();
        int width;
        int height;
        if(size==null) {
            width = height = sizeLimit;
        }
        else {
            width = size.getWidth();
            height = size.getHeight();
            if(width>sizeLimit || height>sizeLimit) {
                int max = Math.max(width, height);
                width = (width * sizeLimit) / max;
                height = (height * sizeLimit) / max;
            }
        }
        writer.write("scale max ");
        writer.write(Integer.toString(width));
        writer.write("x");
        writer.write(Integer.toString(height));
        writer.write(System.lineSeparator());

        if (direction != null && direction.trim().length() > 0) {
            writer.write(direction);
            writer.write(System.lineSeparator());
        }

        String viewTitle = view.getTitle();
        if (StringUtils.isNullOrEmpty(viewTitle)) {
            viewTitle = view.getName();
        }
        writer.write("title " + viewTitle);
        writer.write(System.lineSeparator());

        if (view.getDescription() != null && view.getDescription().trim().length() > 0) {
            writer.write("caption " + view.getDescription());
            writer.write(System.lineSeparator());
        }

        writer.write(System.lineSeparator());

        if (!skinParams.isEmpty()) {
            writer.write(format("skinparam {%s", System.lineSeparator()));
            for (final String name : skinParams.keySet()) {
                writer.write(format("  %s %s%s", name, skinParams.get(name), System.lineSeparator()));
            }
            writer.write(format("}%s", System.lineSeparator()));
        }
    }

    protected void writeFooter(Writer writer) throws IOException {
        writer.write("@enduml");
        writer.write(System.lineSeparator());
    }

}
