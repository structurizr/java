package com.structurizr.graphviz;

import com.structurizr.Workspace;
import com.structurizr.export.Diagram;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DOTExporterTests {

    @Test
    public void test_writeCustomView() {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box1 = workspace.getModel().addCustomElement("Box 1");
        CustomElement box2 = workspace.getModel().addCustomElement("Box 2");
        box1.uses(box2, "Uses");

        CustomView view = workspace.getViews().createCustomView("CustomView", "Title", "Description");
        view.add(box1);
        view.add(box2);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box 1\"]\n" +
                "  2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: Box 2\"]\n" +
                "\n" +
                "  1 -> 2 [id=3]\n" +
                "}", content);
    }

    @Test
    public void test_writeSystemLandscapeView() {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        Person user = workspace.getModel().addPerson("User", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "  2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: User\"]\n" +
                "  3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Software System\"]\n" +
                "\n" +
                "  2 -> 3 [id=4]\n" +
                "}", content);
    }

    @Test
    public void test_writeSystemLandscapeViewWithGroupedElements() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        Person user = workspace.getModel().addPerson("User", "");
        user.setGroup("External");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        softwareSystem.setGroup("Internal");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph \"cluster_group_1\" {\n" +
                "    margin=25\n" +
                "    2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: User\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_2\" {\n" +
                "    margin=25\n" +
                "    3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Software System\"]\n" +
                "  }\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "\n" +
                "  2 -> 3 [id=4]\n" +
                "}", content);
    }

    @Test
    public void test_writeSystemLandscapeViewWithNestedGroupedElements() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");

        SoftwareSystem a = workspace.getModel().addSoftwareSystem("A");
        a.setGroup("Enterprise 1/Department 1/Team 1");

        SoftwareSystem b = workspace.getModel().addSoftwareSystem("B");
        b.setGroup("Enterprise 1/Department 1/Team 2");

        SoftwareSystem c = workspace.getModel().addSoftwareSystem("C");
        c.setGroup("Enterprise 1/Department 2");

        SoftwareSystem d = workspace.getModel().addSoftwareSystem("D");
        d.setGroup("Enterprise 2");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "");
        view.addAllElements();

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph \"cluster_group_1\" {\n" +
                "    margin=25\n" +
                "      subgraph \"cluster_group_2\" {\n" +
                "        margin=25\n" +
                "          subgraph \"cluster_group_3\" {\n" +
                "            margin=25\n" +
                "            1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: A\"]\n" +
                "          }\n" +
                "\n" +
                "          subgraph \"cluster_group_4\" {\n" +
                "            margin=25\n" +
                "            2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: B\"]\n" +
                "          }\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "      subgraph \"cluster_group_5\" {\n" +
                "        margin=25\n" +
                "        3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: C\"]\n" +
                "      }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_6\" {\n" +
                "    margin=25\n" +
                "    4 [width=1.500000,height=1.000000,fixedsize=true,id=4,label=\"4: D\"]\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "}", content);
    }

    @Test
    public void test_writeSystemLandscapeViewInGermanLocale() throws Exception {
        // ranksep=1.0 was being output as ranksep=1,0
        Locale.setDefault(new Locale("de", "DE"));
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        Person user = workspace.getModel().addPerson("User", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        user.uses(softwareSystem, "Uses");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("SystemLandscape", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "  2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: User\"]\n" +
                "  3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Software System\"]\n" +
                "\n" +
                "  2 -> 3 [id=4]\n" +
                "}", content);
    }

    @Test
    public void test_writeSystemContextView() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        Person user = workspace.getModel().addPerson("User", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        user.uses(softwareSystem, "Uses");

        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "  2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: User\"]\n" +
                "  3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Software System\"]\n" +
                "\n" +
                "  2 -> 3 [id=4]\n" +
                "}", content);
    }


    @Test
    public void test_writeSystemContextViewWithGroupedElements() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        Person user = workspace.getModel().addPerson("User", "");
        user.setGroup("External");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        softwareSystem.setGroup("Internal");
        user.uses(softwareSystem, "Uses");

        SystemContextView view = workspace.getViews().createSystemContextView(softwareSystem, "SystemContext", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph \"cluster_group_1\" {\n" +
                "    margin=25\n" +
                "    2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: User\"]\n" +
                "  }\n" +
                "\n" +
                "  subgraph \"cluster_group_2\" {\n" +
                "    margin=25\n" +
                "    3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Software System\"]\n" +
                "  }\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "\n" +
                "  2 -> 3 [id=4]\n" +
                "}", content);
    }

    @Test
    public void test_writeContainerViewWithGroupedElementsInASingleSoftwareSystem() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        Container container1 = softwareSystem.addContainer("Container 1");
        container1.setGroup("Group 1");
        Container container2 = softwareSystem.addContainer("Container 2");
        container2.setGroup("Group 2");
        Container container3 = softwareSystem.addContainer("Container 3");

        container1.uses(container2, "Uses");
        container2.uses(container3, "Uses");

        ContainerView view = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "\n" +
                "  subgraph cluster_2 {\n" +
                "    margin=25\n" +
                "    subgraph \"cluster_group_1\" {\n" +
                "      margin=25\n" +
                "      3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Container 1\"]\n" +
                "    }\n" +
                "\n" +
                "    subgraph \"cluster_group_2\" {\n" +
                "      margin=25\n" +
                "      4 [width=1.500000,height=1.000000,fixedsize=true,id=4,label=\"4: Container 2\"]\n" +
                "    }\n" +
                "\n" +
                "    5 [width=1.500000,height=1.000000,fixedsize=true,id=5,label=\"5: Container 3\"]\n" +
                "  }\n" +
                "\n" +
                "  3 -> 4 [id=6]\n" +
                "  4 -> 5 [id=7]\n" +
                "}", content);
    }

    @Test
    public void test_writeContainerViewWithGroupedElementsInMultipleSoftwareSystems() throws Exception {
        Workspace workspace = new Workspace("Name", "");

        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        container1.setGroup("Group");

        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        container2.setGroup("Group");

        container1.uses(container2, "Uses");

        ContainerView view = workspace.getViews().createContainerView(softwareSystem1, "Containers", "");
        view.add(container1);
        view.add(container2);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph cluster_1 {\n" +
                "    margin=25\n" +
                "    subgraph \"cluster_group_1\" {\n" +
                "      margin=25\n" +
                "      2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: Container 1\"]\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "  subgraph cluster_3 {\n" +
                "    margin=25\n" +
                "    subgraph \"cluster_group_2\" {\n" +
                "      margin=25\n" +
                "      4 [width=1.500000,height=1.000000,fixedsize=true,id=4,label=\"4: Container 2\"]\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "  2 -> 4 [id=5]\n" +
                "}", content);
    }

    @Test
    public void test_writeComponentViewWithGroupedElements() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        CustomElement box = workspace.getModel().addCustomElement("Box");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        Container container = softwareSystem.addContainer("Container");
        Component component1 = container.addComponent("Component 1", "");
        Component component2 = container.addComponent("Component 2", "");
        component2.setGroup("Group 2");
        Component component3 = container.addComponent("Component 3", "");
        component3.setGroup("Group 3");

        component1.uses(component2, "Uses");
        component2.uses(component3, "Uses");

        ComponentView view = workspace.getViews().createComponentView(container, "Components", "");
        view.addAllElements();
        view.add(box);

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();
        assertEquals("digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  1 [width=1.500000,height=1.000000,fixedsize=true,id=1,label=\"1: Box\"]\n" +
                "\n" +
                "  subgraph cluster_3 {\n" +
                "    margin=25\n" +
                "    subgraph \"cluster_group_1\" {\n" +
                "      margin=25\n" +
                "      5 [width=1.500000,height=1.000000,fixedsize=true,id=5,label=\"5: Component 2\"]\n" +
                "    }\n" +
                "\n" +
                "    subgraph \"cluster_group_2\" {\n" +
                "      margin=25\n" +
                "      6 [width=1.500000,height=1.000000,fixedsize=true,id=6,label=\"6: Component 3\"]\n" +
                "    }\n" +
                "\n" +
                "    4 [width=1.500000,height=1.000000,fixedsize=true,id=4,label=\"4: Component 1\"]\n" +
                "  }\n" +
                "\n" +
                "  4 -> 5 [id=7]\n" +
                "  5 -> 6 [id=8]\n" +
                "}", content);
    }

    @Test
    public void test_writeContainerViewWithGroupedElements_WithAndWithoutAGroupSeparator() throws Exception {
        Workspace workspace = new Workspace("Name", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("Software System", "");
        Container container1 = softwareSystem.addContainer("Container 1");
        container1.setGroup("Group 1");
        Container container2 = softwareSystem.addContainer("Container 2");
        container2.setGroup("Group 2");

        ContainerView view = workspace.getViews().createContainerView(softwareSystem, "Containers", "");
        view.addAllElements();

        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(view);

        String content = diagram.getDefinition();

        String expectedResult = "digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=TB,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph cluster_1 {\n" +
                "    margin=25\n" +
                "    subgraph \"cluster_group_1\" {\n" +
                "      margin=25\n" +
                "      2 [width=1.500000,height=1.000000,fixedsize=true,id=2,label=\"2: Container 1\"]\n" +
                "    }\n" +
                "\n" +
                "    subgraph \"cluster_group_2\" {\n" +
                "      margin=25\n" +
                "      3 [width=1.500000,height=1.000000,fixedsize=true,id=3,label=\"3: Container 2\"]\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "}";

        assertEquals(expectedResult, content);

        // this should be the same
        workspace.getModel().addProperty("structurizr.groupSeparator", "/");
        exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        diagram = exporter.export(view);

        content = diagram.getDefinition();
        assertEquals(expectedResult, content);
    }

    @Test
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("src/test/resources/structurizr-54915-workspace.json"));
        DOTExporter exporter = new DOTExporter(RankDirection.TopBottom, 300, 300);
        Diagram diagram = exporter.export(workspace.getViews().getDeploymentViews().iterator().next());

        String content = diagram.getDefinition();

        String expectedResult = "digraph {\n" +
                "  compound=true\n" +
                "  graph [splines=polyline,rankdir=LR,ranksep=1.0,nodesep=1.0,fontsize=5]\n" +
                "  node [shape=box,fontsize=5]\n" +
                "  edge []\n" +
                "\n" +
                "  subgraph cluster_5 {\n" +
                "    margin=25\n" +
                "    subgraph cluster_6 {\n" +
                "      margin=25\n" +
                "      subgraph cluster_12 {\n" +
                "        margin=25\n" +
                "        subgraph cluster_13 {\n" +
                "          margin=25\n" +
                "          14 [width=1.500000,height=1.000000,fixedsize=true,id=14,label=\"14: Database\"]\n" +
                "        }\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "      7 [width=1.500000,height=1.000000,fixedsize=true,id=7,label=\"7: Route 53\"]\n" +
                "      8 [width=1.500000,height=1.000000,fixedsize=true,id=8,label=\"8: Elastic Load Balancer\"]\n" +
                "      subgraph cluster_9 {\n" +
                "        margin=25\n" +
                "        subgraph cluster_10 {\n" +
                "          margin=25\n" +
                "          11 [width=1.500000,height=1.000000,fixedsize=true,id=11,label=\"11: Web Application\"]\n" +
                "        }\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "  11 -> 14 [id=15]\n" +
                "  7 -> 8 [id=16]\n" +
                "  8 -> 11 [id=17]\n" +
                "}";

        assertEquals(expectedResult, content);
    }

}