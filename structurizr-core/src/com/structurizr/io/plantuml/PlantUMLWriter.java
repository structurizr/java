package com.structurizr.io.plantuml;

import com.structurizr.Workspace;
import com.structurizr.io.WorkspaceWriter;
import com.structurizr.io.WorkspaceWriterException;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * A simple PlantUML writer that outputs diagram definitions that can be copy-pasted
 * into http://plantuml.com/plantuml/ ... it supports enterprise context, system context,
 * container, component and dynamic diagrams.
 *
 * Note: This won't work if you have two elements named the same on a diagram.
 */
public final class PlantUMLWriter implements WorkspaceWriter {

    @Override
    public void write(Workspace workspace, Writer writer) throws WorkspaceWriterException {
        if (workspace != null && writer != null) {
            workspace.getViews().getEnterpriseContextViews().forEach(v -> write(v, writer));
            workspace.getViews().getSystemContextViews().forEach(v -> write(v, writer));
            workspace.getViews().getContainerViews().forEach(v -> write(v, writer));
            workspace.getViews().getComponentViews().forEach(v -> write(v, writer));
            workspace.getViews().getDynamicViews().forEach(v -> write(v, writer));
        }
    }

    public void write(View view, Writer writer) {
        if (view != null && writer != null) {
            if (EnterpriseContextView.class.isAssignableFrom(view.getClass())) {
                write((EnterpriseContextView) view, writer);
            } else if (SystemContextView.class.isAssignableFrom(view.getClass())) {
                write((SystemContextView) view, writer);
            } else if (ContainerView.class.isAssignableFrom(view.getClass())) {
                write((ContainerView) view, writer);
            } else if (ComponentView.class.isAssignableFrom(view.getClass())) {
                write((ComponentView) view, writer);
            } else if (DynamicView.class.isAssignableFrom(view.getClass())) {
                write((DynamicView) view, writer);
            }
        }
    }

    private void write(EnterpriseContextView view, Writer writer) {
        try {
            writer.write("@startuml");
            writer.write(System.lineSeparator());

            writer.write("title " + view.getName());
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.External)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, false));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.External)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, false));

            writer.write("package " + nameOf(view.getModel().getEnterprise().getName()) + " {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof Person && ((Person)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, true));

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .filter(e -> e instanceof SoftwareSystem && ((SoftwareSystem)e).getLocation() == Location.Internal)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writer.write("@enduml");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SystemContextView view, Writer writer) {
        try {
            writer.write("@startuml");
            writer.write(System.lineSeparator());

            writer.write("title " + view.getName());
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, false));
            write(view.getRelationships(), writer);

            writer.write("@enduml");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(ContainerView view, Writer writer) {
        try {
            writer.write("@startuml");
            writer.write(System.lineSeparator());

            writer.write("title " + view.getName());
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Container))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, false));

            writer.write("package " + nameOf(view.getSoftwareSystem()) + " {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Container)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writer.write("@enduml");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(ComponentView view, Writer writer) {
        try {
            writer.write("@startuml");
            writer.write(System.lineSeparator());

            writer.write("title " + view.getName());
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> !(ev.getElement() instanceof Component))
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, false));

            writer.write("package " + nameOf(view.getContainer()) + " {");
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Component)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(e -> write(e, writer, true));

            writer.write("}");
            writer.write(System.lineSeparator());

            write(view.getRelationships(), writer);

            writer.write("@enduml");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(DynamicView view, Writer writer) {
        try {
            writer.write("@startuml");
            writer.write(System.lineSeparator());

            writer.write("title " + view.getName());
            writer.write(System.lineSeparator());

            view.getElements().stream()
                    .filter(ev -> ev.getElement() instanceof Person)
                    .map(ElementView::getElement)
                    .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                    .forEach(element -> {
                        try {
                            writer.write("actor " + nameOf(element));
                            writer.write(System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            view.getRelationships().stream()
                    .sorted((rv1, rv2) -> (rv1.getOrder().compareTo(rv2.getOrder())))
                    .forEach(relationship -> {
                        try {
                            writer.write(
                                    String.format("%s -> %s : %s",
                                            nameOf(relationship.getRelationship().getSource()),
                                            nameOf(relationship.getRelationship().getDestination()),
                                            hasValue(relationship.getDescription()) ? relationship.getDescription() : hasValue(relationship.getRelationship().getDescription()) ? relationship.getRelationship().getDescription() : ""
                                    )
                            );
                            writer.write(System.lineSeparator());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            writer.write("@enduml");
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Element element, Writer writer, boolean indent) {
        try {
            if (element instanceof Person) {
                writer.write(
                        String.format("%sactor %s",
                                indent ? "  " : "",
                                nameOf(element)
                        )
                );
            } else {
                writer.write(
                        String.format("%s[%s] <<%s>> as %s",
                                indent ? "  " : "",
                                element.getName(),
                                typeOf(element),
                                nameOf(element)
                        )
                );
            }
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    String.format("%s ..> %s %s%s",
                            nameOf(relationship.getSource()),
                            nameOf(relationship.getDestination()),
                            hasValue(relationship.getDescription()) ? ": " + relationship.getDescription() : "",
                            hasValue(relationship.getTechnology()) ? " <<" + relationship.getTechnology() + ">>" : ""
                    )
            );
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } else {
            return e.getClass().getSimpleName();
        }
    }

    private boolean hasValue(String s) {
        return s != null && s.trim().length() > 0;
    }

}