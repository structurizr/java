package com.structurizr.export.plantuml;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class StructurizrPlantUMLDiagramExporterTests extends AbstractExporterTests {

    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/structurizr-36141-workspace.json"));
        workspace.getViews().getConfiguration().addProperty(StructurizrPlantUMLExporter.PLANTUML_ANIMATION_PROPERTY, "true");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();

        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-SystemLandscape.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(0, diagram.getFrames().size());

        //assertEquals("", diagram.getLegend().getDefinition());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SystemContext")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-SystemContext.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(4, diagram.getFrames().size());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-Containers.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(6, diagram.getFrames().size());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-Components.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(4, diagram.getFrames().size());

        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-SignIn.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(6, diagram.getFrames().size());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-DevelopmentDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(3, diagram.getFrames().size());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-LiveDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());
        assertEquals(5, diagram.getFrames().size());

        // and the sequence diagram version
        workspace.getViews().getConfiguration().addProperty(exporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "true");
        diagrams = exporter.export(workspace);
        diagram = diagrams.stream().filter(d -> d.getKey().equals("SignIn")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/36141-SignIn-sequence.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/structurizr-54915-workspace.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/54915-AmazonWebServicesDeployment.puml"));
        assertEquals(expected, diagram.getDefinition());

        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/54915-AmazonWebServicesDeployment-Legend.puml"));
        assertEquals(expected, diagram.getLegend().getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/groups.json"));
        ThemeUtils.loadThemes(workspace);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(4, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/groups-SystemLandscape.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/groups-Containers.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/groups-Components.puml"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Dynamic")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/groups-Dynamic.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_Sequence() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/sequence.json"));
        ThemeUtils.loadThemes(workspace);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        //assertEquals(2, diagrams.size());
        
        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("Sequence-Container")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/sequence-Container.puml"));
        assertEquals(expected, diagram.getDefinition());

//        diagram = diagrams.stream().filter(md -> md.getKey().equals("Sequence-SoftwareSystem")).findFirst().get();
//        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/sequence-SoftwareSystem.puml"));
//        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_NestedGroupsExample() throws Exception {
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

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description");
        view.addAllElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Organisation 1/Department 1/Team 1").color("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Organisation 1/Department 1/Team 2").color("#0000ff");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/nested-groups.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderGroupStyles() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User 1").setGroup("Group 1");
        workspace.getModel().addPerson("User 2").setGroup("Group 2");
        workspace.getModel().addPerson("User 3").setGroup("Group 3");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "");
        view.addDefaultElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 1").color("#111111").icon("https://example.com/icon1.png");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 2").color("#222222").icon("https://example.com/icon2.png");

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter() {
            @Override
            protected double calculateIconScale(String iconUrl) {
                return 1.0;
            }
        };

        Diagram diagram = exporter.export(view);
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/group-styles-1.puml"));
        assertEquals(expected, diagram.getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group").color("#aabbcc");

        diagram = exporter.export(view);
        expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/group-styles-2.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderContainerDiagramWithExternalContainers() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem1, "Containers", "");
        containerView.add(container1);
        containerView.add(container2);

        Diagram diagram = new StructurizrPlantUMLExporter().export(containerView);
        assertEquals("@startuml\n" +
                "set separator none\n" +
                "title Software System 1 - Containers\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "skinparam {\n" +
                "  arrowFontSize 10\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 200\n" +
                "  maxMessageSize 100\n" +
                "}\n" +
                "\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<SoftwareSystem1.Container1>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem2.Container2>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem1>> {\n" +
                "  BorderColor #9a9a9a\n" +
                "  FontColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem2>> {\n" +
                "  BorderColor #9a9a9a\n" +
                "  FontColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "\n" +
                "rectangle \"Software System 1\\n<size:10>[Software System]</size>\" <<SoftwareSystem1>> {\n" +
                "  rectangle \"==Container 1\\n<size:10>[Container]</size>\" <<SoftwareSystem1.Container1>> as SoftwareSystem1.Container1\n" +
                "}\n" +
                "\n" +
                "rectangle \"Software System 2\\n<size:10>[Software System]</size>\" <<SoftwareSystem2>> {\n" +
                "  rectangle \"==Container 2\\n<size:10>[Container]</size>\" <<SoftwareSystem2.Container2>> as SoftwareSystem2.Container2\n" +
                "}\n" +
                "\n" +
                "SoftwareSystem1.Container1 .[#707070,thickness=2].> SoftwareSystem2.Container2 : \"<color:#707070>Uses\"\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderComponentDiagramWithExternalComponents() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

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
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/component-view-with-external-components-1.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderComponentDiagramWithExternalComponentsAndSoftwareSystemBoundariesIncluded() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

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
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/component-view-with-external-components-2.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderDynamicDiagramWithExternalContainers() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");

        container1.uses(container2, "Uses");

        DynamicView dynamicView = workspace.getViews().createDynamicView(softwareSystem1, "Dynamic", "");
        dynamicView.add(container1, container2);

        Diagram diagram = new StructurizrPlantUMLExporter().export(dynamicView);
        assertEquals("@startuml\n" +
                "set separator none\n" +
                "title Software System 1 - Dynamic\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "skinparam {\n" +
                "  arrowFontSize 10\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 200\n" +
                "  maxMessageSize 100\n" +
                "}\n" +
                "\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<SoftwareSystem1.Container1>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem2.Container2>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem1>> {\n" +
                "  BorderColor #9a9a9a\n" +
                "  FontColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<SoftwareSystem2>> {\n" +
                "  BorderColor #9a9a9a\n" +
                "  FontColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "\n" +
                "rectangle \"Software System 1\\n<size:10>[Software System]</size>\" <<SoftwareSystem1>> {\n" +
                "  rectangle \"==Container 1\\n<size:10>[Container]</size>\" <<SoftwareSystem1.Container1>> as SoftwareSystem1.Container1\n" +
                "}\n" +
                "\n" +
                "rectangle \"Software System 2\\n<size:10>[Software System]</size>\" <<SoftwareSystem2>> {\n" +
                "  rectangle \"==Container 2\\n<size:10>[Container]</size>\" <<SoftwareSystem2.Container2>> as SoftwareSystem2.Container2\n" +
                "}\n" +
                "\n" +
                "SoftwareSystem1.Container1 .[#707070,thickness=2].> SoftwareSystem2.Container2 : \"<color:#707070>1. Uses\"\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    public void test_renderDynamicDiagramWithExternalComponents() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

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
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/dynamic-view-with-external-components-1.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderDynamicDiagramWithExternalComponentsAndSoftwareSystemBoundariesIncluded() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");

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
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/dynamic-view-with-external-components-2.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_renderDiagramWithElementUrls() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System");
        softwareSystem.setUrl("https://structurizr.com");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertTrue(diagram.getDefinition().contains("rectangle \"==Software System\\n<size:10>[Software System]</size>\" <<SoftwareSystem>> as SoftwareSystem [[https://structurizr.com]]\n"));
    }

    @Test
    public void test_renderDiagramWithIncludes() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Software System");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        view.getViewSet().getConfiguration().addProperty(StructurizrPlantUMLExporter.PLANTUML_INCLUDES_PROPERTY, "styles.puml");

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertTrue(diagram.getDefinition().contains("!include styles.puml\n"));
    }

    @Test
    public void test_renderDiagramWithNewLineCharacterInElementName() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addSoftwareSystem("Software\nSystem");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertTrue(diagram.getDefinition().contains("rectangle \"==Software\\nSystem\\n<size:10>[Software System]</size>\" <<SoftwareSystem>> as SoftwareSystem"));
    }

    @Test
    public void test_renderCustomView() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        CustomElement b = model.addCustomElement("B", "Custom", "Description");
        a.uses(b, "Uses");

        CustomView view = workspace.getViews().createCustomView("key", "Title", "Description");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("@startuml\nset separator none\n" +
                "title Title\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "skinparam {\n" +
                "  arrowFontSize 10\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 200\n" +
                "  maxMessageSize 100\n" +
                "}\n" +
                "\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<1>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "skinparam rectangle<<2>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "\n" +
                "rectangle \"==A\" <<1>> as 1\n" +
                "rectangle \"==B\\n<size:10>[Custom]</size>\\n\\nDescription\" <<2>> as 2\n" +
                "\n" +
                "1 .[#707070,thickness=2].> 2 : \"<color:#707070>Uses\"\n" +
                "@enduml", diagram.getDefinition());
    }

    @Test
    void renderWorkspaceWithUnicodeElementName() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("Пользователь");
        workspace.getViews().createSystemLandscapeView("key", "Description").addDefaultElements();

        String diagramDefinition = new StructurizrPlantUMLExporter().export(workspace).stream().findFirst().get().getDefinition();

        assertTrue(diagramDefinition.contains("skinparam rectangle<<Пользователь>> {"));
        assertTrue(diagramDefinition.contains("rectangle \"==Пользователь\\n<size:10>[Person]</size>\" <<Пользователь>> as Пользователь"));
    }

    @Test
    public void testLegend() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        CustomElement a = model.addCustomElement("A");
        a.addTags("Tag 1");
        CustomElement b = model.addCustomElement("B");
        b.addTags("Tag 2");
        a.uses(b, "...").addTags("Tag 3");
        b.uses(a, "...").addTags("Tag 4");

        CustomView view = workspace.getViews().createCustomView("key", "Title", "Description");
        view.addDefaultElements();

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("@startuml\nset separator none\n" +
                "\n" +
                "skinparam {\n" +
                "  shadowing false\n" +
                "  arrowFontSize 15\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 100\n" +
                "  maxMessageSize 100\n" +
                "}\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<_transparent>> {\n" +
                "  BorderColor transparent\n" +
                "  BackgroundColor transparent\n" +
                "  FontColor transparent\n" +
                "}\n" +
                "\n" +
                "skinparam rectangle<<1>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "}\n" +
                "rectangle \"==Element\" <<1>>\n" +
                "\n" +
                "rectangle \".\" <<_transparent>> as 2\n" +
                "2 .[#707070,thickness=2].> 2 : \"<color:#707070>Relationship\"\n" +
                "\n" +
                "\n" +
                "@enduml", diagram.getLegend().getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag 1").background("#ff0000").color("#ffffff").shape(Shape.RoundedBox);
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Tag 2").background("#00ff00").color("#ffffff").shape(Shape.Hexagon);
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Tag 3").color("#0000ff");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle("Tag 4").color("#ff00ff").thickness(3).style(LineStyle.Solid);

        diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("@startuml\nset separator none\n" +
                "\n" +
                "skinparam {\n" +
                "  shadowing false\n" +
                "  arrowFontSize 15\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 100\n" +
                "  maxMessageSize 100\n" +
                "}\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<_transparent>> {\n" +
                "  BorderColor transparent\n" +
                "  BackgroundColor transparent\n" +
                "  FontColor transparent\n" +
                "}\n" +
                "\n" +
                "skinparam rectangle<<1>> {\n" +
                "  BackgroundColor #ff0000\n" +
                "  FontColor #ffffff\n" +
                "  BorderColor #b20000\n" +
                "  roundCorner 20\n" +
                "}\n" +
                "rectangle \"==Tag 1\" <<1>>\n" +
                "\n" +
                "skinparam hexagon<<2>> {\n" +
                "  BackgroundColor #00ff00\n" +
                "  FontColor #ffffff\n" +
                "  BorderColor #00b200\n" +
                "}\n" +
                "hexagon \"==Tag 2\" <<2>>\n" +
                "\n" +
                "rectangle \".\" <<_transparent>> as 3\n" +
                "3 .[#0000ff,thickness=2].> 3 : \"<color:#0000ff>Tag 3\"\n" +
                "\n" +
                "rectangle \".\" <<_transparent>> as 4\n" +
                "4 -[#ff00ff,thickness=3]-> 4 : \"<color:#ff00ff>Tag 4\"\n" +
                "\n" +
                "\n" +
                "@enduml", diagram.getLegend().getDefinition());
    }

    @Test
    public void staticDiagramsAreUnchangedWhenSequenceDiagramsAreEnabled() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        model.addSoftwareSystem("Software System").setGroup("Group");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram;
        String expected = """
                @startuml
                set separator none
                title System Landscape
                                
                top to bottom direction
                                
                skinparam {
                  arrowFontSize 10
                  defaultTextAlignment center
                  wrapWidth 200
                  maxMessageSize 100
                }
                                
                hide stereotype
                                
                skinparam rectangle<<SoftwareSystem>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                                
                rectangle "Group" <<group1>> as group1 {
                  skinparam RectangleBorderColor<<group1>> #cccccc
                  skinparam RectangleFontColor<<group1>> #cccccc
                  skinparam RectangleBorderStyle<<group1>> dashed
                                
                  rectangle "==Software System\\n<size:10>[Software System]</size>" <<SoftwareSystem>> as SoftwareSystem
                }
                                
                                
                @enduml""";

        diagram = exporter.export(view);
        assertEquals(expected, diagram.getDefinition());

        workspace.getViews().getConfiguration().addProperty(StructurizrPlantUMLExporter.PLANTUML_SEQUENCE_DIAGRAM_PROPERTY, "true");

        diagram = exporter.export(view);
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void testFont() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User");
        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.addAllElements();
        workspace.getViews().getConfiguration().getBranding().setFont(new Font("Courier"));

        Diagram diagram = new StructurizrPlantUMLExporter().export(view);
        assertEquals("@startuml\nset separator none\n" +
                "title System Landscape\n" +
                "\n" +
                "top to bottom direction\n" +
                "\n" +
                "skinparam {\n" +
                "  arrowFontSize 10\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 200\n" +
                "  maxMessageSize 100\n" +
                "  defaultFontName \"Courier\"\n" +
                "}\n" +
                "\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<User>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "  shadowing false\n" +
                "}\n" +
                "\n" +
                "rectangle \"==User\\n<size:10>[Person]</size>\" <<User>> as User\n" +
                "\n" +
                "@enduml", diagram.getDefinition().toString());

        assertEquals("@startuml\nset separator none\n" +
                "\n" +
                "skinparam {\n" +
                "  shadowing false\n" +
                "  arrowFontSize 15\n" +
                "  defaultTextAlignment center\n" +
                "  wrapWidth 100\n" +
                "  maxMessageSize 100\n" +
                "  defaultFontName \"Courier\"\n" +
                "}\n" +
                "hide stereotype\n" +
                "\n" +
                "skinparam rectangle<<_transparent>> {\n" +
                "  BorderColor transparent\n" +
                "  BackgroundColor transparent\n" +
                "  FontColor transparent\n" +
                "}\n" +
                "\n" +
                "skinparam rectangle<<1>> {\n" +
                "  BackgroundColor #dddddd\n" +
                "  FontColor #000000\n" +
                "  BorderColor #9a9a9a\n" +
                "}\n" +
                "rectangle \"==Element\" <<1>>\n" +
                "\n" +
                "\n" +
                "@enduml", diagram.getLegend().getDefinition());
    }

    @Test
    public void dynamicView_UnscopedWithGroups() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        softwareSystemA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        softwareSystemB.setGroup("Group 2");
        softwareSystemA.uses(softwareSystemB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView("key", "Description");
        view.add(softwareSystemA, softwareSystemB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/dynamic-view-unscoped-with-groups.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void dynamicView_SoftwareSystemScopedWithGroups() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        Container containerA = softwareSystemA.addContainer("A");
        containerA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        Container containerB = softwareSystemB.addContainer("B");
        containerB.setGroup("Group 2");
        containerA.uses(containerB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView(softwareSystemA, "key", "Description");
        view.add(containerA, containerB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/dynamic-view-software-system-scoped-with-groups.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void dynamicView_ContainerScopedWithGroups() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystemA = workspace.getModel().addSoftwareSystem("A");
        Container containerA = softwareSystemA.addContainer("A");
        Component componentA = containerA.addComponent("A");
        componentA.setGroup("Group 1");
        SoftwareSystem softwareSystemB = workspace.getModel().addSoftwareSystem("B");
        Container containerB = softwareSystemB.addContainer("B");
        Component componentB = containerB.addComponent("B");
        componentB.setGroup("Group 2");
        componentA.uses(componentB, "Uses");

        DynamicView view = workspace.getViews().createDynamicView(containerA, "key", "Description");
        view.add(componentA, componentB);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        String expected = readFile(new File("./src/test/java/com/structurizr/export/plantuml/structurizr/dynamic-view-container-scoped-with-groups.puml"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_writeContainerViewWithGroupedElements_WithAndWithoutAGroupSeparator() {
        Workspace workspace = new Workspace("Name", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        Container container1 = softwareSystem.addContainer("Container 1");
        container1.setGroup("Group 1");
        Container container2 = softwareSystem.addContainer("Container 2");
        container2.setGroup("Group 2");

        ContainerView view = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        view.addAllElements();

        String expectedResult = """
                @startuml
                set separator none
                title Software System - Containers
                                
                top to bottom direction
                                
                skinparam {
                  arrowFontSize 10
                  defaultTextAlignment center
                  wrapWidth 200
                  maxMessageSize 100
                }
                                
                hide stereotype
                                
                skinparam rectangle<<SoftwareSystem.Container1>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                skinparam rectangle<<SoftwareSystem.Container2>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                skinparam rectangle<<SoftwareSystem>> {
                  BorderColor #9a9a9a
                  FontColor #9a9a9a
                  shadowing false
                }
                                
                rectangle "Software System\\n<size:10>[Software System]</size>" <<SoftwareSystem>> {
                  rectangle "Group 1" <<group1>> as group1 {
                    skinparam RectangleBorderColor<<group1>> #cccccc
                    skinparam RectangleFontColor<<group1>> #cccccc
                    skinparam RectangleBorderStyle<<group1>> dashed
                                
                    rectangle "==Container 1\\n<size:10>[Container]</size>" <<SoftwareSystem.Container1>> as SoftwareSystem.Container1
                  }
                                
                  rectangle "Group 2" <<group2>> as group2 {
                    skinparam RectangleBorderColor<<group2>> #cccccc
                    skinparam RectangleFontColor<<group2>> #cccccc
                    skinparam RectangleBorderStyle<<group2>> dashed
                                
                    rectangle "==Container 2\\n<size:10>[Container]</size>" <<SoftwareSystem.Container2>> as SoftwareSystem.Container2
                  }
                                
                }
                                
                @enduml""";

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals(expectedResult, diagram.getDefinition());

        // this should be the same
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        exporter = new StructurizrPlantUMLExporter();
        diagram = exporter.export(view);
        assertEquals(expectedResult, diagram.getDefinition());
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

        DeploymentView view = workspace.getViews().createDeploymentView("key", "Description");
        view.add(infrastructureNode1);
        view.add(infrastructureNode2);
        view.add(softwareSystemInstance);

        String expectedResult = """
                @startuml
                set separator none
                title Deployment - Default
                                
                top to bottom direction
                                
                skinparam {
                  arrowFontSize 10
                  defaultTextAlignment center
                  wrapWidth 200
                  maxMessageSize 100
                }
                                
                hide stereotype
                                
                skinparam rectangle<<Default.Server1.InfrastructureNode1>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                skinparam rectangle<<Default.Server1.InfrastructureNode2>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                skinparam rectangle<<Default.Server1>> {
                  BackgroundColor #ffffff
                  FontColor #000000
                  BorderColor #888888
                  shadowing false
                }
                skinparam rectangle<<Default.Server1.SoftwareSystem_1>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                                
                rectangle "Group 1" <<group1>> as group1 {
                  skinparam RectangleBorderColor<<group1>> #cccccc
                  skinparam RectangleFontColor<<group1>> #cccccc
                  skinparam RectangleBorderStyle<<group1>> dashed
                                
                  rectangle "Server 1\\n<size:10>[Deployment Node]</size>" <<Default.Server1>> as Default.Server1 {
                    rectangle "Group 2" <<group2>> as group2 {
                      skinparam RectangleBorderColor<<group2>> #cccccc
                      skinparam RectangleFontColor<<group2>> #cccccc
                      skinparam RectangleBorderStyle<<group2>> dashed
                                
                      rectangle "==Infrastructure Node 2\\n<size:10>[Infrastructure Node]</size>" <<Default.Server1.InfrastructureNode2>> as Default.Server1.InfrastructureNode2
                      rectangle "==Software System\\n<size:10>[Software System]</size>" <<Default.Server1.SoftwareSystem_1>> as Default.Server1.SoftwareSystem_1
                    }
                                
                    rectangle "==Infrastructure Node 1\\n<size:10>[Infrastructure Node]</size>" <<Default.Server1.InfrastructureNode1>> as Default.Server1.InfrastructureNode1
                  }
                                
                }
                                
                @enduml""";

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals(expectedResult, diagram.getDefinition());

        // this should be the same
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        exporter = new StructurizrPlantUMLExporter();
        diagram = exporter.export(view);
        assertEquals(expectedResult, diagram.getDefinition());
    }

    @Test
    public void test_ElementInstanceUrl() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        a.setUrl("https://example.com/url1");
        SoftwareSystemInstance aInstance = workspace.getModel().addDeploymentNode("Node").add(a);

        DeploymentView view = workspace.getViews().createDeploymentView("deployment", "Default");
        view.add(aInstance);

        assertEquals("""
@startuml
set separator none
title Deployment - Default

top to bottom direction

skinparam {
  arrowFontSize 10
  defaultTextAlignment center
  wrapWidth 200
  maxMessageSize 100
}

hide stereotype

skinparam rectangle<<Default.Node.A_1>> {
  BackgroundColor #dddddd
  FontColor #000000
  BorderColor #9a9a9a
  shadowing false
}
skinparam rectangle<<Default.Node>> {
  BackgroundColor #ffffff
  FontColor #000000
  BorderColor #888888
  shadowing false
}

rectangle "Node\\n<size:10>[Deployment Node]</size>" <<Default.Node>> as Default.Node {
  rectangle "==A\\n<size:10>[Software System]</size>" <<Default.Node.A_1>> as Default.Node.A_1 [[https://example.com/url1]]
}

@enduml""", new StructurizrPlantUMLExporter().export(view).getDefinition());

        aInstance.setUrl("https://example.com/url2");
        assertEquals("""
@startuml
set separator none
title Deployment - Default

top to bottom direction

skinparam {
  arrowFontSize 10
  defaultTextAlignment center
  wrapWidth 200
  maxMessageSize 100
}

hide stereotype

skinparam rectangle<<Default.Node.A_1>> {
  BackgroundColor #dddddd
  FontColor #000000
  BorderColor #9a9a9a
  shadowing false
}
skinparam rectangle<<Default.Node>> {
  BackgroundColor #ffffff
  FontColor #000000
  BorderColor #888888
  shadowing false
}

rectangle "Node\\n<size:10>[Deployment Node]</size>" <<Default.Node>> as Default.Node {
  rectangle "==A\\n<size:10>[Software System]</size>" <<Default.Node.A_1>> as Default.Node.A_1 [[https://example.com/url2]]
}

@enduml""", new StructurizrPlantUMLExporter().export(view).getDefinition());

    }

    @Test
    void groupAndSoftwareSystemNameAreTheSame() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Name");
        softwareSystem.setGroup("Name");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "Description");
        view.add(softwareSystem);

        StructurizrPlantUMLExporter exporter = new StructurizrPlantUMLExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                @startuml
                set separator none
                title System Landscape
                                
                top to bottom direction
                                
                skinparam {
                  arrowFontSize 10
                  defaultTextAlignment center
                  wrapWidth 200
                  maxMessageSize 100
                }
                                
                hide stereotype
                                
                skinparam rectangle<<Name>> {
                  BackgroundColor #dddddd
                  FontColor #000000
                  BorderColor #9a9a9a
                  shadowing false
                }
                                
                rectangle "Name" <<group1>> as group1 {
                  skinparam RectangleBorderColor<<group1>> #cccccc
                  skinparam RectangleFontColor<<group1>> #cccccc
                  skinparam RectangleBorderStyle<<group1>> dashed
                                
                  rectangle "==Name\\n<size:10>[Software System]</size>" <<Name>> as Name
                }
                                
                                
                @enduml""", diagram.getDefinition());
    }

}