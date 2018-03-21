package com.structurizr.io.plantuml;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PlantUMLWriterTests {
    private static final String DATA_STORE_TAG = "DataStore";
    private static final String SOME_TAG = "Some";

    private PlantUMLWriter plantUMLWriter;
    private Workspace workspace;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        plantUMLWriter = new PlantUMLWriter();

        // Public plantuml.com/plantuml server limits dimensions to 2000, but local servers can be configured
        // differently. Setting limit here to 1999 to be provably different to plantuml.com AND still usable at the
        // public server AND bigger than A6 so that we can ensure smaller paper sizes are respected correctly.
        plantUMLWriter.setSizeLimit(1999);
        workspace = new Workspace("Name", "Description");
        stringWriter = new StringWriter();
    }

    @Test
    public void test_writeWorkspace_ThrowsAnExceptionWhenPassedANullWorkspace() throws Exception {
        try {
            plantUMLWriter.write((Workspace)null, null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_writeWorkspace_ThrowsAnExceptionWhenPassedANullWriter() throws Exception {
        try {
            plantUMLWriter.write(workspace, null);
            fail();
        } catch (Exception e) {
            assertEquals("A writer must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_writeView_ThrowsAnExceptionWhenPassedANullView() throws Exception {
        try {
            plantUMLWriter.write((View)null, null);
            fail();
        } catch (Exception e) {
            assertEquals("A view must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_writeView_ThrowsAnExceptionWhenPassedANullWriter() throws Exception {
        populateWorkspace();

        try {
            plantUMLWriter.write(workspace.getViews().getSystemLandscapeViews().stream().findFirst().get(), null);
            fail();
        } catch (Exception e) {
            assertEquals("A writer must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_writeWorkspace() throws Exception {
        populateWorkspace();

        plantUMLWriter.write(workspace, stringWriter);
        assertEquals(
                SYSTEM_LANDSCAPE_VIEW +
                SYSTEM_CONTEXT_VIEW +
                CONTAINER_VIEW +
                COMPONENT_VIEW +
                DYNAMIC_VIEW +
                DEPLOYMENT_VIEW, stringWriter.toString());
    }

    @Test
    public void test_writeEnterpriseContextView() throws Exception {
        populateWorkspace();

        SystemLandscapeView systemLandscapeView = workspace.getViews().getSystemLandscapeViews()
            .stream().findFirst().get();
        plantUMLWriter.write(systemLandscapeView, stringWriter);

        assertEquals(SYSTEM_LANDSCAPE_VIEW, stringWriter.toString());

    }

    @Test
    public void test_writeSystemContextView() throws Exception {
        populateWorkspace();

        SystemContextView systemContextView = workspace.getViews().getSystemContextViews()
            .stream().findFirst().get();
        plantUMLWriter.write(systemContextView, stringWriter);

        assertEquals(SYSTEM_CONTEXT_VIEW, stringWriter.toString());
    }

    @Test
    public void test_writeContainerView() throws Exception {
        populateWorkspace();

        ContainerView containerView = workspace.getViews().getContainerViews()
            .stream().findFirst().get();
        plantUMLWriter.write(containerView, stringWriter);

        assertEquals(CONTAINER_VIEW, stringWriter.toString());
    }

    @Test
    public void test_writeComponentsView() throws Exception {
        populateWorkspace();

        ComponentView componentView = workspace.getViews().getComponentViews()
            .stream().findFirst().get();
        plantUMLWriter.write(componentView, stringWriter);

        assertEquals(COMPONENT_VIEW, stringWriter.toString());
    }

    @Test
    public void test_writeDynamicView() throws Exception {
        populateWorkspace();

        DynamicView dynamicView = workspace.getViews().getDynamicViews()
                .stream().findFirst().get();
        plantUMLWriter.write(dynamicView, stringWriter);

        assertEquals(DYNAMIC_VIEW, stringWriter.toString());
    }

    @Test
    public void test_writeDeploymentView() throws Exception {
        populateWorkspace();

        DeploymentView deploymentView = workspace.getViews().getDeploymentViews()
                .stream().findFirst().get();
        plantUMLWriter.write(deploymentView, stringWriter);

        assertEquals(DEPLOYMENT_VIEW, stringWriter.toString());
    }

    private void populateWorkspace() {
        Model model = workspace.getModel();
        model.setEnterprise(new Enterprise("Some Enterprise"));

        Person user = model.addPerson(Location.Internal, "User",
                "A detailed description of the user to be displayed on the diagrams");
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Software System", "");
        user.uses(softwareSystem, "Uses");

        SoftwareSystem emailSystem = model.addSoftwareSystem(Location.External, "E-mail System", "An SMTP relay configured to send emails to the users.");
        softwareSystem.uses(emailSystem, "Sends e-mail using");
        emailSystem.delivers(user, "Delivers e-mails to");

        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "A relational database management system, likely PostgreSQL or MySQL but anything with JDBC drivers would be suitable.", "");
        database.addTags(DATA_STORE_TAG);
        user.uses(webApplication, "Uses", "HTTP");
        webApplication.uses(database, "Reads from and writes to", "JDBC");
        webApplication.uses(emailSystem, "Sends e-mail using");

        Component controller = webApplication.addComponent("SomeController", "", "Spring MVC Controller");
        controller.addTags(SOME_TAG);
        Component emailComponent = webApplication.addComponent("EmailComponent", "");
        Component repository = webApplication.addComponent("SomeRepository", "", "Spring Data");
        repository.addTags(SOME_TAG);
        user.uses(controller, "Uses", "HTTP");
        controller.uses(repository, "Uses");
        controller.uses(emailComponent, "Sends e-mail using");
        repository.uses(database, "Reads from and writes to", "JDBC");
        emailComponent.uses(emailSystem, "Sends e-mails using", "SMTP");

        DeploymentNode webServer = model.addDeploymentNode("Web Server", "A server hosted at AWS EC2.", "Ubuntu 12.04 LTS");
        webServer.addDeploymentNode("Apache Tomcat", "The live web server", "Apache Tomcat 8.x")
                .add(webApplication);
        DeploymentNode databaseServer = model.addDeploymentNode("Database Server", "A server hosted at AWS EC2.", "Ubuntu 12.04 LTS");
        databaseServer.addDeploymentNode("MySQL", "The live database server", "MySQL 5.5.x")
                .add(database);

        SystemLandscapeView
                systemLandscapeView = workspace.getViews().createSystemLandscapeView("enterpriseContext", "");
        systemLandscapeView.addAllElements();

        SystemContextView
            systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "systemContext", "");
        systemContextView.addAllElements();

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem, "containers", "");
        containerView.setPaperSize(PaperSize.A2_Landscape);
        containerView.addAllElements();

        ComponentView componentView = workspace.getViews().createComponentView(webApplication, "components", "");
        componentView.setPaperSize(PaperSize.A6_Portrait);
        componentView.addAllElements();

        DynamicView dynamicView = workspace.getViews().createDynamicView(webApplication, "dynamic", "");
        dynamicView.add(user, "Requests /something", controller);
        dynamicView.add(controller, repository);
        dynamicView.add(repository, "select * from something", database);

        DeploymentView deploymentView = workspace.getViews().createDeploymentView(softwareSystem, "deployment", "");
        deploymentView.addAllDeploymentNodes();

        Styles styles = workspace.getViews().getConfiguration().getStyles();
        styles.addElementStyle(DATA_STORE_TAG).shape(Shape.Cylinder);
    }

    @Test
    public void test_toString_ThrowsAnException_WhenPassedANullWorkspace() throws Exception {
        try {
            assertEquals(0, plantUMLWriter.toString(null).length);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_toStdOut_ThrowsAnException_WhenPassedANullWorkspace() throws Exception {
        try {
            plantUMLWriter.toStdOut(null);
            fail();
        } catch (Exception e) {
            assertEquals("A workspace must be provided.", e.getMessage());
        }
    }

    @Test
    public void test_toString_ReturnsAnEmptyArray_WhenTheWorkspaceContainsNoDiagrams() throws Exception {
        assertEquals(0, plantUMLWriter.toString(new Workspace("", "")).length);
    }

    @Test
    public void test_toString_ReturnsAnArrayOfDiagramsWhenThereAreDiagrams() throws Exception {
        populateWorkspace();
        String diagrams[] = plantUMLWriter.toString(workspace);
        assertEquals(6, diagrams.length);
        assertEquals(SYSTEM_LANDSCAPE_VIEW, diagrams[0]);
        assertEquals(SYSTEM_CONTEXT_VIEW, diagrams[1]);
        assertEquals(CONTAINER_VIEW, diagrams[2]);
        assertEquals(COMPONENT_VIEW, diagrams[3]);
        assertEquals(DYNAMIC_VIEW, diagrams[4]);
        assertEquals(DEPLOYMENT_VIEW, diagrams[5]);
    }

    @Test
    public void test_writeView_IncludesSkinParams_WhenSkinParamsAreAdded() throws Exception {
        workspace = new Workspace("", "");
        workspace.getModel().addSoftwareSystem("My software system", "").setLocation(Location.Internal);
        workspace.getViews().createSystemLandscapeView("thekey", "").addAllElements();
        plantUMLWriter.addSkinParam("handwritten", "true");

        plantUMLWriter.write(workspace, stringWriter);

        assertEquals("@startuml(id=thekey)" + System.lineSeparator() +
                "scale max 1999x1999" + System.lineSeparator() +
                "title System Landscape" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "skinparam {" + System.lineSeparator() +
                "  shadowing false" + System.lineSeparator() +
                "  arrowColor #707070" + System.lineSeparator() +
                "  actorBorderColor #707070" + System.lineSeparator() +
                "  componentBorderColor #707070" + System.lineSeparator() +
                "  rectangleBorderColor #707070" + System.lineSeparator() +
                "  noteBackgroundColor #ffffff" + System.lineSeparator() +
                "  noteBorderColor #707070" + System.lineSeparator() +
                "  handwritten true" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "package \"Enterprise\" {" + System.lineSeparator() +
                "  rectangle \"My software system\" <<Software System>> as 1 #dddddd" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "@enduml" + System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeView_IncludesStyling_WhenStylesAreAdded() throws Exception {
        workspace = new Workspace("", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("My software system", "");
        Person user = workspace.getModel().addPerson("A user", "");
        user.uses(softwareSystem, "Uses");
        workspace.getViews().createSystemContextView(softwareSystem, "key", "").addAllElements();
        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.PERSON).background("#00ff00");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).color("#0000ff");

        plantUMLWriter.write(workspace, stringWriter);

        assertEquals("@startuml(id=key)" + System.lineSeparator() +
                "scale max 1999x1999" + System.lineSeparator() +
                "title My software system - System Context" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "skinparam {" + System.lineSeparator() +
                "  shadowing false" + System.lineSeparator() +
                "  arrowColor #707070" + System.lineSeparator() +
                "  actorBorderColor #707070" + System.lineSeparator() +
                "  componentBorderColor #707070" + System.lineSeparator() +
                "  rectangleBorderColor #707070" + System.lineSeparator() +
                "  noteBackgroundColor #ffffff" + System.lineSeparator() +
                "  noteBorderColor #707070" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "rectangle \"A user\" <<Person>> as 2 #00ff00" + System.lineSeparator() +
                "rectangle \"My software system\" <<Software System>> as 1 #ff0000" + System.lineSeparator() +
                "2 .[#0000ff].> 1 : Uses" + System.lineSeparator() +
                "@enduml" + System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeView_IncludesIncludes_WhenIncludesAreAdded() throws Exception {
        workspace = new Workspace("", "");
        SoftwareSystem softwareSystem = workspace.getModel().addSoftwareSystem("My software system", "");
        Person user = workspace.getModel().addPerson("A user", "");
        user.uses(softwareSystem, "Uses");
        workspace.getViews().createSystemContextView(softwareSystem, "thekey", "").addAllElements();
        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.SOFTWARE_SYSTEM).background("#ff0000");
        workspace.getViews().getConfiguration().getStyles().addElementStyle(Tags.PERSON).background("#00ff00");
        workspace.getViews().getConfiguration().getStyles().addRelationshipStyle(Tags.RELATIONSHIP).color("#0000ff");

        plantUMLWriter.addIncludeFile("do-not-include-me.puml");
        plantUMLWriter.clearIncludes();
        plantUMLWriter.addIncludeFile("whole-file.puml");
        plantUMLWriter.addIncludeFile("unnamed-diagrams.puml", 0);
        plantUMLWriter.addIncludeFile("named-diagrams.puml", "diagram-name");
        plantUMLWriter.addIncludeURL(URI.create("http://example.com/whole-file.puml"));
        plantUMLWriter.addIncludeURL(URI.create("http://example.com/unnamed-diagrams.puml"), 0);
        plantUMLWriter.addIncludeURL(URI.create("http://example.com/named-diagrams.puml"), "diagram-name");

        plantUMLWriter.clearSkinParams();

        plantUMLWriter.write(workspace, stringWriter);

        assertEquals("@startuml(id=thekey)" + System.lineSeparator() +
            "!include whole-file.puml" + System.lineSeparator() +
            "!include unnamed-diagrams.puml!0" + System.lineSeparator() +
            "!include named-diagrams.puml!diagram-name" + System.lineSeparator() +
            "!includeurl http://example.com/whole-file.puml" + System.lineSeparator() +
            "!includeurl http://example.com/unnamed-diagrams.puml!0" + System.lineSeparator() +
            "!includeurl http://example.com/named-diagrams.puml!diagram-name" + System.lineSeparator() +
            "scale max 1999x1999" + System.lineSeparator() +
            "title My software system - System Context" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "rectangle \"A user\" <<Person>> as 2 #00ff00" + System.lineSeparator() +
            "rectangle \"My software system\" <<Software System>> as 1 #ff0000" + System.lineSeparator() +
            "2 .[#0000ff].> 1 : Uses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator(), stringWriter.toString());
    }

    private static final String SYSTEM_LANDSCAPE_VIEW = "@startuml(id=enterpriseContext)" + System.lineSeparator() +
            "scale max 1999x1999" + System.lineSeparator() +
            "title System Landscape for Some Enterprise" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "rectangle 4 <<Software System>> #dddddd [" + System.lineSeparator() +
            "  E-mail System" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  An SMTP relay configured to" + System.lineSeparator() +
            "  send emails to the users." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "package \"Some Enterprise\" {" + System.lineSeparator() +
            "  rectangle 1 <<Person>> #dddddd [" + System.lineSeparator() +
            "    User" + System.lineSeparator() +
            "    --" + System.lineSeparator() +
            "    A detailed description of the" + System.lineSeparator() +
            "    user to be displayed on the" + System.lineSeparator() +
            "    diagrams" + System.lineSeparator() +
            "  ]" + System.lineSeparator() +
            "  rectangle \"Software System\" <<Software System>> as 2 #dddddd" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "4 .[#707070].> 1 : Delivers e-mails to" + System.lineSeparator() +
            "2 .[#707070].> 4 : Sends e-mail using" + System.lineSeparator() +
            "1 .[#707070].> 2 : Uses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

    private static final String SYSTEM_CONTEXT_VIEW = "@startuml(id=systemContext)" + System.lineSeparator() +
            "scale max 1999x1999" + System.lineSeparator() +
            "title Software System - System Context" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "rectangle 4 <<Software System>> #dddddd [" + System.lineSeparator() +
            "  E-mail System" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  An SMTP relay configured to" + System.lineSeparator() +
            "  send emails to the users." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "rectangle \"Software System\" <<Software System>> as 2 #dddddd" + System.lineSeparator() +
            "rectangle 1 <<Person>> #dddddd [" + System.lineSeparator() +
            "  User" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A detailed description of the" + System.lineSeparator() +
            "  user to be displayed on the" + System.lineSeparator() +
            "  diagrams" + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "4 .[#707070].> 1 : Delivers e-mails to" + System.lineSeparator() +
            "2 .[#707070].> 4 : Sends e-mail using" + System.lineSeparator() +
            "1 .[#707070].> 2 : Uses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

    private static final String CONTAINER_VIEW = "@startuml(id=containers)" + System.lineSeparator() +
            "scale max 1999x1413" + System.lineSeparator() +
            "title Software System - Containers" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "rectangle 4 <<Software System>> #dddddd [" + System.lineSeparator() +
            "  E-mail System" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  An SMTP relay configured to" + System.lineSeparator() +
            "  send emails to the users." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "rectangle 1 <<Person>> #dddddd [" + System.lineSeparator() +
            "  User" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A detailed description of the" + System.lineSeparator() +
            "  user to be displayed on the" + System.lineSeparator() +
            "  diagrams" + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "package \"Software System\" <<Software System>> {" + System.lineSeparator() +
            "  database 8 <<Container>> #dddddd [" + System.lineSeparator() +
            "    Database" + System.lineSeparator() +
            "    --" + System.lineSeparator() +
            "    A relational database" + System.lineSeparator() +
            "    management system, likely" + System.lineSeparator() +
            "    PostgreSQL or MySQL but" + System.lineSeparator() +
            "    anything with JDBC drivers" + System.lineSeparator() +
            "    would be suitable." + System.lineSeparator() +
            "  ]" + System.lineSeparator() +
            "  rectangle \"Web Application\" <<Container>> as 7 #dddddd" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "4 .[#707070].> 1 : Delivers e-mails to" + System.lineSeparator() +
            "1 .[#707070].> 7 : <<HTTP>>\\nUses" + System.lineSeparator() +
            "7 .[#707070].> 8 : <<JDBC>>\\nReads from and writes to" + System.lineSeparator() +
            "7 .[#707070].> 4 : Sends e-mail using" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

    private static final String COMPONENT_VIEW = "@startuml(id=components)" + System.lineSeparator() +
            "scale max 1240x1748" + System.lineSeparator() +
            "title Software System - Web Application - Components" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "database 8 <<Container>> #dddddd [" + System.lineSeparator() +
            "  Database" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A relational database" + System.lineSeparator() +
            "  management system, likely" + System.lineSeparator() +
            "  PostgreSQL or MySQL but" + System.lineSeparator() +
            "  anything with JDBC drivers" + System.lineSeparator() +
            "  would be suitable." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "rectangle 4 <<Software System>> #dddddd [" + System.lineSeparator() +
            "  E-mail System" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  An SMTP relay configured to" + System.lineSeparator() +
            "  send emails to the users." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "rectangle 1 <<Person>> #dddddd [" + System.lineSeparator() +
            "  User" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A detailed description of the" + System.lineSeparator() +
            "  user to be displayed on the" + System.lineSeparator() +
            "  diagrams" + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "package \"Web Application\" <<Container>> {" + System.lineSeparator() +
            "  component \"EmailComponent\" <<Component>> as 13 #dddddd" + System.lineSeparator() +
            "  component \"SomeController\" <<Spring MVC Controller>> as 12 #dddddd" + System.lineSeparator() +
            "  component \"SomeRepository\" <<Spring Data>> as 14 #dddddd" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "4 .[#707070].> 1 : Delivers e-mails to" + System.lineSeparator() +
            "13 .[#707070].> 4 : <<SMTP>>\\nSends e-mails using" + System.lineSeparator() +
            "12 .[#707070].> 13 : Sends e-mail using" + System.lineSeparator() +
            "12 .[#707070].> 14 : Uses" + System.lineSeparator() +
            "14 .[#707070].> 8 : <<JDBC>>\\nReads from and writes to" + System.lineSeparator() +
            "1 .[#707070].> 12 : <<HTTP>>\\nUses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

    private static final String DYNAMIC_VIEW = "@startuml(id=dynamic)" + System.lineSeparator() +
            "scale max 1999x1999" + System.lineSeparator() +
            "title Web Application - Dynamic" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "database 8 <<Container>> #dddddd [" + System.lineSeparator() +
            "  Database" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A relational database" + System.lineSeparator() +
            "  management system, likely" + System.lineSeparator() +
            "  PostgreSQL or MySQL but" + System.lineSeparator() +
            "  anything with JDBC drivers" + System.lineSeparator() +
            "  would be suitable." + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "component \"SomeController\" <<Spring MVC Controller>> as 12 #dddddd" + System.lineSeparator() +
            "component \"SomeRepository\" <<Spring Data>> as 14 #dddddd" + System.lineSeparator() +
            "rectangle 1 <<Person>> #dddddd [" + System.lineSeparator() +
            "  User" + System.lineSeparator() +
            "  --" + System.lineSeparator() +
            "  A detailed description of the" + System.lineSeparator() +
            "  user to be displayed on the" + System.lineSeparator() +
            "  diagrams" + System.lineSeparator() +
            "]" + System.lineSeparator() +
            "1 -[#707070]> 12 : 1. Requests /something" + System.lineSeparator() +
            "12 -[#707070]> 14 : 2. Uses" + System.lineSeparator() +
            "14 -[#707070]> 8 : 3. select * from something" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

    private static final String DEPLOYMENT_VIEW = "@startuml(id=deployment)" + System.lineSeparator() +
            "scale max 1999x1999" + System.lineSeparator() +
            "title Software System - Deployment" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "skinparam {" + System.lineSeparator() +
            "  shadowing false" + System.lineSeparator() +
            "  arrowColor #707070" + System.lineSeparator() +
            "  actorBorderColor #707070" + System.lineSeparator() +
            "  componentBorderColor #707070" + System.lineSeparator() +
            "  rectangleBorderColor #707070" + System.lineSeparator() +
            "  noteBackgroundColor #ffffff" + System.lineSeparator() +
            "  noteBorderColor #707070" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "node \"Database Server\" <<Ubuntu 12.04 LTS>> as 23 {" + System.lineSeparator() +
            "  node \"MySQL\" <<MySQL 5.5.x>> as 24 {" + System.lineSeparator() +
            "    database \"Database\" <<Container>> as 25 #dddddd" + System.lineSeparator() +
            "  }" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "node \"Web Server\" <<Ubuntu 12.04 LTS>> as 20 {" + System.lineSeparator() +
            "  node \"Apache Tomcat\" <<Apache Tomcat 8.x>> as 21 {" + System.lineSeparator() +
            "    rectangle \"Web Application\" <<Container>> as 22 #dddddd" + System.lineSeparator() +
            "  }" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "22 .[#707070].> 25 : <<JDBC>>\\nReads from and writes to" + System.lineSeparator() +
            "@enduml" + System.lineSeparator();

}
