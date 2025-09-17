package com.structurizr.export.plantuml;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ElementStyle;
import com.structurizr.view.ModelView;
import com.structurizr.view.Shape;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public abstract class AbstractPlantUMLExporter extends AbstractDiagramExporter {

    protected static final int DEFAULT_FONT_SIZE = 24;

    public static final String PLANTUML_TITLE_PROPERTY = "plantuml.title";
    public static final String PLANTUML_INCLUDES_PROPERTY = "plantuml.includes";
    public static final String PLANTUML_ANIMATION_PROPERTY = "plantuml.animation";
    public static final String PLANTUML_SEQUENCE_DIAGRAM_PROPERTY = "plantuml.sequenceDiagram";

    public static final String DIAGRAM_TITLE_TAG = "Diagram:Title";
    public static final String DIAGRAM_DESCRIPTION_TAG = "Diagram:Description";

    public AbstractPlantUMLExporter() {
        this(ColorScheme.Light);
    }

    public AbstractPlantUMLExporter(ColorScheme colorScheme) {
        super(colorScheme);
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

        if (includeTitle(view)) {
            ElementStyle titleStyle = findElementStyle(view, DIAGRAM_TITLE_TAG);
            ElementStyle descriptionStyle = findElementStyle(view, DIAGRAM_DESCRIPTION_TAG);

            String title = view.getTitle();
            if (StringUtils.isNullOrEmpty(title)) {
                title = view.getName();
            }

            String description = view.getDescription();
            if (StringUtils.isNullOrEmpty(description)) {
                writer.writeLine(
                        String.format(
                                "title <size:%s>%s</size>",
                                titleStyle != null ? titleStyle.getFontSize() : DEFAULT_FONT_SIZE,
                                title
                        )
                );
            } else {
                writer.writeLine(
                        String.format(
                                "title <size:%s>%s</size>\\n<size:%s>%s</size>",
                                titleStyle != null ? titleStyle.getFontSize() : DEFAULT_FONT_SIZE,
                                title,
                                descriptionStyle != null ? descriptionStyle.getFontSize() : DEFAULT_FONT_SIZE,
                                description
                        )
                );
            }
        }

        writer.writeLine();
        writer.writeLine("set separator none");
    }

    protected void writeIncludes(ModelView view, IndentingWriter writer) {
        String commaSeparatedIncludes = getViewOrViewSetProperty(view, PLANTUML_INCLUDES_PROPERTY, "");
        if (!StringUtils.isNullOrEmpty(commaSeparatedIncludes)) {
            String[] includes = commaSeparatedIncludes.split(",");

            for (String include : includes) {
                if (!StringUtils.isNullOrEmpty(include)) {
                    include = include.trim();
                    writer.writeLine("!include " + include);
                }
            }
            writer.writeLine();
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

    protected boolean isSupportedIcon(String icon) {
        return !StringUtils.isNullOrEmpty(icon) && icon.startsWith("http");
    }

    protected double calculateIconScale(String iconUrl, int maxIconSize) {
        double scale = 0.5;

        try {
            URL url = new URL(iconUrl);
            BufferedImage bi = ImageIO.read(url);

            int width = bi.getWidth();
            int height = bi.getHeight();

            scale = ((double)maxIconSize) / Math.max(width, height);
        } catch (UnsupportedOperationException | UnsatisfiedLinkError | IIOException e) {
            // This is a known issue on native builds since AWT packages aren't available.
            // So we just swallow the error and use the default scale
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scale;
    }

}
