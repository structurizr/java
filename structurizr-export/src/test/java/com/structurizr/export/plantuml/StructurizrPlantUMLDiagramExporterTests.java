package com.structurizr.export.plantuml;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StructurizrPlantUMLDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void systemLandscapeView_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A", "Description.");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B", "Description.");
        a.uses(b, "Description", "Technology");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as A
                rectangle "==B\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as B
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "Description\\n<size:16>[Technology]</size>"
                                
                @enduml""", diagram.getDefinition());

        assertEquals("""
                @startuml
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // transparent element for relationships in legend
                  .Element-Transparent {
                    BackgroundColor: transparent;
                    LineColor: transparent;
                    FontColor: transparent;
                  }
                </style>
                                
                rectangle "==Element" <<Element-RWxlbWVudA==>>
                                
                rectangle "." <<.Element-Transparent>> as 1
                1 --> 1 <<Relationship-UmVsYXRpb25zaGlw>> : "Relationship"
                                
                @enduml""", diagram.getLegend().getDefinition());
    }

    @Test
    public void systemLandscapeView_NoStyling_Dark() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A", "Description.");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B", "Description.");
        a.uses(b, "Description", "Technology");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Dark);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #111111;
                    FontColor: #cccccc;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #cccccc;
                    FontColor: #cccccc;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as A
                rectangle "==B\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as B
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "Description\\n<size:16>[Technology]</size>"
                                
                @enduml""", diagram.getDefinition());

        assertEquals("""
                @startuml
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #111111;
                    FontColor: #cccccc;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #cccccc;
                    FontColor: #cccccc;
                    FontSize: 24;
                  }
                  // transparent element for relationships in legend
                  .Element-Transparent {
                    BackgroundColor: transparent;
                    LineColor: transparent;
                    FontColor: transparent;
                  }
                </style>
                                
                rectangle "==Element" <<Element-RWxlbWVudA==>>
                                
                rectangle "." <<.Element-Transparent>> as 1
                1 --> 1 <<Relationship-UmVsYXRpb25zaGlw>> : "Relationship"
                                
                @enduml""", diagram.getLegend().getDefinition());
    }

    @Test
    public void systemContextView_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A", "Description.");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B", "Description.");
        a.uses(b, "Description", "Technology");

        SystemContextView view = workspace.getViews().createSystemContextView(a, "key", "Description");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>System Context View: A</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as A
                rectangle "==B\\n<size:16>[Software System]</size>\\n\\nDescription." <<Element-RWxlbWVudA==>> as B
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "Description\\n<size:16>[Technology]</size>"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void containerView_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("Software System A", "Description.");
        Container container1 = a.addContainer("Container 1", "Description", "Technology");
        Container container2 = a.addContainer("Container 2", "Description", "Technology");
        container1.uses(container2, "Description", "Technology");

        ContainerView view = workspace.getViews().createContainerView(a, "key", "Description");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>Container View: Software System A</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Software System A
                  .Boundary-U29mdHdhcmUgU3lzdGVtIEE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Software System A\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIEE=>> {
                  rectangle "==Container 1\\n<size:16>[Container: Technology]</size>\\n\\nDescription" <<Element-RWxlbWVudA==>> as SoftwareSystemA.Container1
                  rectangle "==Container 2\\n<size:16>[Container: Technology]</size>\\n\\nDescription" <<Element-RWxlbWVudA==>> as SoftwareSystemA.Container2
                }
                                
                SoftwareSystemA.Container1 --> SoftwareSystemA.Container2 <<Relationship-UmVsYXRpb25zaGlw>> : "Description\\n<size:16>[Technology]</size>"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void componentView_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("Software System", "Description.");
        Container container = a.addContainer("Container", "Description", "Technology");
        Component component1 = container.addComponent("Component 1", "Description", "Technology");
        Component component2 = container.addComponent("Component 2", "Description", "Technology");
        component1.uses(component2, "Description", "Technology");

        ComponentView view = workspace.getViews().createComponentView(container, "key", "Description");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>Component View: Software System - Container</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Container
                  .Boundary-Q29udGFpbmVy {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Container\\n<size:16>[Container: Technology]</size>" <<Boundary-Q29udGFpbmVy>> {
                  rectangle "==Component 1\\n<size:16>[Component: Technology]</size>\\n\\nDescription" <<Element-RWxlbWVudA==>> as SoftwareSystem.Container.Component1
                  rectangle "==Component 2\\n<size:16>[Component: Technology]</size>\\n\\nDescription" <<Element-RWxlbWVudA==>> as SoftwareSystem.Container.Component2
                }
                                
                SoftwareSystem.Container.Component1 --> SoftwareSystem.Container.Component2 <<Relationship-UmVsYXRpb25zaGlw>> : "Description\\n<size:16>[Technology]</size>"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void deploymentView_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        DeploymentNode node1 = workspace.getModel().addDeploymentNode("Node 1");
        node1.addDeploymentNode("Node 2").add(a);
        node1.addDeploymentNode("Node 3").addInfrastructureNode("Infrastructure Node");

        DeploymentView view = workspace.getViews().createDeploymentView("deployment", "Default");
        view.addDefaultElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>Deployment View: Default</size>\\n<size:24>Default</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                </style>
                                
                rectangle "Node 1\\n<size:16>[Deployment Node]</size>" <<Element-RWxlbWVudA==>> as Default.Node1 {
                  rectangle "Node 2\\n<size:16>[Deployment Node]</size>" <<Element-RWxlbWVudA==>> as Default.Node1.Node2 {
                    rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Default.Node1.Node2.A_1
                  }
                                
                  rectangle "Node 3\\n<size:16>[Deployment Node]</size>" <<Element-RWxlbWVudA==>> as Default.Node1.Node3 {
                    rectangle "==Infrastructure Node\\n<size:16>[Infrastructure Node]</size>" <<Element-RWxlbWVudA==>> as Default.Node1.Node3.InfrastructureNode
                  }
                                
                }
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_CollaborationStyle_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");

        a.uses(b, "Uses");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(a, b);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>Dynamic View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as A
                rectangle "==B\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as B
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_CollaborationStyle_Frames() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        SoftwareSystem c = workspace.getModel().addSoftwareSystem("C");

        a.uses(b, "Uses");
        b.uses(c, "Uses");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(a, b);
        view.add(b, c);
        view.addProperty(StructurizrPlantUMLExporter.PLANTUML_ANIMATION_PROPERTY, "true");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        List<Diagram> frames = exporter.export(view).getFrames();
        assertEquals(2, frames.size());

        assertEquals("""
                @startuml
                title <size:24>Dynamic View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as A
                rectangle "==B\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as B
                rectangle "==C\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as C
                hide C
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                B --> C <<Relationship-UmVsYXRpb25zaGlw>> : "2: Uses"
                                
                @enduml""", frames.get(0).getDefinition());

        assertEquals("""
                @startuml
                title <size:24>Dynamic View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as A
                hide A
                rectangle "==B\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as B
                rectangle "==C\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as C

                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                B --> C <<Relationship-UmVsYXRpb25zaGlw>> : "2: Uses"
                                
                @enduml""", frames.get(1).getDefinition());
    }

    @Test
    public void dynamicView_SequenceStyle_NoStyling_Light() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");

        a.uses(b, "Uses", "JSON/HTTPS");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(a, b);
        view.addProperty(StructurizrPlantUMLExporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "true");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);

        assertEquals("""
                @startuml
                title <size:24>Dynamic View</size>\\n<size:24>Description</size>
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                participant "A\\n<size:16>[Software System]</size>" as A <<Element-RWxlbWVudA==>> #ffffff
                participant "B\\n<size:16>[Software System]</size>" as B <<Element-RWxlbWVudA==>> #ffffff
                                
                A -> B <<Relationship-UmVsYXRpb25zaGlw>> : 1: Uses\\n<size:16>[JSON/HTTPS]</size>
                                
                @enduml""", diagram.getDefinition());

        assertEquals("""
                @startuml
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // transparent element for relationships in legend
                  .Element-Transparent {
                    BackgroundColor: transparent;
                    LineColor: transparent;
                    FontColor: transparent;
                  }
                </style>
                                
                rectangle "==Element" <<Element-RWxlbWVudA==>>
                                
                rectangle "." <<.Element-Transparent>> as 1
                1 --> 1 <<Relationship-UmVsYXRpb25zaGlw>> : "Relationship"
                                
                @enduml""", diagram.getLegend().getDefinition());
    }

    @Test
    public void groups() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/groups.json"));

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(4, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                                
                set separator none
                top to bottom direction
                skinparam ranksep 60
                skinparam nodesep 30
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Group 1
                  .Group-R3JvdXAgMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2
                  .Group-R3JvdXAgMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2/Group 3
                  .Group-R3JvdXAgMi9Hcm91cCAz {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Group 1" <<Group-R3JvdXAgMQ==>> as groupR3JvdXAgMQ== {
                  rectangle "==B\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as B
                }
                                
                rectangle "Group 2" <<Group-R3JvdXAgMg==>> as groupR3JvdXAgMg== {
                  rectangle "==C\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as C
                    rectangle "Group 3" <<Group-R3JvdXAgMi9Hcm91cCAz>> as groupR3JvdXAgMi9Hcm91cCAz {
                      rectangle "==D\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as D
                    }
                                
                }
                                
                rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as A
                                
                B --> C <<Relationship-UmVsYXRpb25zaGlw>> : ""
                C --> D <<Relationship-UmVsYXRpb25zaGlw>> : ""
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : ""
                                
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Container View: D</size>
                                
                set separator none
                top to bottom direction
                skinparam ranksep 60
                skinparam nodesep 30
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // D
                  .Boundary-RA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 4
                  .Group-R3JvdXAgNA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "==C\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as C
                                
                rectangle "D\\n<size:16>[Software System]</size>" <<Boundary-RA==>> {
                  rectangle "Group 4" <<Group-R3JvdXAgNA==>> as groupR3JvdXAgNA== {
                    rectangle "==F\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as D.F
                  }
                                
                  rectangle "==E\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as D.E
                }
                                
                C --> D.E <<Relationship-UmVsYXRpb25zaGlw>> : ""
                C --> D.F <<Relationship-UmVsYXRpb25zaGlw>> : ""
                                
                @enduml""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Component View: D - F</size>
                                
                set separator none
                top to bottom direction
                skinparam ranksep 60
                skinparam nodesep 30
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // F
                  .Boundary-Rg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 5
                  .Group-R3JvdXAgNQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "==C\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as C
                                
                rectangle "F\\n<size:16>[Container]</size>" <<Boundary-Rg==>> {
                  rectangle "Group 5" <<Group-R3JvdXAgNQ==>> as groupR3JvdXAgNQ== {
                    rectangle "==H\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as D.F.H
                  }
                                
                  rectangle "==G\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as D.F.G
                }
                                
                C --> D.F.G <<Relationship-UmVsYXRpb25zaGlw>> : ""
                C --> D.F.H <<Relationship-UmVsYXRpb25zaGlw>> : ""
                D.F.G --> D.F.H <<Relationship-UmVsYXRpb25zaGlw>> : ""
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void nestedGroups() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");

        SoftwareSystem a = workspace.getModel().addSoftwareSystem("Team 1");
        a.setGroup("Organisation 1/Department 1/Team 1");

        SoftwareSystem b = workspace.getModel().addSoftwareSystem("Team 2");
        b.setGroup("Organisation 1/Department 1/Team 2");

        SoftwareSystem c = workspace.getModel().addSoftwareSystem("Organisation 1");
        c.setGroup("Organisation 1");

        SoftwareSystem d = workspace.getModel().addSoftwareSystem("Organisation 2");
        d.setGroup("Organisation 2");

        SoftwareSystem e = workspace.getModel().addSoftwareSystem("Department 1");
        e.setGroup("Organisation 1/Department 1");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape");
        view.addAllElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Organisation 1/Department 1/Team 1").color("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Organisation 1/Department 1/Team 2").color("#0000ff");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Organisation 1
                  .Group-T3JnYW5pc2F0aW9uIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Organisation 1/Department 1
                  .Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAx {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Organisation 1/Department 1/Team 1
                  .Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #ff0000;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Organisation 1/Department 1/Team 2
                  .Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #0000ff;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Organisation 2
                  .Group-T3JnYW5pc2F0aW9uIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Organisation 1" <<Group-T3JnYW5pc2F0aW9uIDE=>> as groupT3JnYW5pc2F0aW9uIDE= {
                  rectangle "==Organisation 1\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Organisation1
                    rectangle "Department 1" <<Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAx>> as groupT3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAx {
                      rectangle "==Department 1\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Department1
                        rectangle "Team 1" <<Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMQ==>> as groupT3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMQ== {
                          rectangle "==Team 1\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Team1
                        }
                                
                        rectangle "Team 2" <<Group-T3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMg==>> as groupT3JnYW5pc2F0aW9uIDEvRGVwYXJ0bWVudCAxL1RlYW0gMg== {
                          rectangle "==Team 2\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Team2
                        }
                                
                    }
                                
                }
                                
                rectangle "Organisation 2" <<Group-T3JnYW5pc2F0aW9uIDI=>> as groupT3JnYW5pc2F0aW9uIDI= {
                  rectangle "==Organisation 2\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Organisation2
                }
                                
                                
                @enduml""", diagram.getDefinition());
    }


    @Test
    public void test_Sequence() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/sequence.json"));
        ThemeUtils.loadThemes(workspace);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(2, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("Sequence-Container")).findFirst().get();
        String actual = """
                @startuml
                title <size:24>Dynamic View: d - e</size>
                                
                set separator none
                !pragma teoz true
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                box "A\\n[Software System]"
                    box "B\\n[Container]"
                      participant "C\\n<size:16>[Component]</size>" as A.B.C <<Element-RWxlbWVudA==>> #ffffff
                    end box
                                
                  end box
                                
                box "d\\n[Software System]"
                    box "e\\n[Container]"
                      participant "f\\n<size:16>[Component]</size>" as d.e.f <<Element-RWxlbWVudA==>> #ffffff
                      participant "g\\n<size:16>[Component]</size>" as d.e.g <<Element-RWxlbWVudA==>> #ffffff
                      participant "h\\n<size:16>[Component]</size>" as d.e.h <<Element-RWxlbWVudA==>> #ffffff
                    end box
                                
                  end box
                                
                d.e.f -> d.e.h <<Relationship-UmVsYXRpb25zaGlw>> : 1:\s
                A.B.C -> d.e.f <<Relationship-UmVsYXRpb25zaGlw>> : 2:\s
                d.e.f -> d.e.g <<Relationship-UmVsYXRpb25zaGlw>> : 3:\s
                                
                @enduml""";

        assertEquals(actual, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Sequence-SoftwareSystem")).findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: A</size>
                               
                set separator none
                !pragma teoz true
                hide stereotype
                               
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                               
                box "A\\n[Software System]"
                  participant "B\\n<size:16>[Container]</size>" as A.B <<Element-RWxlbWVudA==>> #ffffff
                end box
                               
                box "d\\n[Software System]"
                  participant "e\\n<size:16>[Container]</size>" as d.e <<Element-RWxlbWVudA==>> #ffffff
                end box
                               
                A.B -> d.e <<Relationship-UmVsYXRpb25zaGlw>> : 1:\s
                               
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void containerDiagramWithExternalContainers() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem1, "Containers", "");
        containerView.add(container1);
        containerView.add(container2);

        Diagram diagram = new StructurizrPlantUMLExporter().export(containerView);
        assertEquals("""
                @startuml
                title <size:24>Container View: Software System 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Software System 1
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 2
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Software System 1\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDE=>> {
                  rectangle "==Container 1\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1
                }
                                
                rectangle "Software System 2\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDI=>> {
                  rectangle "==Container 2\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2
                }
                                
                SoftwareSystem1.Container1 --> SoftwareSystem2.Container2 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void componentDiagramWithExternalComponents() {
        Workspace workspace = new Workspace("Name");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        Component component2 = container1.addComponent("Component 2");

        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component3 = container2.addComponent("Component 3");

        component1.uses(component2, "Uses");
        component2.uses(component3, "Uses");

        ComponentView componentView = workspace.getViews().createComponentView(container1, "Components", "");
        componentView.add(component1);
        componentView.add(component2);
        componentView.add(component3);

        Diagram diagram = new StructurizrPlantUMLExporter().export(componentView);
        assertEquals("""
                @startuml
                title <size:24>Component View: Software System 1 - Container 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Container 1
                  .Boundary-Q29udGFpbmVyIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Container 2
                  .Boundary-Q29udGFpbmVyIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Container 1\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDE=>> {
                  rectangle "==Component 1\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component1
                  rectangle "==Component 2\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component2
                }
                                
                rectangle "Container 2\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDI=>> {
                  rectangle "==Component 3\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2.Component3
                }
                                
                SoftwareSystem1.Container1.Component1 --> SoftwareSystem1.Container1.Component2 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                SoftwareSystem1.Container1.Component2 --> SoftwareSystem2.Container2.Component3 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void componentDiagramWithExternalComponentsAndSoftwareSystemBoundariesIncluded() {
        Workspace workspace = new Workspace("Name");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        Component component2 = container1.addComponent("Component 2");

        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component3 = container2.addComponent("Component 3");

        component1.uses(component2, "Uses");
        component2.uses(component3, "Uses");

        ComponentView componentView = workspace.getViews().createComponentView(container1, "Components", "");
        componentView.add(component1);
        componentView.add(component2);
        componentView.add(component3);
        componentView.addProperty("structurizr.softwareSystemBoundaries", "true");

        Diagram diagram = new StructurizrPlantUMLExporter().export(componentView);
        assertEquals("""
                @startuml
                title <size:24>Component View: Software System 1 - Container 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Container 1
                  .Boundary-Q29udGFpbmVyIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Container 2
                  .Boundary-Q29udGFpbmVyIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 1
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 2
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Software System 1\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDE=>> {
                    rectangle "Container 1\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDE=>> {
                      rectangle "==Component 1\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component1
                      rectangle "==Component 2\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component2
                    }
                                
                  }
                                
                rectangle "Software System 2\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDI=>> {
                    rectangle "Container 2\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDI=>> {
                      rectangle "==Component 3\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2.Component3
                    }
                                
                  }
                                
                SoftwareSystem1.Container1.Component1 --> SoftwareSystem1.Container1.Component2 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                SoftwareSystem1.Container1.Component2 --> SoftwareSystem2.Container2.Component3 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_ExternalContainers() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem1, "Dynamic", "");
        dynamicView.add(container1, container2);

        Diagram diagram = new StructurizrPlantUMLExporter().export(dynamicView);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Software System 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Software System 1
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 2
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Software System 1\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDE=>> {
                  rectangle "==Container 1\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1
                }
                                
                rectangle "Software System 2\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDI=>> {
                  rectangle "==Container 2\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2
                }
                                
                SoftwareSystem1.Container1 --> SoftwareSystem2.Container2 <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_ExternalComponents() {
        Workspace workspace = new Workspace("Name");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        Component component2 = container1.addComponent("Component 2");

        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component3 = container2.addComponent("Component 3");

        component1.uses(component2, "Uses");
        component2.uses(component3, "Uses");

        DynamicView dynamicView = workspace.getViews().createDynamicView(container1, "Dynamic", "");
        dynamicView.add(component1, component2);
        dynamicView.add(component2, component3);

        Diagram diagram = new StructurizrPlantUMLExporter().export(dynamicView);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Software System 1 - Container 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Container 1
                  .Boundary-Q29udGFpbmVyIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Container 2
                  .Boundary-Q29udGFpbmVyIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Container 1\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDE=>> {
                  rectangle "==Component 1\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component1
                  rectangle "==Component 2\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component2
                }
                                
                rectangle "Container 2\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDI=>> {
                  rectangle "==Component 3\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2.Component3
                }
                                
                SoftwareSystem1.Container1.Component1 --> SoftwareSystem1.Container1.Component2 <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                SoftwareSystem1.Container1.Component2 --> SoftwareSystem2.Container2.Component3 <<Relationship-UmVsYXRpb25zaGlw>> : "2: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_ExternalComponentsAndSoftwareSystemBoundariesIncluded() {
        Workspace workspace = new Workspace("Name");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        Component component2 = container1.addComponent("Component 2");

        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component3 = container2.addComponent("Component 3");

        component1.uses(component2, "Uses");
        component2.uses(component3, "Uses");

        DynamicView dynamicView = workspace.getViews().createDynamicView(container1, "Dynamic", "");
        dynamicView.add(component1, component2);
        dynamicView.add(component2, component3);
        dynamicView.addProperty("structurizr.softwareSystemBoundaries", "true");

        Diagram diagram = new StructurizrPlantUMLExporter().export(dynamicView);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Software System 1 - Container 1</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Container 1
                  .Boundary-Q29udGFpbmVyIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Container 2
                  .Boundary-Q29udGFpbmVyIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 1
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDE= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Software System 2
                  .Boundary-U29mdHdhcmUgU3lzdGVtIDI= {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Software System 1\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDE=>> {
                    rectangle "Container 1\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDE=>> {
                      rectangle "==Component 1\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component1
                      rectangle "==Component 2\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem1.Container1.Component2
                    }
                                
                  }
                                
                rectangle "Software System 2\\n<size:16>[Software System]</size>" <<Boundary-U29mdHdhcmUgU3lzdGVtIDI=>> {
                    rectangle "Container 2\\n<size:16>[Container]</size>" <<Boundary-Q29udGFpbmVyIDI=>> {
                      rectangle "==Component 3\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem2.Container2.Component3
                    }
                                
                  }
                                
                SoftwareSystem1.Container1.Component1 --> SoftwareSystem1.Container1.Component2 <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                SoftwareSystem1.Container1.Component2 --> SoftwareSystem2.Container2.Component3 <<Relationship-UmVsYXRpb25zaGlw>> : "2: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void elementUrls() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        softwareSystem.setUrl("https://structurizr.com");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertTrue(diagram.getDefinition().contains("as SoftwareSystem [[https://structurizr.com]]"));
    }

    @Test
    public void elementInstanceUrl() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        a.setUrl("https://example.com/url1");
        SoftwareSystemInstance aInstance = workspace.getModel().addDeploymentNode("Node").add(a);

        DeploymentView view = workspace.getViews().createDeploymentView("deployment", "Default");
        view.add(aInstance);

        assertTrue(new StructurizrPlantUMLExporter().export(view).getDefinition().contains("as Default.Node.A_1 [[https://example.com/url1]]"));

        aInstance.setUrl("https://example.com/url2");
        assertTrue(new StructurizrPlantUMLExporter().export(view).getDefinition().contains("as Default.Node.A_1 [[https://example.com/url2]]"));
    }

    @Test
    public void newLineCharacterInElementName() {
        Workspace workspace = new Workspace("Name");
        workspace.getModel().addSoftwareSystem("Software\nSystem");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                </style>
                                
                rectangle "==Software\\nSystem\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as SoftwareSystem
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void customView() {
        Workspace workspace = new Workspace("Name");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        CustomElement b = model.addCustomElement("B", "Custom", "Description");
        a.uses(b, "Uses");

        CustomView view = workspace.getViews().createCustomView("key", "Title");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("""
                @startuml
                title <size:24>Title</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "==A" <<Element-RWxlbWVudA==>> as 1
                rectangle "==B\\n<size:16>[Custom]</size>\\n\\nDescription" <<Element-RWxlbWVudA==>> as 2
                                
                1 --> 2 <<Relationship-UmVsYXRpb25zaGlw>> : "Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    void renderWorkspaceWithUnicodeElementName() {
        Workspace workspace = new Workspace("Name");
        workspace.getModel().addPerson("Пользователь");
        workspace.getViews().createSystemLandscapeView("key", "Description").addDefaultElements();

        String diagramDefinition = new StructurizrPlantUMLExporter().export(workspace).stream().findFirst().get().getDefinition();

        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>\\n<size:24>Description</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                </style>
                                
                rectangle "==Пользователь\\n<size:16>[Person]</size>" <<Element-RWxlbWVudA==>> as Пользователь
                                
                @enduml""", diagramDefinition);
    }

    @Test
    public void font() {
        Workspace workspace = new Workspace("Name");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.addAllElements();
        workspace.getViews().getConfiguration().getBranding().setFont(new Font("Courier"));

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertTrue(diagram.getDefinition().contains("""
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                    FontName: Courier;
                  }"""));

        assertTrue(diagram.getLegend().getDefinition().contains("""
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                    FontName: Courier;
                  }"""));
    }

    @Test
    public void include() {
        Workspace workspace = new Workspace("Name");
        workspace.getModel().addSoftwareSystem("A");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.addDefaultElements();

        view.getViewSet().getConfiguration().addProperty(StructurizrPlantUMLExporter.PLANTUML_INCLUDES_PROPERTY, "styles.puml");

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        System.out.println(diagram.getDefinition());
        assertTrue(diagram.getDefinition().contains("!include styles.puml\n"));
    }

    @Test
    public void dynamicView_UnscopedWithGroups() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        softwareSystemA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        softwareSystemB.setGroup("Group 2");
        softwareSystemA.uses(softwareSystemB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView("key");
        view.add(softwareSystemA, softwareSystemB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // Group 1
                  .Group-R3JvdXAgMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2
                  .Group-R3JvdXAgMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Group 1" <<Group-R3JvdXAgMQ==>> as groupR3JvdXAgMQ== {
                  rectangle "==A\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as A
                }
                                
                rectangle "Group 2" <<Group-R3JvdXAgMg==>> as groupR3JvdXAgMg== {
                  rectangle "==B\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as B
                }
                                
                                
                A --> B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_SoftwareSystemScopedWithGroups() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        Container containerA = softwareSystemA.addContainer("A");
        containerA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        Container containerB = softwareSystemB.addContainer("B");
        containerB.setGroup("Group 2");
        containerA.uses(containerB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView(softwareSystemA, "key");
        view.add(containerA, containerB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: A</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // A
                  .Boundary-QQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // B
                  .Boundary-Qg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 1
                  .Group-R3JvdXAgMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2
                  .Group-R3JvdXAgMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "A\\n<size:16>[Software System]</size>" <<Boundary-QQ==>> {
                  rectangle "Group 1" <<Group-R3JvdXAgMQ==>> as groupR3JvdXAgMQ== {
                    rectangle "==A\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as A.A
                  }
                                
                }
                                
                rectangle "B\\n<size:16>[Software System]</size>" <<Boundary-Qg==>> {
                  rectangle "Group 2" <<Group-R3JvdXAgMg==>> as groupR3JvdXAgMg== {
                    rectangle "==B\\n<size:16>[Container]</size>" <<Element-RWxlbWVudA==>> as B.B
                  }
                                
                }
                                
                A.A --> B.B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void dynamicView_ContainerScopedWithGroups() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        Container containerA = softwareSystemA.addContainer("A");
        Component componentA = containerA.addComponent("A");
        componentA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        Container containerB = softwareSystemB.addContainer("B");
        Component componentB = containerB.addComponent("B");
        componentB.setGroup("Group 2");
        componentA.uses(componentB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView(containerA, "key");
        view.add(componentA, componentB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>Dynamic View: A - A</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // A
                  .Boundary-QQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // B
                  .Boundary-Qg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 1
                  .Group-R3JvdXAgMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2
                  .Group-R3JvdXAgMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "A\\n<size:16>[Container]</size>" <<Boundary-QQ==>> {
                  rectangle "Group 1" <<Group-R3JvdXAgMQ==>> as groupR3JvdXAgMQ== {
                    rectangle "==A\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as A.A.A
                  }
                                
                }
                                
                rectangle "B\\n<size:16>[Container]</size>" <<Boundary-Qg==>> {
                  rectangle "Group 2" <<Group-R3JvdXAgMg==>> as groupR3JvdXAgMg== {
                    rectangle "==B\\n<size:16>[Component]</size>" <<Element-RWxlbWVudA==>> as B.B.B
                  }
                                
                }
                                
                A.A.A --> B.B.B <<Relationship-UmVsYXRpb25zaGlw>> : "1: Uses"
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    public void deploymentView_WithGroups() {
        Workspace workspace = new Workspace("Name", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");

        DeploymentNode server1 = workspace.getModel().addDeploymentNode("Server 1");
        server1.setGroup("Group 1");

        InfrastructureNode infrastructureNode1 = server1.addInfrastructureNode("Infrastructure Node 1");
        InfrastructureNode infrastructureNode2 = server1.addInfrastructureNode("Infrastructure Node 2");

        SoftwareSystemInstance softwareSystemInstance = server1.add(softwareSystem);
        softwareSystemInstance.setGroup("Group 2");
        infrastructureNode2.setGroup("Group 2");

        DeploymentView view = workspace.getViews().createDeploymentView("key");
        view.add(infrastructureNode1);
        view.add(infrastructureNode2);
        view.add(softwareSystemInstance);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>Deployment View: Default</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Group 1
                  .Group-R3JvdXAgMQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                  // Group 2
                  .Group-R3JvdXAgMg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Group 1" <<Group-R3JvdXAgMQ==>> as groupR3JvdXAgMQ== {
                  rectangle "Server 1\\n<size:16>[Deployment Node]</size>" <<Element-RWxlbWVudA==>> as Default.Server1 {
                    rectangle "Group 2" <<Group-R3JvdXAgMg==>> as groupR3JvdXAgMg== {
                      rectangle "==Infrastructure Node 2\\n<size:16>[Infrastructure Node]</size>" <<Element-RWxlbWVudA==>> as Default.Server1.InfrastructureNode2
                      rectangle "==Software System\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Default.Server1.SoftwareSystem_1
                    }
                                
                    rectangle "==Infrastructure Node 1\\n<size:16>[Infrastructure Node]</size>" <<Element-RWxlbWVudA==>> as Default.Server1.InfrastructureNode1
                  }
                                
                }
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    void light_group() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.setGroup("Name");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.add(softwareSystem);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Name
                  .Group-TmFtZQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Name" <<Group-TmFtZQ==>> as groupTmFtZQ== {
                  rectangle "==Name\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Name
                }
                                
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    void dark_group() {
        Workspace workspace = new Workspace("Name");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.setGroup("Name");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key");
        view.add(softwareSystem);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Dark);
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                title <size:24>System Landscape View</size>
                                
                set separator none
                top to bottom direction
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #111111;
                    FontColor: #cccccc;
                  }
                  // Element
                  .Element-RWxlbWVudA== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Name
                  .Group-TmFtZQ== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 2-2;
                    LineThickness: 2;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                  }
                </style>
                                
                rectangle "Name" <<Group-TmFtZQ==>> as groupTmFtZQ== {
                  rectangle "==Name\\n<size:16>[Software System]</size>" <<Element-RWxlbWVudA==>> as Name
                }
                                
                                
                @enduml""", diagram.getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    public void amazonWebServicesExample_Light() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        ThemeUtils.loadThemes(workspace);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Light);
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: X - Live</size>
                                
                set separator none
                left to right direction
                skinparam ranksep 60
                skinparam nodesep 30
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element,Amazon Web Services - Auto Scaling
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n {
                    BackgroundColor: #ffffff;
                    LineColor: #cc2264;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cc2264;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Cloud
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ= {
                    BackgroundColor: #ffffff;
                    LineColor: #232f3e;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #232f3e;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - EC2
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy {
                    BackgroundColor: #ffffff;
                    LineColor: #d86613;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #d86613;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Elastic Load Balancing
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw== {
                    BackgroundColor: #ffffff;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - RDS
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT {
                    BackgroundColor: #ffffff;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - RDS MySQL instance
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl {
                    BackgroundColor: #ffffff;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Region
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u {
                    BackgroundColor: #ffffff;
                    LineColor: #147eba;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #147eba;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Route 53
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM= {
                    BackgroundColor: #ffffff;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Application
                  .Element-RWxlbWVudCxBcHBsaWNhdGlvbg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    RoundCorner: 20;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Database
                  .Element-RWxlbWVudCxEYXRhYmFzZQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "Amazon Web Services\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ=>> as Live.AmazonWebServices {
                  rectangle "US-East-1\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/region.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u>> as Live.AmazonWebServices.USEast1 {
                    rectangle "Autoscaling group\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-auto-scaling.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n>> as Live.AmazonWebServices.USEast1.Autoscalinggroup {
                      rectangle "Amazon EC2 - Ubuntu server\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-ec2.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy>> as Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver {
                        rectangle "==Web Application\\n<size:16>[Container: Java and Spring Boot]</size>" <<Element-RWxlbWVudCxBcHBsaWNhdGlvbg==>> as Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1
                      }
                                
                    }
                                
                    rectangle "Amazon RDS\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT>> as Live.AmazonWebServices.USEast1.AmazonRDS {
                      rectangle "MySQL\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds-mysql-instance.png{scale=0.36}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl>> as Live.AmazonWebServices.USEast1.AmazonRDS.MySQL {
                        database "==Database Schema\\n<size:16>[Container]</size>" <<Element-RWxlbWVudCxEYXRhYmFzZQ==>> as Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1
                      }
                                
                    }
                                
                    rectangle "==DNS router\\n<size:16>[Infrastructure Node: Route 53]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-route-53.png{scale=0.24}>\\n\\nRoutes incoming requests based upon domain name." <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM=>> as Live.AmazonWebServices.USEast1.DNSrouter
                    rectangle "==Load Balancer\\n<size:16>[Infrastructure Node: Elastic Load Balancer]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/elastic-load-balancing.png{scale=0.24}>\\n\\nAutomatically distributes incoming application traffic." <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw==>> as Live.AmazonWebServices.USEast1.LoadBalancer
                  }
                                
                }
                                
                Live.AmazonWebServices.USEast1.LoadBalancer --> Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1 <<Relationship-UmVsYXRpb25zaGlw>> : "Forwards requests to\\n<size:16>[HTTPS]</size>"
                Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1 --> Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1 <<Relationship-UmVsYXRpb25zaGlw>> : "Reads from and writes to\\n<size:16>[MySQL Protocol/SSL]</size>"
                Live.AmazonWebServices.USEast1.DNSrouter --> Live.AmazonWebServices.USEast1.LoadBalancer <<Relationship-UmVsYXRpb25zaGlw>> : "Forwards requests to\\n<size:16>[HTTPS]</size>"
                                
                @enduml""", diagram.getDefinition());

        assertEquals("""
                @startuml
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element,Amazon Web Services - Auto Scaling
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n {
                    BackgroundColor: #ffffff;
                    LineColor: #cc2264;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cc2264;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - Cloud
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ= {
                    BackgroundColor: #ffffff;
                    LineColor: #232f3e;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #232f3e;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - EC2
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy {
                    BackgroundColor: #ffffff;
                    LineColor: #d86613;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #d86613;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - Elastic Load Balancing
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw== {
                    BackgroundColor: #ffffff;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - RDS
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT {
                    BackgroundColor: #ffffff;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - RDS MySQL instance
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl {
                    BackgroundColor: #ffffff;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - Region
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u {
                    BackgroundColor: #ffffff;
                    LineColor: #147eba;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #147eba;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Amazon Web Services - Route 53
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM= {
                    BackgroundColor: #ffffff;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Application
                  .Element-RWxlbWVudCxBcHBsaWNhdGlvbg== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    RoundCorner: 20;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Element,Database
                  .Element-RWxlbWVudCxEYXRhYmFzZQ== {
                    BackgroundColor: #ffffff;
                    LineColor: #444444;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #444444;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 200;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                  // transparent element for relationships in legend
                  .Element-Transparent {
                    BackgroundColor: transparent;
                    LineColor: transparent;
                    FontColor: transparent;
                  }
                </style>
                                
                rectangle "==Amazon Web Services - Auto Scaling\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-auto-scaling.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n>>
                                
                rectangle "==Amazon Web Services - Cloud\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ=>>
                                
                rectangle "==Amazon Web Services - EC2\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-ec2.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy>>
                                
                rectangle "==Amazon Web Services - Elastic Load Balancing\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/elastic-load-balancing.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw==>>
                                
                rectangle "==Amazon Web Services - RDS\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT>>
                                
                rectangle "==Amazon Web Services - RDS MySQL instance\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds-mysql-instance.png{scale=0.36}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl>>
                                
                rectangle "==Amazon Web Services - Region\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/region.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u>>
                                
                rectangle "==Amazon Web Services - Route 53\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-route-53.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM=>>
                                
                rectangle "==Application" <<Element-RWxlbWVudCxBcHBsaWNhdGlvbg==>>
                                
                database "==Database" <<Element-RWxlbWVudCxEYXRhYmFzZQ==>>
                                
                rectangle "." <<.Element-Transparent>> as 1
                1 --> 1 <<Relationship-UmVsYXRpb25zaGlw>> : "Relationship"
                                
                @enduml""", diagram.getLegend().getDefinition());
    }

    @Test
    @Tag("IntegrationTest")
    public void amazonWebServicesExample_Dark() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        ThemeUtils.loadThemes(workspace);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter(ColorScheme.Dark);
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                @startuml
                title <size:24>Deployment View: X - Live</size>
                                
                set separator none
                left to right direction
                skinparam ranksep 60
                skinparam nodesep 30
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #111111;
                    FontColor: #cccccc;
                  }
                  // Element,Amazon Web Services - Auto Scaling
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n {
                    BackgroundColor: #111111;
                    LineColor: #cc2264;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cc2264;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Cloud
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ= {
                    BackgroundColor: #111111;
                    LineColor: #232f3e;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #232f3e;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - EC2
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy {
                    BackgroundColor: #111111;
                    LineColor: #d86613;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #d86613;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Elastic Load Balancing
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw== {
                    BackgroundColor: #111111;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - RDS
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT {
                    BackgroundColor: #111111;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - RDS MySQL instance
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl {
                    BackgroundColor: #111111;
                    LineColor: #3b48cc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #3b48cc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Region
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u {
                    BackgroundColor: #111111;
                    LineColor: #147eba;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #147eba;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Amazon Web Services - Route 53
                  .Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM= {
                    BackgroundColor: #111111;
                    LineColor: #693cc5;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #693cc5;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Application
                  .Element-RWxlbWVudCxBcHBsaWNhdGlvbg== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 0;
                    LineThickness: 2;
                    RoundCorner: 20;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Database
                  .Element-RWxlbWVudCxEYXRhYmFzZQ== {
                    BackgroundColor: #111111;
                    LineColor: #cccccc;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #cccccc;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #cccccc;
                    FontColor: #cccccc;
                    FontSize: 24;
                  }
                </style>
                                
                rectangle "Amazon Web Services\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-cloud.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQ2xvdWQ=>> as Live.AmazonWebServices {
                  rectangle "US-East-1\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/region.png{scale=0.5142857142857142}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUmVnaW9u>> as Live.AmazonWebServices.USEast1 {
                    rectangle "Autoscaling group\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/aws-auto-scaling.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gQXV0byBTY2FsaW5n>> as Live.AmazonWebServices.USEast1.Autoscalinggroup {
                      rectangle "Amazon EC2 - Ubuntu server\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-ec2.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRUMy>> as Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver {
                        rectangle "==Web Application\\n<size:16>[Container: Java and Spring Boot]</size>" <<Element-RWxlbWVudCxBcHBsaWNhdGlvbg==>> as Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1
                      }
                                
                    }
                                
                    rectangle "Amazon RDS\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds.png{scale=0.24}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRT>> as Live.AmazonWebServices.USEast1.AmazonRDS {
                      rectangle "MySQL\\n<size:16>[Deployment Node]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-rds-mysql-instance.png{scale=0.36}>" <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUkRTIE15U1FMIGluc3RhbmNl>> as Live.AmazonWebServices.USEast1.AmazonRDS.MySQL {
                        database "==Database Schema\\n<size:16>[Container]</size>" <<Element-RWxlbWVudCxEYXRhYmFzZQ==>> as Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1
                      }
                                
                    }
                                
                    rectangle "==DNS router\\n<size:16>[Infrastructure Node: Route 53]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/amazon-route-53.png{scale=0.24}>\\n\\nRoutes incoming requests based upon domain name." <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gUm91dGUgNTM=>> as Live.AmazonWebServices.USEast1.DNSrouter
                    rectangle "==Load Balancer\\n<size:16>[Infrastructure Node: Elastic Load Balancer]</size>\\n\\n<img:https://static.structurizr.com/themes/amazon-web-services-2020.04.30/elastic-load-balancing.png{scale=0.24}>\\n\\nAutomatically distributes incoming application traffic." <<Element-RWxlbWVudCxBbWF6b24gV2ViIFNlcnZpY2VzIC0gRWxhc3RpYyBMb2FkIEJhbGFuY2luZw==>> as Live.AmazonWebServices.USEast1.LoadBalancer
                  }
                                
                }
                                
                Live.AmazonWebServices.USEast1.LoadBalancer --> Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1 <<Relationship-UmVsYXRpb25zaGlw>> : "Forwards requests to\\n<size:16>[HTTPS]</size>"
                Live.AmazonWebServices.USEast1.Autoscalinggroup.AmazonEC2Ubuntuserver.WebApplication_1 --> Live.AmazonWebServices.USEast1.AmazonRDS.MySQL.DatabaseSchema_1 <<Relationship-UmVsYXRpb25zaGlw>> : "Reads from and writes to\\n<size:16>[MySQL Protocol/SSL]</size>"
                Live.AmazonWebServices.USEast1.DNSrouter --> Live.AmazonWebServices.USEast1.LoadBalancer <<Relationship-UmVsYXRpb25zaGlw>> : "Forwards requests to\\n<size:16>[HTTPS]</size>"
                                
                @enduml""", diagram.getDefinition());

        assertEquals("""
                @startuml
                title <size:24>Dynamic View: Internet Banking System - API Application</size>\\n<size:24>Summarises how the sign in feature works in the single-page application.</size>
                                
                set separator none
                hide stereotype
                                
                <style>
                  root {
                    BackgroundColor: #ffffff;
                    FontColor: #444444;
                  }
                  // Element,Component
                  .Element-RWxlbWVudCxDb21wb25lbnQ= {
                    BackgroundColor: #85bbf0;
                    LineColor: #5d82a8;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #000000;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Container,Database
                  .Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U= {
                    BackgroundColor: #438dd5;
                    LineColor: #2e6295;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #ffffff;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Element,Container,Web Browser
                  .Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI= {
                    BackgroundColor: #438dd5;
                    LineColor: #2e6295;
                    LineStyle: 0;
                    LineThickness: 2;
                    FontColor: #ffffff;
                    FontSize: 24;
                    HorizontalAlignment: center;
                    Shadowing: 0;
                    MaximumWidth: 450;
                  }
                  // Relationship
                  .Relationship-UmVsYXRpb25zaGlw {
                    LineThickness: 2;
                    LineStyle: 10-10;
                    LineColor: #444444;
                    FontColor: #444444;
                    FontSize: 24;
                  }
                </style>
                                
                box "API Application\\n[Container: Java and Spring MVC]"
                  participant "Sign In Controller\\n<size:16>[Component: Spring MVC Rest Controller]</size>" as InternetBankingSystem.APIApplication.SignInController <<Element-RWxlbWVudCxDb21wb25lbnQ=>> #85bbf0
                  participant "Security Component\\n<size:16>[Component: Spring Bean]</size>" as InternetBankingSystem.APIApplication.SecurityComponent <<Element-RWxlbWVudCxDb21wb25lbnQ=>> #85bbf0
                end box
                                
                participant "Single-Page Application\\n<size:16>[Container: JavaScript and Angular]</size>" as InternetBankingSystem.SinglePageApplication <<Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI=>> #438dd5
                database "Database\\n<size:16>[Container: Oracle Database Schema]</size>" as InternetBankingSystem.Database <<Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U=>> #438dd5
                                
                InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : 1: Submits credentials to\\n<size:16>[JSON/HTTPS]</size>
                InternetBankingSystem.APIApplication.SignInController -> InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : 2: Validates credentials using
                InternetBankingSystem.APIApplication.SecurityComponent -> InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : 3: select * from users where username = ?\\n<size:16>[SQL/TCP]</size>
                InternetBankingSystem.APIApplication.SecurityComponent <-- InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : 4: Returns user data to\\n<size:16>[SQL/TCP]</size>
                InternetBankingSystem.APIApplication.SignInController <-- InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : 5: Returns true if the hashed password matches
                InternetBankingSystem.SinglePageApplication <-- InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : 6: Sends back an authentication token to\\n<size:16>[JSON/HTTPS]</size>
                                
                @enduml""", diagram.getLegend().getDefinition());
    }

    @Test
    public void test_BigBankPlcExample_StructurizrPlantUML_Seq() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/big-bank-plc.json"));
        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        assertEquals("""
               @startuml
               title <size:24>Dynamic View: Internet Banking System - API Application</size>\\n<size:24>Summarises how the sign in feature works in the single-page application.</size>
               
               set separator none
               top to bottom direction
               skinparam ranksep 60
               skinparam nodesep 30
               hide stereotype
               
               <style>
                 root {
                   BackgroundColor: #ffffff;
                   FontColor: #444444;
                 }
                 // Element,Component
                 .Element-RWxlbWVudCxDb21wb25lbnQ= {
                   BackgroundColor: #85bbf0;
                   LineColor: #5d82a8;
                   LineStyle: 0;
                   LineThickness: 2;
                   FontColor: #000000;
                   FontSize: 24;
                   HorizontalAlignment: center;
                   Shadowing: 0;
                   MaximumWidth: 450;
                 }
                 // Element,Container,Database
                 .Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U= {
                   BackgroundColor: #438dd5;
                   LineColor: #2e6295;
                   LineStyle: 0;
                   LineThickness: 2;
                   FontColor: #ffffff;
                   FontSize: 24;
                   HorizontalAlignment: center;
                   Shadowing: 0;
                   MaximumWidth: 450;
                 }
                 // Element,Container,Web Browser
                 .Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI= {
                   BackgroundColor: #438dd5;
                   LineColor: #2e6295;
                   LineStyle: 0;
                   LineThickness: 2;
                   FontColor: #ffffff;
                   FontSize: 24;
                   HorizontalAlignment: center;
                   Shadowing: 0;
                   MaximumWidth: 450;
                 }
                 // Relationship
                 .Relationship-UmVsYXRpb25zaGlw {
                   LineThickness: 2;
                   LineStyle: 10-10;
                   LineColor: #444444;
                   FontColor: #444444;
                   FontSize: 24;
                 }
                 // API Application
                 .Boundary-QVBJIEFwcGxpY2F0aW9u {
                   BackgroundColor: #438dd5;
                   LineColor: #2e6295;
                   LineStyle: 0;
                   LineThickness: 2;
                   FontColor: #ffffff;
                   FontSize: 24;
                   HorizontalAlignment: center;
                   Shadowing: 0;
                 }
               </style>
               
               rectangle "API Application\\n<size:16>[Container: Java and Spring MVC]</size>" <<Boundary-QVBJIEFwcGxpY2F0aW9u>> {
                 rectangle "==Sign In Controller\\n<size:16>[Component: Spring MVC Rest Controller]</size>\\n\\nAllows users to sign in to the Internet Banking System." <<Element-RWxlbWVudCxDb21wb25lbnQ=>> as InternetBankingSystem.APIApplication.SignInController
                 rectangle "==Security Component\\n<size:16>[Component: Spring Bean]</size>\\n\\nProvides functionality related to signing in, changing passwords, etc." <<Element-RWxlbWVudCxDb21wb25lbnQ=>> as InternetBankingSystem.APIApplication.SecurityComponent
               }
               
               rectangle "==Single-Page Application\\n<size:16>[Container: JavaScript and Angular]</size>\\n\\nProvides all of the Internet banking functionality to customers via their web browser." <<Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI=>> as InternetBankingSystem.SinglePageApplication
               database "==Database\\n<size:16>[Container: Oracle Database Schema]</size>\\n\\nStores user registration information, hashed authentication credentials, access logs, etc." <<Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U=>> as InternetBankingSystem.Database
               
               InternetBankingSystem.SinglePageApplication --> InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : "1: Submits credentials to\\n<size:16>[JSON/HTTPS]</size>"
               InternetBankingSystem.APIApplication.SignInController --> InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : "2: Validates credentials using"
               InternetBankingSystem.APIApplication.SecurityComponent --> InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : "3: select * from users where username = ?\\n<size:16>[SQL/TCP]</size>"
               InternetBankingSystem.APIApplication.SecurityComponent <-- InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : "4: Returns user data to\\n<size:16>[SQL/TCP]</size>"
               InternetBankingSystem.APIApplication.SignInController <-- InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : "5: Returns true if the hashed password matches"
               InternetBankingSystem.SinglePageApplication <-- InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : "6: Sends back an authentication token to\\n<size:16>[JSON/HTTPS]</size>"
               
               @enduml""", diagram.getDefinition());

        // and the sequence diagram version
        workspace.getViews().getConfiguration().addProperty(exporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "true");
        diagrams = exporter.export(workspace);
        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
            assertEquals("""
                    @startuml
                    title <size:24>Dynamic View: Internet Banking System - API Application</size>\\n<size:24>Summarises how the sign in feature works in the single-page application.</size>
                                     
                    set separator none
                    hide stereotype
                                     
                    <style>
                      root {
                        BackgroundColor: #ffffff;
                        FontColor: #444444;
                      }
                      // Element,Component
                      .Element-RWxlbWVudCxDb21wb25lbnQ= {
                        BackgroundColor: #85bbf0;
                        LineColor: #5d82a8;
                        LineStyle: 0;
                        LineThickness: 2;
                        FontColor: #000000;
                        FontSize: 24;
                        HorizontalAlignment: center;
                        Shadowing: 0;
                        MaximumWidth: 450;
                      }
                      // Element,Container,Database
                      .Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U= {
                        BackgroundColor: #438dd5;
                        LineColor: #2e6295;
                        LineStyle: 0;
                        LineThickness: 2;
                        FontColor: #ffffff;
                        FontSize: 24;
                        HorizontalAlignment: center;
                        Shadowing: 0;
                        MaximumWidth: 450;
                      }
                      // Element,Container,Web Browser
                      .Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI= {
                        BackgroundColor: #438dd5;
                        LineColor: #2e6295;
                        LineStyle: 0;
                        LineThickness: 2;
                        FontColor: #ffffff;
                        FontSize: 24;
                        HorizontalAlignment: center;
                        Shadowing: 0;
                        MaximumWidth: 450;
                      }
                      // Relationship
                      .Relationship-UmVsYXRpb25zaGlw {
                        LineThickness: 2;
                        LineStyle: 10-10;
                        LineColor: #444444;
                        FontColor: #444444;
                        FontSize: 24;
                      }
                    </style>
                                     
                    box "API Application\\n[Container: Java and Spring MVC]"
                      participant "Sign In Controller\\n<size:16>[Component: Spring MVC Rest Controller]</size>" as InternetBankingSystem.APIApplication.SignInController <<Element-RWxlbWVudCxDb21wb25lbnQ=>> #85bbf0
                      participant "Security Component\\n<size:16>[Component: Spring Bean]</size>" as InternetBankingSystem.APIApplication.SecurityComponent <<Element-RWxlbWVudCxDb21wb25lbnQ=>> #85bbf0
                    end box
                                     
                    participant "Single-Page Application\\n<size:16>[Container: JavaScript and Angular]</size>" as InternetBankingSystem.SinglePageApplication <<Element-RWxlbWVudCxDb250YWluZXIsV2ViIEJyb3dzZXI=>> #438dd5
                    database "Database\\n<size:16>[Container: Oracle Database Schema]</size>" as InternetBankingSystem.Database <<Element-RWxlbWVudCxDb250YWluZXIsRGF0YWJhc2U=>> #438dd5
                                     
                    InternetBankingSystem.SinglePageApplication -> InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : 1: Submits credentials to\\n<size:16>[JSON/HTTPS]</size>
                    InternetBankingSystem.APIApplication.SignInController -> InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : 2: Validates credentials using
                    InternetBankingSystem.APIApplication.SecurityComponent -> InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : 3: select * from users where username = ?\\n<size:16>[SQL/TCP]</size>
                    InternetBankingSystem.APIApplication.SecurityComponent <-- InternetBankingSystem.Database <<Relationship-UmVsYXRpb25zaGlw>> : 4: Returns user data to\\n<size:16>[SQL/TCP]</size>
                    InternetBankingSystem.APIApplication.SignInController <-- InternetBankingSystem.APIApplication.SecurityComponent <<Relationship-UmVsYXRpb25zaGlw>> : 5: Returns true if the hashed password matches
                    InternetBankingSystem.SinglePageApplication <-- InternetBankingSystem.APIApplication.SignInController <<Relationship-UmVsYXRpb25zaGlw>> : 6: Sends back an authentication token to\\n<size:16>[JSON/HTTPS]</size>
                                     
                    @enduml""", diagram.getDefinition());
    }
}