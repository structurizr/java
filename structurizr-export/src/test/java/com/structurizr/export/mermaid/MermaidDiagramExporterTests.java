package com.structurizr.export.mermaid;

import com.structurizr.Workspace;
import com.structurizr.export.AbstractExporterTests;
import com.structurizr.export.Diagram;
import com.structurizr.model.*;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MermaidDiagramExporterTests extends AbstractExporterTests {

    @Test
    @Tag("IntegrationTest")
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/amazon-web-services.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        MermaidDiagramExporter exporter = new MermaidDiagramExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        assertEquals("""
                graph LR
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Deployment View: X - Live"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph 5 ["Amazon Web Services"]
                      style 5 fill:#ffffff,stroke:#232f3e,color:#232f3e
                
                      subgraph 6 ["US-East-1"]
                        style 6 fill:#ffffff,stroke:#147eba,color:#147eba
                
                        subgraph 10 ["Autoscaling group"]
                          style 10 fill:#ffffff,stroke:#cc2264,color:#cc2264
                
                          subgraph 11 ["Amazon EC2 - Ubuntu server"]
                            style 11 fill:#ffffff,stroke:#d86613,color:#d86613
                
                            12("<div style='font-weight: bold'>Web Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: Java and Spring Boot]</div>")
                            style 12 fill:#ffffff,stroke:#444444,color:#444444
                          end
                
                        end
                
                        subgraph 14 ["Amazon RDS"]
                          style 14 fill:#ffffff,stroke:#3b48cc,color:#3b48cc
                
                          subgraph 15 ["MySQL"]
                            style 15 fill:#ffffff,stroke:#3b48cc,color:#3b48cc
                
                            16[("<div style='font-weight: bold'>Database Schema</div><div style='font-size: 70%; margin-top: 0px'>[Container]</div>")]
                            style 16 fill:#ffffff,stroke:#444444,color:#444444
                          end
                
                        end
                
                        7["<div style='font-weight: bold'>DNS router</div><div style='font-size: 70%; margin-top: 0px'>[Infrastructure Node: Route 53]</div><div style='font-size: 80%; margin-top:10px'>Routes incoming requests<br />based upon domain name.</div>"]
                        style 7 fill:#ffffff,stroke:#693cc5,color:#693cc5
                        8["<div style='font-weight: bold'>Load Balancer</div><div style='font-size: 70%; margin-top: 0px'>[Infrastructure Node: Elastic Load Balancer]</div><div style='font-size: 80%; margin-top:10px'>Automatically distributes<br />incoming application traffic.</div>"]
                        style 8 fill:#ffffff,stroke:#693cc5,color:#693cc5
                      end
                
                    end
                
                    8-. "<div>Forwards requests to</div><div style='font-size: 70%'>[HTTPS]</div>" .->12
                    12-. "<div>Reads from and writes to</div><div style='font-size: 70%'>[MySQL Protocol/SSL]</div>" .->16
                    7-. "<div>Forwards requests to</div><div style='font-size: 70%'>[HTTPS]</div>" .->8
                
                  end""", diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/resources/groups.json"));
        ThemeUtils.loadThemes(workspace);

        MermaidDiagramExporter exporter = new MermaidDiagramExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["System Landscape View"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph group1 ["Group 1"]
                      style group1 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                      2["<div style='font-weight: bold'>B</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                      style 2 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph group2 ["Group 2"]
                      style group2 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                      3["<div style='font-weight: bold'>C</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                      style 3 fill:#ffffff,stroke:#444444,color:#444444
                        subgraph group3 ["Group 3"]
                          style group3 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                          4["<div style='font-weight: bold'>D</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                          style 4 fill:#ffffff,stroke:#444444,color:#444444
                        end
                
                    end
                
                    1["<div style='font-weight: bold'>A</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                    style 1 fill:#ffffff,stroke:#444444,color:#444444
                
                    2-. "<div></div><div style='font-size: 70%'></div>" .->3
                    3-. "<div></div><div style='font-size: 70%'></div>" .->4
                    1-. "<div></div><div style='font-size: 70%'></div>" .->2

                  end""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Container View: D"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    3["<div style='font-weight: bold'>C</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                    style 3 fill:#ffffff,stroke:#444444,color:#444444
                
                    subgraph 4 ["D"]
                      style 4 fill:#ffffff,stroke:#444444,color:#444444
                
                      subgraph group1 ["Group 4"]
                        style group1 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                        6["<div style='font-weight: bold'>F</div><div style='font-size: 70%; margin-top: 0px'>[Container]</div>"]
                        style 6 fill:#ffffff,stroke:#444444,color:#444444
                      end
                
                      5["<div style='font-weight: bold'>E</div><div style='font-size: 70%; margin-top: 0px'>[Container]</div>"]
                      style 5 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    3-. "<div></div><div style='font-size: 70%'></div>" .->5
                    3-. "<div></div><div style='font-size: 70%'></div>" .->6

                  end""", diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Component View: D - F"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    3["<div style='font-weight: bold'>C</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                    style 3 fill:#ffffff,stroke:#444444,color:#444444
                
                    subgraph 4 ["D"]
                      style 4 fill:#ffffff,stroke:#444444,color:#444444
                
                      subgraph 6 ["F"]
                        style 6 fill:#ffffff,stroke:#444444,color:#444444
                
                        subgraph group1 ["Group 5"]
                          style group1 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                          8["<div style='font-weight: bold'>H</div><div style='font-size: 70%; margin-top: 0px'>[Component]</div>"]
                          style 8 fill:#ffffff,stroke:#444444,color:#444444
                        end
                
                        7["<div style='font-weight: bold'>G</div><div style='font-size: 70%; margin-top: 0px'>[Component]</div>"]
                        style 7 fill:#ffffff,stroke:#444444,color:#444444
                      end
                
                    end
                
                    3-. "<div></div><div style='font-size: 70%'></div>" .->7
                    3-. "<div></div><div style='font-size: 70%'></div>" .->8
                
                  end""", diagram.getDefinition());
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

        MermaidDiagramExporter exporter = new MermaidDiagramExporter();
        Collection<Diagram> diagrams = exporter.export(workspace);

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["System Landscape View"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph group1 ["Organisation 1"]
                      style group1 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                      3["<div style='font-weight: bold'>Organisation 1</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                      style 3 fill:#ffffff,stroke:#444444,color:#444444
                        subgraph group2 ["Department 1"]
                          style group2 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                          5["<div style='font-weight: bold'>Department 1</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                          style 5 fill:#ffffff,stroke:#444444,color:#444444
                            subgraph group3 ["Team 1"]
                              style group3 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                              1["<div style='font-weight: bold'>Team 1</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                              style 1 fill:#ffffff,stroke:#444444,color:#444444
                            end
                
                            subgraph group4 ["Team 2"]
                              style group4 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                              2["<div style='font-weight: bold'>Team 2</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                              style 2 fill:#ffffff,stroke:#444444,color:#444444
                            end
                
                        end
                
                    end
                
                    subgraph group5 ["Organisation 2"]
                      style group5 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                      4["<div style='font-weight: bold'>Organisation 2</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div>"]
                      style 4 fill:#ffffff,stroke:#444444,color:#444444
                    end

                
                  end""", diagram.getDefinition());
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

        Diagram diagram = new MermaidDiagramExporter().export(containerView);
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Container View: Software System 1"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph 1 ["Software System 1"]
                      style 1 fill:#ffffff,stroke:#444444,color:#444444
                
                      2["<div style='font-weight: bold'>Container 1</div><div style='font-size: 70%; margin-top: 0px'>[Container]</div>"]
                      style 2 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph 3 ["Software System 2"]
                      style 3 fill:#ffffff,stroke:#444444,color:#444444
                
                      4["<div style='font-weight: bold'>Container 2</div><div style='font-size: 70%; margin-top: 0px'>[Container]</div>"]
                      style 4 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    2-. "<div>Uses</div><div style='font-size: 70%'></div>" .->4
                
                  end""", diagram.getDefinition());
    }

    @Test
    public void test_renderComponentDiagramWithExternalComponents() {
        Workspace workspace = new Workspace("Name", "Description");
        SoftwareSystem softwareSystem1 = workspace.getModel().addSoftwareSystem("Software System 1");
        Container container1 = softwareSystem1.addContainer("Container 1");
        Component component1 = container1.addComponent("Component 1");
        SoftwareSystem softwareSystem2 = workspace.getModel().addSoftwareSystem("Software System 2");
        Container container2 = softwareSystem2.addContainer("Container 2");
        Component component2 = container2.addComponent("Component 2");

        component1.uses(component2, "Uses");

        ComponentView componentView = workspace.getViews().createComponentView(container1, "Components", "");
        componentView.add(component1);
        componentView.add(component2);

        Diagram diagram = new MermaidDiagramExporter().export(componentView);
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Component View: Software System 1 - Container 1"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph 1 ["Software System 1"]
                      style 1 fill:#ffffff,stroke:#444444,color:#444444
                
                      subgraph 2 ["Container 1"]
                        style 2 fill:#ffffff,stroke:#444444,color:#444444
                
                        3["<div style='font-weight: bold'>Component 1</div><div style='font-size: 70%; margin-top: 0px'>[Component]</div>"]
                        style 3 fill:#ffffff,stroke:#444444,color:#444444
                      end
                
                    end
                
                    subgraph 4 ["Software System 2"]
                      style 4 fill:#ffffff,stroke:#444444,color:#444444
                
                      subgraph 5 ["Container 2"]
                        style 5 fill:#ffffff,stroke:#444444,color:#444444
                
                        6["<div style='font-weight: bold'>Component 2</div><div style='font-size: 70%; margin-top: 0px'>[Component]</div>"]
                        style 6 fill:#ffffff,stroke:#444444,color:#444444
                      end
                
                    end
                
                    3-. "<div>Uses</div><div style='font-size: 70%'></div>" .->6
                
                  end""", diagram.getDefinition());
    }

    @Test
    public void test_renderGroupStyles() {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getModel().addPerson("User 1").setGroup("Group 1");
        workspace.getModel().addPerson("User 2").setGroup("Group 2");
        workspace.getModel().addPerson("User 3").setGroup("Group 3");

        SystemLandscapeView view = workspace.getViews().createSystemLandscapeView("key", "");
        view.addDefaultElements();

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 1").color("#111111");
        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group:Group 2").color("#222222");

        MermaidDiagramExporter exporter = new MermaidDiagramExporter();
        Diagram diagram = exporter.export(view);
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["System Landscape View"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph group1 ["Group 1"]
                      style group1 fill:#ffffff,stroke:#111111,color:#111111,stroke-dasharray:5
                
                      1["<div style='font-weight: bold'>User 1</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 1 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph group2 ["Group 2"]
                      style group2 fill:#ffffff,stroke:#222222,color:#222222,stroke-dasharray:5
                
                      2["<div style='font-weight: bold'>User 2</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 2 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph group3 ["Group 3"]
                      style group3 fill:#ffffff,stroke:#cccccc,color:#cccccc,stroke-dasharray:5
                
                      3["<div style='font-weight: bold'>User 3</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 3 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                
                  end""", diagram.getDefinition());

        workspace.getViews().getConfiguration().getStyles().addElementStyle("Group").color("#aabbcc");

        diagram = exporter.export(view);
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["System Landscape View"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    subgraph group1 ["Group 1"]
                      style group1 fill:#ffffff,stroke:#111111,color:#111111,stroke-dasharray:5
                
                      1["<div style='font-weight: bold'>User 1</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 1 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph group2 ["Group 2"]
                      style group2 fill:#ffffff,stroke:#222222,color:#222222,stroke-dasharray:5
                
                      2["<div style='font-weight: bold'>User 2</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 2 fill:#ffffff,stroke:#444444,color:#444444
                    end
                
                    subgraph group3 ["Group 3"]
                      style group3 fill:#ffffff,stroke:#aabbcc,color:#aabbcc,stroke-dasharray:5
                
                      3["<div style='font-weight: bold'>User 3</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div>"]
                      style 3 fill:#ffffff,stroke:#444444,color:#444444
                    end
                

                  end""", diagram.getDefinition());
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

        Diagram diagram = new MermaidDiagramExporter().export(view);
        assertEquals("""
                graph TB
                  linkStyle default fill:#ffffff
                
                  subgraph diagram ["Title"]
                    style diagram fill:#ffffff,stroke:#ffffff
                
                    1["<div style='font-weight: bold'>A</div><div style='font-size: 70%; margin-top: 0px'></div>"]
                    style 1 fill:#ffffff,stroke:#444444,color:#444444
                    2["<div style='font-weight: bold'>B</div><div style='font-size: 70%; margin-top: 0px'>[Custom]</div><div style='font-size: 80%; margin-top:10px'>Description</div>"]
                    style 2 fill:#ffffff,stroke:#444444,color:#444444
                
                    1-. "<div>Uses</div><div style='font-size: 70%'></div>" .->2

                  end""", diagram.getDefinition());
    }

}