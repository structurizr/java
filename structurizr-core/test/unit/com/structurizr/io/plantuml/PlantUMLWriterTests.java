package com.structurizr.io.plantuml;

import com.structurizr.Workspace;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class PlantUMLWriterTests {

    private PlantUMLWriter plantUMLWriter;
    private Workspace workspace;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        plantUMLWriter = new PlantUMLWriter();
        workspace = new Workspace("Name", "Description");
        stringWriter = new StringWriter();
    }

    @Test
    public void test_writeWorkspace_DoesNotThrowAnExceptionWhenPassedNullParameters() throws Exception {
        plantUMLWriter.write((Workspace) null, null);
        plantUMLWriter.write(workspace, null);
        plantUMLWriter.write((Workspace) null, stringWriter);
    }

    @Test
    public void test_writeView_DoesNotThrowAnExceptionWhenPassedNullParameters() throws Exception {
        populateWorkspace();

        plantUMLWriter.write((View) null, null);
        plantUMLWriter.write(workspace.getViews().getEnterpriseContextViews().stream().findFirst().get(), null);
        plantUMLWriter.write((View) null, stringWriter);
    }

    @Test
    public void test_writeWorkspace() throws Exception {
        populateWorkspace();

        plantUMLWriter.write(workspace, stringWriter);
        assertEquals("@startuml" + System.lineSeparator() +
                "title Enterprise Context for Some Enterprise" + System.lineSeparator() +
                "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
                "package SomeEnterprise {" + System.lineSeparator() +
                "  actor User" + System.lineSeparator() +
                "  [Software System] <<Software System>> as SoftwareSystem" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
                "SoftwareSystem ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
                "User ..> SoftwareSystem : Uses" + System.lineSeparator() +
                "@enduml" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "@startuml" + System.lineSeparator() +
                "title Software System - System Context" + System.lineSeparator() +
                "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
                "[Software System] <<Software System>> as SoftwareSystem" + System.lineSeparator() +
                "actor User" + System.lineSeparator() +
                "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
                "SoftwareSystem ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
                "User ..> SoftwareSystem : Uses" + System.lineSeparator() +
                "@enduml" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "@startuml" + System.lineSeparator() +
                "title Software System - Containers" + System.lineSeparator() +
                "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
                "actor User" + System.lineSeparator() +
                "package SoftwareSystem {" + System.lineSeparator() +
                "  [Database] <<Container>> as Database" + System.lineSeparator() +
                "  [Web Application] <<Container>> as WebApplication" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
                "User ..> WebApplication : Uses <<HTTP>>" + System.lineSeparator() +
                "WebApplication ..> Database : Reads from and writes to <<JDBC>>" + System.lineSeparator() +
                "WebApplication ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
                "@enduml" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "@startuml" + System.lineSeparator() +
                "title Software System - Web Application - Components" + System.lineSeparator() +
                "[Database] <<Container>> as Database" + System.lineSeparator() +
                "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
                "actor User" + System.lineSeparator() +
                "package WebApplication {" + System.lineSeparator() +
                "  [EmailComponent] <<Component>> as EmailComponent" + System.lineSeparator() +
                "  [SomeController] <<Spring MVC Controller>> as SomeController" + System.lineSeparator() +
                "  [SomeRepository] <<Spring Data>> as SomeRepository" + System.lineSeparator() +
                "}" + System.lineSeparator() +
                "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
                "EmailComponent ..> EmailSystem : Sends e-mails using <<SMTP>>" + System.lineSeparator() +
                "SomeController ..> EmailComponent : Sends e-mail using" + System.lineSeparator() +
                "SomeController ..> SomeRepository : Uses" + System.lineSeparator() +
                "SomeRepository ..> Database : Reads from and writes to <<JDBC>>" + System.lineSeparator() +
                "User ..> SomeController : Uses <<HTTP>>" + System.lineSeparator() +
                "@enduml" + System.lineSeparator() +
                "" + System.lineSeparator() +
                "@startuml" + System.lineSeparator() +
                "title Web Application - Dynamic" + System.lineSeparator() +
                "actor User" + System.lineSeparator() +
                "User -> SomeController : Requests /something" + System.lineSeparator() +
                "SomeController -> SomeRepository : Uses" + System.lineSeparator() +
                "SomeRepository -> Database : select * from something" + System.lineSeparator() +
                "@enduml" + System.lineSeparator() +
                System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeEnterpriseContextView() throws Exception {
        populateWorkspace();

        EnterpriseContextView enterpriseContextView = workspace.getViews().getEnterpriseContextViews()
            .stream().findFirst().get();
        plantUMLWriter.write(enterpriseContextView, stringWriter);

        assertEquals("@startuml" + System.lineSeparator() +
            "title Enterprise Context for Some Enterprise" + System.lineSeparator() +
            "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
            "package SomeEnterprise {" + System.lineSeparator() +
            "  actor User" + System.lineSeparator() +
            "  [Software System] <<Software System>> as SoftwareSystem" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
            "SoftwareSystem ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
            "User ..> SoftwareSystem : Uses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator() +
            System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeSystemContextView() throws Exception {
        populateWorkspace();

        SystemContextView systemContextView = workspace.getViews().getSystemContextViews()
            .stream().findFirst().get();
        plantUMLWriter.write(systemContextView, stringWriter);

        assertEquals("@startuml" + System.lineSeparator() +
            "title Software System - System Context" + System.lineSeparator() +
            "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
            "[Software System] <<Software System>> as SoftwareSystem" + System.lineSeparator() +
            "actor User" + System.lineSeparator() +
            "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
            "SoftwareSystem ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
            "User ..> SoftwareSystem : Uses" + System.lineSeparator() +
            "@enduml" + System.lineSeparator() +
            "" + System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeContainerView() throws Exception {
        populateWorkspace();

        ContainerView containerView = workspace.getViews().getContainerViews()
            .stream().findFirst().get();
        plantUMLWriter.write(containerView, stringWriter);

        assertEquals("@startuml" + System.lineSeparator() +
            "title Software System - Containers" + System.lineSeparator() +
            "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
            "actor User" + System.lineSeparator() +
            "package SoftwareSystem {" + System.lineSeparator() +
            "  [Database] <<Container>> as Database" + System.lineSeparator() +
            "  [Web Application] <<Container>> as WebApplication" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
            "User ..> WebApplication : Uses <<HTTP>>" + System.lineSeparator() +
            "WebApplication ..> Database : Reads from and writes to <<JDBC>>" + System.lineSeparator() +
            "WebApplication ..> EmailSystem : Sends e-mail using" + System.lineSeparator() +
            "@enduml" + System.lineSeparator() +
            "" + System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeComponentsView() throws Exception {
        populateWorkspace();

        ComponentView componentView = workspace.getViews().getComponentViews()
            .stream().findFirst().get();
        plantUMLWriter.write(componentView, stringWriter);

        assertEquals("@startuml" + System.lineSeparator() +
            "title Software System - Web Application - Components" + System.lineSeparator() +
            "[Database] <<Container>> as Database" + System.lineSeparator() +
            "[E-mail System] <<Software System>> as EmailSystem" + System.lineSeparator() +
            "actor User" + System.lineSeparator() +
            "package WebApplication {" + System.lineSeparator() +
            "  [EmailComponent] <<Component>> as EmailComponent" + System.lineSeparator() +
            "  [SomeController] <<Spring MVC Controller>> as SomeController" + System.lineSeparator() +
            "  [SomeRepository] <<Spring Data>> as SomeRepository" + System.lineSeparator() +
            "}" + System.lineSeparator() +
            "EmailSystem ..> User : Delivers e-mails to" + System.lineSeparator() +
            "EmailComponent ..> EmailSystem : Sends e-mails using <<SMTP>>" + System.lineSeparator() +
            "SomeController ..> EmailComponent : Sends e-mail using" + System.lineSeparator() +
            "SomeController ..> SomeRepository : Uses" + System.lineSeparator() +
            "SomeRepository ..> Database : Reads from and writes to <<JDBC>>" + System.lineSeparator() +
            "User ..> SomeController : Uses <<HTTP>>" + System.lineSeparator() +
            "@enduml" + System.lineSeparator() +
            "" + System.lineSeparator(), stringWriter.toString());
    }

    @Test
    public void test_writeDynamicView() throws Exception {
        populateWorkspace();

        DynamicView dynamicView = workspace.getViews().getDynamicViews()
            .stream().findFirst().get();
        plantUMLWriter.write(dynamicView, stringWriter);

        assertEquals("@startuml" + System.lineSeparator() +
            "title Web Application - Dynamic" + System.lineSeparator() +
            "actor User" + System.lineSeparator() +
            "User -> SomeController : Requests /something" + System.lineSeparator() +
            "SomeController -> SomeRepository : Uses" + System.lineSeparator() +
            "SomeRepository -> Database : select * from something" + System.lineSeparator() +
            "@enduml" + System.lineSeparator() +
            System.lineSeparator(), stringWriter.toString());
    }

    private void populateWorkspace() {
        Model model = workspace.getModel();
        model.setEnterprise(new Enterprise("Some Enterprise"));

        Person user = model.addPerson(Location.Internal, "User", "");
        SoftwareSystem softwareSystem = model.addSoftwareSystem(Location.Internal, "Software System", "");
        user.uses(softwareSystem, "Uses");

        SoftwareSystem emailSystem = model.addSoftwareSystem(Location.External, "E-mail System", "");
        softwareSystem.uses(emailSystem, "Sends e-mail using");
        emailSystem.delivers(user, "Delivers e-mails to");

        Container webApplication = softwareSystem.addContainer("Web Application", "", "");
        Container database = softwareSystem.addContainer("Database", "", "");
        user.uses(webApplication, "Uses", "HTTP");
        webApplication.uses(database, "Reads from and writes to", "JDBC");
        webApplication.uses(emailSystem, "Sends e-mail using");

        Component controller = webApplication.addComponent("SomeController", "", "Spring MVC Controller");
        Component emailComponent = webApplication.addComponent("EmailComponent", "");
        Component repository = webApplication.addComponent("SomeRepository", "", "Spring Data");
        user.uses(controller, "Uses", "HTTP");
        controller.uses(repository, "Uses");
        controller.uses(emailComponent, "Sends e-mail using");
        repository.uses(database, "Reads from and writes to", "JDBC");
        emailComponent.uses(emailSystem, "Sends e-mails using", "SMTP");

        EnterpriseContextView
            enterpriseContextView = workspace.getViews().createEnterpriseContextView("enterpriseContext", "");
        enterpriseContextView.addAllElements();

        SystemContextView
            systemContextView = workspace.getViews().createSystemContextView(softwareSystem, "systemContext", "");
        systemContextView.addAllElements();

        ContainerView containerView = workspace.getViews().createContainerView(softwareSystem, "containers", "");
        containerView.addAllElements();

        ComponentView componentView = workspace.getViews().createComponentView(webApplication, "components", "");
        componentView.addAllElements();

        DynamicView dynamicView = workspace.getViews().createDynamicView(webApplication, "dynamic", "");
        dynamicView.add(user, "Requests /something", controller);
        dynamicView.add(controller, repository);
        dynamicView.add(repository, "select * from something", database);
    }

}
