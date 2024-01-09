package com.structurizr.export.plantuml;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.ModelView;
import com.structurizr.view.Shape;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

public abstract class AbstractPlantUMLExporter extends AbstractDiagramExporter {

    public static final String PLANTUML_TITLE_PROPERTY = "plantuml.title";
    public static final String PLANTUML_INCLUDES_PROPERTY = "plantuml.includes";
    public static final String PLANTUML_ANIMATION_PROPERTY = "plantuml.animation";
    public static final String PLANTUML_SEQUENCE_DIAGRAM_PROPERTY = "plantuml.sequenceDiagram";

    private static final double MAX_ICON_SIZE = 30.0;

    private final Map<String, String> skinParams = new LinkedHashMap<>();

    protected Map<String, String> getSkinParams() {
        return skinParams;
    }

    public void addSkinParam(String name, String value) {
        skinParams.put(name, value);
    }

    public void clearSkinParams() {
        skinParams.clear();
    }

    String plantUMLShapeOf(ModelView view, Element element) {
        Shape shape = findElementStyle(view, element).getShape();

        return plantUMLShapeOf(shape);
    }

    String plantUMLShapeOf(Shape shape) {
        switch(shape) {
            case Person:
            case Robot:
                return "person";
            case Component:
                return "component";
            case Cylinder:
                return "database";
            case Folder:
                return "folder";
            case Ellipse:
            case Circle:
                return "storage";
            case Hexagon:
                return "hexagon";
            case Pipe:
                return "queue";
            default:
                return "rectangle";
        }
    }

    String plantumlSequenceType(ModelView view, Element element) {
        Shape shape = findElementStyle(view, element).getShape();

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

    String idOf(ModelItem modelItem) {
        if (modelItem instanceof Element) {
            Element element = (Element)modelItem;
            if (element.getParent() == null) {
                if (element instanceof DeploymentNode) {
                    DeploymentNode dn = (DeploymentNode)element;
                    return filter(dn.getEnvironment()) + "." + id(dn);
                } else {
                    return id(element);
                }
            } else {
                return idOf(element.getParent()) + "." + id(modelItem);
            }
        }

        return id(modelItem);
    }

    private String id(ModelItem modelItem) {
        if (modelItem instanceof Person) {
            return id((Person)modelItem);
        } else  if (modelItem instanceof SoftwareSystem) {
            return id((SoftwareSystem)modelItem);
        } else  if (modelItem instanceof Container) {
            return id((Container)modelItem);
        } else  if (modelItem instanceof Component) {
            return id((Component)modelItem);
        } else  if (modelItem instanceof DeploymentNode) {
            return id((DeploymentNode)modelItem);
        } else  if (modelItem instanceof InfrastructureNode) {
            return id((InfrastructureNode)modelItem);
        } else  if (modelItem instanceof SoftwareSystemInstance) {
            return id((SoftwareSystemInstance)modelItem);
        } else  if (modelItem instanceof ContainerInstance) {
            return id((ContainerInstance)modelItem);
        }

        return modelItem.getId();
    }

    private String id(Person person) {
        return filter(person.getName());
    }

    private String id(SoftwareSystem softwareSystem) {
        return filter(softwareSystem.getName());
    }

    private String id(Container container) {
        return filter(container.getName());
    }

    private String id(Component component) {
        return filter(component.getName());
    }

    private String id(DeploymentNode deploymentNode) {
        return filter(deploymentNode.getName());
    }

    private String id(InfrastructureNode infrastructureNode) {
        return filter(infrastructureNode.getName());
    }

    private String id(SoftwareSystemInstance softwareSystemInstance) {
        return filter(softwareSystemInstance.getName()) + "_" + softwareSystemInstance.getInstanceId();
    }

    private String id(ContainerInstance containerInstance) {
        return filter(containerInstance.getName()) + "_" + containerInstance.getInstanceId();
    }

    private String filter(String s) {
        return s.replaceAll("(?U)\\W", "");
    }

    protected boolean includeTitle(ModelView view) {
        return "true".equals(getViewOrViewSetProperty(view, PLANTUML_TITLE_PROPERTY, "true"));
    }

    @Override
    protected boolean isAnimationSupported(ModelView view) {
        return "true".equalsIgnoreCase(getViewOrViewSetProperty(view, PLANTUML_ANIMATION_PROPERTY, "false"));
    }

    @Override
    protected void writeHeader(ModelView view, IndentingWriter writer) {
        writer.writeLine("@startuml");
        writer.writeLine("set separator none");

        if (includeTitle(view)) {
            String viewTitle = view.getTitle();
            if (StringUtils.isNullOrEmpty(viewTitle)) {
                viewTitle = view.getName();
            }
            writer.writeLine("title " + viewTitle);
        }

        writer.writeLine();
    }

    protected void writeSkinParams(IndentingWriter writer) {
        if (!skinParams.isEmpty()) {
            writer.writeLine("skinparam {");
            writer.indent();
            for (final String name : skinParams.keySet()) {
                writer.writeLine(format("%s %s", name, skinParams.get(name)));
            }
            writer.outdent();
            writer.writeLine("}");
        }
    }

    protected void writeIncludes(ModelView view, IndentingWriter writer) {
        String[] includes = getViewOrViewSetProperty(view, PLANTUML_INCLUDES_PROPERTY, "").split(",");
        for (String include : includes) {
            if (!StringUtils.isNullOrEmpty(include)) {
                include = include.trim();
                writer.writeLine("!include " + include);
            }
        }
    }

    @Override
    protected void writeFooter(ModelView view, IndentingWriter writer) {
        writer.writeLine("@enduml");
    }

    @Override
    protected Diagram createDiagram(ModelView view, String definition) {
        return new PlantUMLDiagram(view, definition);
    }

    protected boolean elementStyleHasSupportedIcon(ElementStyle elementStyle) {
        return !StringUtils.isNullOrEmpty(elementStyle.getIcon()) && elementStyle.getIcon().startsWith("http");
    }

    protected double calculateIconScale(String iconUrl) {
        double scale = 0.5;

        try {
            URL url = new URL(iconUrl);
            BufferedImage bi = ImageIO.read(url);

            int width = bi.getWidth();
            int height = bi.getHeight();

            scale = MAX_ICON_SIZE / Math.max(width, height);
        } catch (UnsupportedOperationException | UnsatisfiedLinkError | IIOException e) {
            // This is a known issue on native builds since AWT packages aren't available.
            // So we just swallow the error and use the default scale
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scale;
    }

}
